package common.core.util.system;

import cn.hutool.core.util.StrUtil;
import org.springframework.context.annotation.ConditionContext;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * @author zack <br>
 * @create 2021-06-25<br>
 * @project project-custom <br>
 */
public class EnvPropertiesUtil {

    /**
     * Get property value from system, Such as -Dp=1
     *
     * @param key
     * @return
     */
    @Nullable
    public static String getPropertiesFromSystem(String key) {
        return Optional.ofNullable(System.getenv(key)).orElse(System.getProperty(key));
    }

    /**
     * Get property value, including yml config. *
     *
     * @param key
     * @param context
     * @return
     */
    @Nullable
    public static String getProperties(String key, ConditionContext context) {
        String property = getPropertiesFromSystem(key);

        if (StrUtil.isBlank(property)) {
            property = context.getEnvironment().getProperty(key);
        }

        return property;
    }
}
