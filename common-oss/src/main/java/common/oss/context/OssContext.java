package common.oss.context;

import common.core.constant.enums.CommonResponseEnum;
import common.oss.annotation.OssType;
import common.oss.annotation.OssTypeImpl;
import common.oss.condition.CustomCondition;
import common.oss.constnats.enums.OssUploadTypeEnum;
import common.oss.service.OSSHander;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zack <br>
 * @create 2021-06-22 17:33 <br>
 * @project swagger-3 <br>
 */
@Component
@Conditional(CustomCondition.class)
public class OssContext {
    private Map<OssType, OSSHander> handlerMap;

    /**
     * Use OssType as key.
     *
     * @param handlers
     */
    @Autowired
    public OssContext(List<OSSHander> handlers) {
        handlerMap =
                handlers.stream()
                        .collect(Collectors.toMap(OssContext::type, v -> v, (v1, v2) -> v1));
        CommonResponseEnum.OSS_CUSTOM_ERROR.assertNotEmptyWithMsg(
                handlerMap, "Oss Context Is Null, please Check Config!");
    }

    private static OssType type(OSSHander handler) {
        return AnnotationUtils.findAnnotation(handler.getClass(), OssType.class);
    }

    /**
     * Get different bean for order handle. <br>
     * 还有一种就是 直接把任务丢进 Context, Context 根据上下文去处理<br>
     *
     * @param typeEnum
     * @return
     */
    public OSSHander getHandler(OssUploadTypeEnum typeEnum) {

        OssType orderHandlerType = new OssTypeImpl(typeEnum);
        OSSHander ossHander = handlerMap.get(orderHandlerType);

        CommonResponseEnum.OSS_CUSTOM_ERROR.assertNotNullWithMsg(
                ossHander, "Oss Context Is Null, please Check Config!");

        return ossHander;
    }
}
