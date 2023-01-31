package common.logging.desensitize.v2;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.google.common.collect.Maps;
import lombok.experimental.UtilityClass;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.*;

/**
 * @author zack
 */
@UtilityClass
public class YmlUtil {

    /** desensitize configuration file name */
    public String PROPERTY_NAME = "logback-desensitize.yml";

    /* start symbol of desensitize configuration */
    public final String YML_HEAD_KEY = "log-desensitize";

    public final String ENABLE_FLAG = "enable";

    public final String IGNORE = "ignore";

    public final String PATTERN = "pattern";

    public final String PATTERNS = "patterns";

    public final String CUSTOM = "custom";

    public final String KEY = "key";

    public final String DEFAULT_REGEX = "defaultRegex";
    public final String POSITION = "position";
    public final String CUSTOM_REGEX = "customRegex";

    public Map<String, Object> desensitiveConfigCache;

    public final DumperOptions OPTIONS = new DumperOptions();

    static {
        OPTIONS.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        desensitiveConfigCache = loadYmlConfig(PROPERTY_NAME, YML_HEAD_KEY);
    }

    /**
     * obtain all pattern, including pattern and patterns.<br>
     * notice pattern has high priority. <br>
     *
     * <pre>
     *   Map<String, String> onceKey =  builder()
     *            .put("customRegex", "^0[0-9]{2,3}-[0-9]{7,8}")
     *            .put("position", "1,3")
     *            .build();
     *   ImmutableList<Map<String, String>> moreKeys = ImmutableList.builder()
     *                         .add(builder().put("defaultRegex", "phone").put("position", "4,7").build())
     *                         .add(builder().put("defaultRegex", "email").put("position", "xx)").build())
     *                         .add(builder().put("defaultRegex", "ixy").put("position", "xx").build())
     *                         .add(builder().put("customRegex", "^1{10}").put("position", "1,3").build())
     *                         .add(builder().put("defaultRegex", "other").put("position", "1,3").build())
     *                         .build();
     *    map = builder()
     *          .put("identity", "9, 13")
     *          .put("email", "@>(4,7)")
     *          .put("password", "password")
     *          .put("localMobile", onceKey)
     *          .put("phone", moreKeys)
     *          .build();
     *
     * </pre>
     *
     * @return pattern <key, string> || <key, map<string, string>>
     */
    public Map<String, Object> getAllPattern() {

        Map<String, Object> patterns = getPatterns();
        patterns.putAll(getPattern());

        return patterns;
    }

    /**
     * whether enabled desensitize, and default value is false.
     *
     * @return whether enabled desensitize
     */
    public Boolean getEnableConfig() {
        Object flag = desensitiveConfigCache.get(ENABLE_FLAG);
        return flag instanceof Boolean ? (Boolean) flag : false;
    }

    /**
     * whether ignore letter case, and default value is true.
     *
     * @return whether ignore letter case
     */
    public Boolean getIgnoreConfig() {
        Object flag = desensitiveConfigCache.get(IGNORE);
        return flag instanceof Boolean ? (Boolean) flag : true;
    }

    /**
     * Get Maps of yml specified key configuration.
     *
     * @param fileName yml file name
     * @param keyName specified key name
     * @return specified yml config info
     */
    private Map<String, Object> loadYmlConfig(String fileName, String keyName) {
        try {
            HashMap map = loadYmlConfig(fileName);
            Object keyConfig;
            if (MapUtil.isEmpty(map) || !((keyConfig = map.get(keyName)) instanceof Map)) {
                return Maps.newHashMap();
            }

            return (Map<String, Object>) keyConfig;
        } catch (Exception e) {
            return Maps.newHashMap();
        }
    }

    /**
     * Get Maps of yml configuration.
     *
     * @param fileName yml file name
     * @return yml config info
     */
    private HashMap loadYmlConfig(String fileName) {
        InputStream inputStream = YmlUtil.class.getClassLoader().getResourceAsStream(fileName);
        return new Yaml(OPTIONS).loadAs(inputStream, HashMap.class);
    }

    private Map<String, Object> getPattern() {
        Object pattern = desensitiveConfigCache.get(PATTERN);

        if (Objects.nonNull(pattern) && pattern instanceof Map) {
            return (Map<String, Object>) pattern;
        } else {
            return Maps.newHashMap();
        }
    }

    private Map<String, Object> getPatterns() {
        Map<String, Object> map = new HashMap<>();

        Object patterns = desensitiveConfigCache.get(PATTERNS);
        // 1. patterns will be list if contains more keys
        if (patterns instanceof List) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) patterns;
            if (CollUtil.isEmpty(list)) {
                return Maps.newHashMap();
            }
            list.forEach(x -> assembleMap(map, x));

            return map;
        } else if (patterns instanceof Map) {
            // 2. patterns will be map if just one key configured
            assembleMap(map, (Map<String, Object>) patterns);

            return map;
        }

        return Maps.newHashMap();
    }

    /**
     * build pattern as <code> map<key, rule> </code>
     *
     * @param map map
     * @param patterns patterns
     */
    private void assembleMap(Map<String, Object> map, Map<String, Object> patterns) {
        Object key = patterns.get(KEY);
        if (!(key instanceof String)) {
            return;
        }

        Arrays.stream(((String) key).replace(" ", "").split(","))
                .forEach(x -> map.put(x, patterns.get(CUSTOM)));
    }
}
