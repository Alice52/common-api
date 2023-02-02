package common.logging.desensitize.v2;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.hutool.core.text.CharSequenceUtil.*;
import static common.logging.desensitize.v2.YmlUtil.*;
import static java.util.Objects.isNull;
import static org.apache.logging.log4j.util.Strings.isBlank;

/**
 * @author Zack Zhang
 */
@UtilityClass
public class DesensitizationUtil {

    /**
     * 正则匹配模式
     *
     * <pre>
     *    1. group(1): key
     *    2. group(2): 分隔符(：和=)
     *    3. group(3): value
     * </pre>
     *
     * issue:
     *
     * <pre>
     *    1. 这个正则太复杂了: 重写
     *    2. 该正则表达式第三个() 可能无法匹配以某些特殊符号开头和结尾的<br>
     *    3. 如密码这种字段(前后如果有很多特殊字段), 则无法匹配, 建议密码直接加密, 无需脱敏
     * </pre>
     */
    public final Pattern REGEX_PATTERN =
            Pattern.compile(
                    "\\s*([\"]?[\\w]+[\"]?)(\\s*[:=]+[^\\u4e00-\\u9fa5@,.*{\\[\\w]*\\s*)([\\u4e00-\\u9fa5_\\-@.\\w]+)[\\W&&[^\\-@.]]?\\s*");

    public final String PHONE = "phone";
    public final String EMAIL = "email";
    public final String IDENTITY = "identity";
    public final String OTHER = "other";
    public final String PASSWORD = "password";
    private Boolean enable = null;
    private Boolean ignoreCase;
    private Map<String, Object> allPattern;
    private Map<String, Object> lowerCaseAllPattern;

    static {
        allPattern = YmlUtil.getAllPattern();
        ignoreCase = getIgnoreCase();
    }

    /**
     * 将event对象的formattedMessage脱敏
     *
     * @param message LoggingEvent的formattedMessage属性
     * @return 脱敏后的日志信息
     */
    public String customChange(String message) {
        try {
            // not enable desensitize feature or message is blank
            Map<String, Object> config = YmlUtil.desensitiveConfigCache;
            if (MapUtil.isEmpty(config) || !enableDesensitize() || StrUtil.isBlank(message)) {
                return message;
            }

            // contains no keyword, which need to be desensitized
            if (!containsKeyword(message)) {
                return message;
            }

            // 原始信息 - 格式化后的
            String originalMessage = message;

            Matcher regexMatcher = REGEX_PATTERN.matcher(message);
            while (regexMatcher.find()) {
                // group(1)就是key, group(2)就是分隔符(如：和=), group(3)就是value

                // 获取原始Value
                String rawValue =
                        Optional.ofNullable(regexMatcher.group(3))
                                .orElseGet(() -> EMPTY)
                                .replace("\"", EMPTY)
                                .trim();
                // 获取Key对应规则
                Object rules = getConfiguredRules(regexMatcher.group(1));
                if (isNull(rules) || isBlank(rawValue) || NULL.equals(rawValue)) {
                    continue;
                }

                String ruleOrPosition = firstAppliedRule(rules, rawValue).replaceAll(SPACE, EMPTY);
                // when is not specified rule, such as email | password, which define in pattern
                // and rule's position is empty, which define in patterns' custom
                if (EMPTY.equals(ruleOrPosition)) {
                    continue;
                }

                // password rule. which will be log as ******
                if (PASSWORD.equalsIgnoreCase(ruleOrPosition)) {
                    originalMessage = replaceTuple(originalMessage, regexMatcher, "******");
                    continue;
                }

                String rawRuleOrPosition = ruleOrPosition;
                String position = getRulePosition(ruleOrPosition);
                if (!EMPTY.equals(position)) {
                    ruleOrPosition = position;
                }

                String dValue = getDesensitizedValue(rawValue, ruleOrPosition, rawRuleOrPosition);
                originalMessage = replaceTuple(originalMessage, regexMatcher, dValue);
            }

            return originalMessage;
        } catch (Exception e) {
            return message;
        }
    }

