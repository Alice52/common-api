package common.logging.desensitize.v2;

import cn.hutool.core.map.MapUtil;
import com.google.common.collect.Maps;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.hutool.core.text.CharSequenceUtil.EMPTY;
import static cn.hutool.core.text.CharSequenceUtil.NULL;
import static common.logging.desensitize.v2.YmlUtil.*;
import static java.util.Objects.isNull;
import static org.apache.logging.log4j.util.Strings.isBlank;

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

    /** 匹配非数字 */
    public final Pattern REGEX_NUM = Pattern.compile("[^0-9]");

    public Boolean enable = null;

    public Boolean ignoreCase = null;

    public Map<String, Object> allPattern = YmlUtil.getAllPattern();

    public Map<String, Object> lowerCaseAllPattern;

    public final String PHONE = "phone";
    public final String EMAIL = "email";
    public final String IDENTITY = "identity";
    public final String OTHER = "other";
    public final String PASSWORD = "password";

    /**
     * 将event对象的formattedMessage脱敏
     *
     * @param message LoggingEvent的formattedMessage属性
     * @return 脱敏后的日志信息
     */
    public String customChange(String message) {
        try {
            // 原始信息 - 格式化后的
            String originalMessage = message;

            Map<String, Object> config = YmlUtil.desensitiveConfigCache;
            if (MapUtil.isEmpty(config) || !enableDesensitize()) {
                return originalMessage;
            }

            // todo: this reg will poor performance
            Matcher regexMatcher = REGEX_PATTERN.matcher(message);

            // 如果部分匹配（一个对象/JSON字符串/Map/List<对象/Map>等会有多个匹配），就根据分组来获取key和value
            while (regexMatcher.find()) {

                // 获取key: 将引号替换掉，去掉两边空格（JSON字符串去引号）
                String key =
                        Optional.ofNullable(regexMatcher.group(1))
                                .orElseGet(() -> EMPTY)
                                .replaceAll("\"", "")
                                .trim();

                // 获取原始Value
                String rawValue =
                        Optional.ofNullable(regexMatcher.group(3))
                                .orElseGet(() -> EMPTY)
                                .replaceAll("\"", "")
                                .trim();
                // 获取Key对应规则
                Object rules = getPatternRules(key);

                if (isNull(rules) || isBlank(rawValue) || NULL.equals(rawValue)) {
                    continue;
                }

                // todo: what's this ?? password?
                if (!rawValue.equalsIgnoreCase(key)) {
                    continue;
                }

                String ruleOrPosition = firstAppliedRule(rules, rawValue).replaceAll(" ", "");
                // when is not specified rule, such as email | password, which define in pattern
                // and rule's position is empty, which define in patterns' custom
                if (EMPTY.equals(ruleOrPosition)) {
                    continue;
                }

                // password rule. which will be log as ******
                if (PASSWORD.equalsIgnoreCase(ruleOrPosition)) {
                    rawValue = "******";
                    originalMessage = replaceTuple(originalMessage, regexMatcher, rawValue);
                    continue;
                }

                String rawRuleOrPosition = ruleOrPosition;
                String position = getRulePosition(ruleOrPosition);
                if (!EMPTY.equals(position)) {
                    ruleOrPosition = position;
                }

                rawValue = getReplaceValue(rawValue, ruleOrPosition, rawRuleOrPosition);
                //  if (rawValue != null && !"".equals(rawValue)) {
                originalMessage = replaceTuple(originalMessage, regexMatcher, rawValue);
                // }
            }

            return originalMessage;
        } catch (Exception e) {
            // 捕获到异常，直接返回结果(空字符串) - 这个异常可能发生的场景：同时开启控制台和输出文件的时候
            // 当控制台进行一次脱敏之后，文件的再去脱敏，是对脱敏后的message脱敏，则正则匹配会出现错误
            // 比如123456789@.com 脱敏后：123***456789@qq.com，正则匹配到123，这个123去substring的时候会出错
            return "";
        }
    }

    private static String replaceTuple(
            String originalMessage, Matcher regexMatcher, String rawValue) {
        String origin = regexMatcher.group(1) + regexMatcher.group(2) + regexMatcher.group(3);
        return originalMessage.replace(
                origin, regexMatcher.group(1) + regexMatcher.group(2) + rawValue);
    }

    /**
     * 获取替换后的value
     *
     * @param value value
     * @param position 核心规则
     * @param rawRuleOrPosition 原始规则
     * @return
     */
    private String getReplaceValue(String value, String position, String rawRuleOrPosition) {

        String[] split = position.split(",");

        if (split.length >= 2 && !"".equals(position)) {
            String append = "";
            String start = REGEX_NUM.matcher(split[0]).replaceAll("");
            String end = REGEX_NUM.matcher(split[1]).replaceAll("");
            int startSub = Integer.parseInt(start) - 1;
            int endSub = Integer.parseInt(end) - 1;
            // 脱敏起点/结尾符下标
            int index;
            String flagSub;
            int indexOf;
            int newValueL;
            String newValue;
            // 脱敏结尾
            if (rawRuleOrPosition.contains(">")) {
                // 获取>的下标
                index = rawRuleOrPosition.indexOf(">");
                // 获取标志符号
                flagSub = rawRuleOrPosition.substring(0, index);
                // 获取标志符号的下标
                indexOf = value.indexOf(flagSub);
                // 获取标志符号前面数据
                newValue = value.substring(0, indexOf);
                // 获取数据的长度
                newValueL = newValue.length();
                // 获取标识符及后面的数据
                append = value.substring(indexOf);
                value =
                        doDesensitize(
                                        Math.max(startSub, 0),
                                        endSub >= 0
                                                ? (endSub <= newValueL ? endSub : newValueL - 1)
                                                : 0,
                                        newValue)
                                + append;
            } else if (rawRuleOrPosition.contains("<")) {
                // 脱敏起点
                index = rawRuleOrPosition.indexOf("<");
                flagSub = rawRuleOrPosition.substring(0, index);
                indexOf = value.indexOf(flagSub);
                newValue = value.substring(indexOf + 1);
                newValueL = newValue.length();
                append = value.substring(0, indexOf + 1);
                value =
                        append
                                + doDesensitize(
                                        Math.max(startSub, 0),
                                        endSub >= 0
                                                ? (endSub <= newValueL ? endSub : newValueL - 1)
                                                : 0,
                                        newValue);
            } else if (rawRuleOrPosition.contains(",")) {
                newValueL = value.length();
                value =
                        doDesensitize(
                                Math.max(startSub, 0),
                                endSub >= 0 ? (endSub <= newValueL ? endSub : newValueL - 1) : 0,
                                value);
            }
        } else if (!"".equals(position)) {
            int beforeIndexOf = position.indexOf("*");
            int last = position.length() - position.lastIndexOf("*");
            int lastIndexOf = value.length() - last;
            value = doDesensitize(beforeIndexOf, lastIndexOf, value);
        }
        return value;
    }

    /**
     * Get pattern's rule by key
     *
     * @param key key
     * @return rules
     */
    private Object getPatternRules(String key) {

        // init for flag and lower case convert
        if (isNull(ignoreCase)) {
            ignoreCase = YmlUtil.getIgnoreConfig();
            if (ignoreCase) {
                lowerCaseAllPattern = transformUpperCase(allPattern);
            }
        }

        if (ignoreCase) {
            return lowerCaseAllPattern.get(key.toLowerCase());
        } else {
            return allPattern.get(key);
        }
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

            // todo: check value ?? what's this?
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

    private Boolean enableDesensitize() {

        if (isNull(enable)) {
            return YmlUtil.getEnableConfig();
        }

        return enable;
    }

    /**
     * 脱敏处理
     *
     * @param start 脱敏开始下标
     * @param end 脱敏结束下标
     * @param value value
     * @return
     */
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
