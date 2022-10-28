package common.core.component;

import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

/**
 * @author zack <br>
 * @create 2021-06-04 16:42 <br>
 * @project custom-test <br>
 */
@Slf4j
@Component
public class AppContextHolder {
    private static ThreadLocal<Map<String, Object>> CONTEXT_HOLDER = new ThreadLocal<>();

    public static void upsertByKey(String prefix, String key, Object value) {
        getContextHolder().put(prefix + key, value);
    }

    @Deprecated
    public static void upsertByKey(String key, Object value) {
        getContextHolder().put(key, value);
    }

    public static <T> T getByKey(String prefix, String key) {
        key = prefix + key;
        Object value = getContextHolder().get(key);
        if (ObjectUtil.isNull(value)) {
            return null;
        }

        return (T) value;
    }

    @Deprecated
    public static <T> T getByKey(String key, Class<T> clazz) {
        Object value = getContextHolder().get(key);
        if (ObjectUtil.isNull(value)) {
            return null;
        }

        T t = null;
        try {
            t = clazz.cast(value);
        } catch (ClassCastException ex) {
            log.warn("cast object: {} to type: {} error: {}", value, clazz, ex);
        }

        return t;
    }

    public static void removeByKey(String prefix, String key) {
        if (contain(prefix, key)) {
            remove(key);
        }
    }

    @Deprecated
    public static void removeByKey(String key) {
        if (contain(key)) {
            remove(key);
        }
    }

    public static boolean contain(String prefix, String key) {
        key = prefix + key;
        Map<String, Object> objectMap = getContextHolder();

        return objectMap.containsKey(key);
    }

    @Deprecated
    public static boolean contain(String key) {
        Map<String, Object> objectMap = getContextHolder();

        return objectMap.containsKey(key);
    }

    /**
     * Get appHolder value of map.<br>
     * If not exist, it will be initialized.
     *
     * @return
     */
    private static Map<String, Object> getContextHolder() {
        return initMapIfNeed(CONTEXT_HOLDER.get());
    }

    private static void remove(String key) {
        CONTEXT_HOLDER.get().remove(key);
    }

    private static Map<String, Object> initMapIfNeed(Map<String, Object> objectMap) {
        if (ObjectUtil.isNotNull(objectMap)) {
            return objectMap;
        } else {
            return initAppHolder();
        }
    }

    private static Map<String, Object> initAppHolder() {
        CONTEXT_HOLDER.set(new HashMap<>(16));

        return CONTEXT_HOLDER.get();
    }
}