    /**
     * whether the formatted message contains keyword.
     *
     * @param message
     * @return
     */
    private static boolean containsKeyword(String message) {

        for (String keyword : allPattern.keySet()) {
            if (containsIgnoreCase(message, keyword)) {
                return true;
            }
        }

        return false;
    }

    /**
     * replace this matched k-v for desensitize
     *
     * @param originalMessage full length message
     * @param regexMatcher current matched tuple
     * @param rawValue desensitized value
     * @return
     */
    private static String replaceTuple(
            String originalMessage, Matcher regexMatcher, String rawValue) {
        String origin = regexMatcher.group(1) + regexMatcher.group(2) + regexMatcher.group(3);
        return originalMessage.replace(
                origin, regexMatcher.group(1) + regexMatcher.group(2) + rawValue);
    }

    /**
     * Calculate desensitized value by position info.
     *
     * @param value value
     * @param position such as 1,3
     * @param rawRuleOrPosition origin rule{`@<(1,3)`} or position{`1,3`}
     * @return
     */
    private String getDesensitizedValue(String value, String position, String rawRuleOrPosition) {

        final int two = 2;
        final String ast = "*";

        if (EMPTY.equals(position)) {
            return value;
        }

        String[] split = position.split(StrPool.COMMA);
        if (split.length != two && !position.contains(ast)) {
            return value;
        }

        // position is nc***b: which means just remain first two and last one letter
        int length = value.length();
        if (position.contains(ast)) {
            int start = position.indexOf(ast);
            int end = length - (position.length() - position.lastIndexOf(ast));
            return CharSequenceUtil.hide(value, start, end + 1);
        }

        // rawRuleOrPosition is 1,3 || rawRuleOrPosition is @>(1,3)
        if (position.equals(rawRuleOrPosition) || rawRuleOrPosition.contains(">")) {
            if (NumberUtil.isNumber(split[0]) && NumberUtil.isNumber(split[1])) {
                int start = NumberUtil.parseInt(split[0]);
                int end = NumberUtil.parseInt(split[1]);
                return CharSequenceUtil.hide(value, start, end + 1);
            }
            return value;
        }

        // position is @<(1,3)
        if (rawRuleOrPosition.contains("<")) {
            if (NumberUtil.isNumber(split[0]) && NumberUtil.isNumber(split[1])) {
                int start = NumberUtil.parseInt(split[0]);
                int end = NumberUtil.parseInt(split[1]);
                return CharSequenceUtil.hide(value, length - end, length - start + 1);
            }
            return value;
        }

        return value;
    }

    /**
     * Get pattern's rule by key <br>
     * todo: how to guarantee the configured sequence
     *
     * @param key key
     * @return rules string | map | list
     */
    private Object getConfiguredRules(String key) {

        key = Optional.ofNullable(key).orElseGet(() -> EMPTY).replace("\"", EMPTY).trim();

        if (Boolean.TRUE.equals(ignoreCase)) {
            return lowerCaseAllPattern.get(key.toUpperCase());
        } else {
            return allPattern.get(key);
        }
    }

    /**
     * init for flag and lower case convert
     *
     * @return
     */
    private boolean getIgnoreCase() {
        if (isNull(ignoreCase)) {
            ignoreCase = YmlUtil.getIgnoreConfig();
            if (Boolean.TRUE.equals(ignoreCase)) {
                lowerCaseAllPattern = transformUpperCase(allPattern);
            }

            return ignoreCase;
        }

        return ignoreCase;
    }

    /**
     * convert pattern's key to upper case for ignore case func.
     *
     * @param pattern pattern
     * @return upper case key pattern
     */
    public Map<String, Object> transformUpperCase(Map<String, Object> pattern) {
        Map<String, Object> resultMap = new HashMap<>(pattern.size());

        if (MapUtil.isEmpty(pattern)) {
            return Maps.newHashMap();
        }

        pattern.forEach((k, v) -> resultMap.put(k.toUpperCase(), v));

        return resultMap;
    }

