package common.oss.context;

import cn.hutool.json.JSONUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import common.core.constant.enums.CommonResponseEnum;
import common.core.exception.BaseException;
import common.oss.annotation.OssType;
import common.oss.annotation.OssTypeImpl;
import common.oss.condition.CustomCondition;
import common.oss.constnats.enums.OssUploadTypeEnum;
import common.oss.service.OSSHander;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zack <br>
 * @create 2021-06-22 17:33 <br>
 * @project swagger-3 <br>
 */
@Slf4j
@Component
@Conditional(CustomCondition.class)
public class OssContextV2 {
    private Map<OssType, OSSHander> handlerMap;

    /**
     * Use OssType as key.
     *
     * @param handlers
     */
    @Autowired
    public OssContextV2(List<OSSHander> handlers) {
        handlerMap =
                handlers.stream()
                        .collect(Collectors.toMap(OssContextV2::type, v -> v, (v1, v2) -> v1));
        CommonResponseEnum.OSS_CUSTOM_ERROR.assertNotEmptyWithMsg(
                handlerMap, "Oss Context Is Null, please Check Config!");
    }

    private static OssType type(OSSHander handler) {
        return AnnotationUtils.findAnnotation(handler.getClass(), OssType.class);
    }

    public JSONObject signature(OssUploadTypeEnum typeEnum) throws UnsupportedEncodingException {
        OSSHander handler = getHandler(typeEnum);
        return handler.signature();
    }

    public Map<String, String> upload(
            String fileName, File file, Map<String, String> headers, OssUploadTypeEnum typeEnum) {

        OSSHander handler = getHandler(typeEnum);
        return handler.upload(fileName, file, headers);
    }

    public String getExportPath(OssUploadTypeEnum typeEnum) {
        OSSHander handler = getHandler(typeEnum);
        return handler.getExportPath();
    }

    public OSSObject downLoad(OSS client, String bucket, String fileKey) {

        OSSObject ossObject;
        try {
            ossObject = client.getObject(bucket, fileKey);
            log.debug("OSS下载成功：{}", JSONUtil.toJsonStr(ossObject));
        } catch (Exception e) {
            throw new BaseException(CommonResponseEnum.OSS_DOWNLOAD_ERROR, e);
        }

        return ossObject;
    }

    /**
     * 直接把任务丢进 Context, Context 根据上下文去处理
     *
     * @param typeEnum
     * @return
     */
    private OSSHander getHandler(OssUploadTypeEnum typeEnum) {

        OssType orderHandlerType = new OssTypeImpl(typeEnum);
        OSSHander ossHander = handlerMap.get(orderHandlerType);

        CommonResponseEnum.OSS_CUSTOM_ERROR.assertNotNullWithMsg(
                ossHander, "Oss Context Is Null, please Check Config!");

        return ossHander;
    }
}
