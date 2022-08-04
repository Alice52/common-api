package common.oss.condition;

import cn.hutool.core.util.StrUtil;
import common.core.util.EnvPropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * At least one of tencent and aliyun is enabled.
 *
 * @author zack <br>
 * @create 2021-06-25<br>
 * @project project-custom <br>
 */
@Slf4j
public class CustomCondition implements Condition {

    static String TENCENT_ENABLED = "common.oss.tencent.enable";
    static String ALIYUN_ENABLED = "common.oss.aliyun.enable";

    private static boolean match(String enabled) {
        return StrUtil.isNotBlank(enabled)
                && enabled.equalsIgnoreCase(String.valueOf(Boolean.TRUE));
    }

    /**
     * At least one of tencent and aliyun is enabled
     *
     * @param context the condition context
     * @param metadata metadata of the {@link org.springframework.core.type.AnnotationMetadata
     *     class}, which is annotation info
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

        String enabled = EnvPropertiesUtil.getProperties(TENCENT_ENABLED, context);
        if (match(enabled)) {
            return true;
        }
        enabled = EnvPropertiesUtil.getProperties(ALIYUN_ENABLED, context);

        return match(enabled);
    }
}