    /**
     * Get first applied rule or custom's position, which will be used to desensitize message
     *
     * @param rules pattern rules
     * @param rawValue raw value of message in group(3)
     * @return first applied rule or custom's position, such as "@>(4,7)" || password || 9,13
     */
    private String firstAppliedRule(Object rules, String rawValue) {

        // in case of: pattern key is `@>(4,7)` || password
        if (rules instanceof String) {
            return (String) rules;
        }

        // in case of: patterns key just one
        if (rules instanceof Map) {
            return appliedPosition((Map<String, Object>) rules, rawValue);
        }

        // in case of: patterns key  more than one
        if (rules instanceof List) {
            // 使用第一个匹配上的规则
            for (Map<String, Object> map : (List<Map<String, Object>>) rules) {
                String rule = appliedPosition(map, rawValue);
                if (!EMPTY.equals(rule)) {
                    return rule;
                }
            }
        }

        return EMPTY;
    }

    /**
     * Get rule's position string.
     *
     * @param map rule, such as <code>
     *      map.put("customRegex | defaultRegex", "^0[0-9]{7,8}").put("position", "1,3") </code>
     * @param value raw value before desensitize
     * @return
     */
    private String appliedPosition(Map<String, Object> map, String value) {
        if (MapUtil.isEmpty(map)) {
            return EMPTY;
        }

        // if map's key is defaultRegex
        if (map.containsKey(DEFAULT_REGEX)) {
            String defaultRegex = map.get(DEFAULT_REGEX).toString();
            String position = map.get(POSITION).toString();

            // notice: this will check value pattern again
            if (IDENTITY.equals(defaultRegex) && RegUtil.isIdentity(value)) {
                return position;
            }

            if (EMAIL.equals(defaultRegex) && RegUtil.isEmail(value)) {
                return position;
            }

            if (PHONE.equals(defaultRegex) && RegUtil.isMobile(value)) {
                return position;
            }

            if (OTHER.equals(defaultRegex)) {
                return position;
            }
        }

        // if map's key is customRegex
        if (map.containsKey(CUSTOM_REGEX)) {
            String customRegex = map.get(CUSTOM_REGEX).toString();
            if (value.matches(customRegex)) {
                return map.get(POSITION).toString();
            }
        }

        return EMPTY;
    }

    /**
     * return position of rule
     *
     * @param ruleOrPosition position or rule, such as "@>(4,7)"
     * @return position
     */
    private String getRulePosition(String ruleOrPosition) {
        if (!ruleOrPosition.contains("(")) {
            return EMPTY;
        }

        int startCons = ruleOrPosition.indexOf("(");
        int endCons = ruleOrPosition.indexOf(")");
        return ruleOrPosition.substring(startCons + 1, endCons);
    }

    private boolean enableDesensitize() {

        if (isNull(enable)) {
            return YmlUtil.getEnableConfig();
        }

        return enable;
    }

    /**
     * 脱敏处理
     *
     * @deprecated
     * @param start 脱敏开始下标
     * @param end 脱敏结束下标
     * @param value value
     * @return
     */
    @Deprecated
    public String doDesensitize(int start, int end, String value) {

        char[] chars;
        int i;
        // 正常情况 - end在数组长度内
        if (start >= 0 && end + 1 <= value.length()) {
            chars = value.toCharArray();
            // 脱敏替换
            for (i = start; i < chars.length && i < end + 1; ++i) {
                chars[i] = '*';
            }
            return new String(chars);
        } else if (start >= 0 && end >= value.length()) {
            // 非正常情况 - end在数组长度外
            chars = value.toCharArray();
            for (i = start; i < chars.length; ++i) {
                chars[i] = '*';
            }
            return new String(chars);
        } else {
            // 不符要求，不脱敏
            return value;
        }
    }
}
