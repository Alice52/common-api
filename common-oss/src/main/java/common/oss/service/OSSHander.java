package common.oss.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.naming.OperationNotSupportedException;

import cn.hutool.json.JSONUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import common.core.constant.enums.CommonResponseEnum;
import common.core.exception.BaseException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zack <br>
 * @create 2021-06-22 15:38 <br>
 * @project swagger-3 <br>
 */
public interface OSSHander {

    Logger log = LoggerFactory.getLogger(OSSHander.class);

    /** Init oss client */
    default void init() throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }

    /**
     * Get policy and signature info, then send post request to host with signature info, <br>
     * it will put object to oss.
     *
     * @return Map contains signature info for front end upload.
     */
    JSONObject signature() throws UnsupportedEncodingException;

    /**
     * update file directly
     *
     * @param fileName
     * @param file
     * @param headers
     * @return
     */
    Map<String, String> upload(String fileName, File file, Map<String, String> headers);

    /**
     * update file directly
     *
     * @param fileName
     * @param file
     * @return
     */
    default Map<String, String> upload(String fileName, File file) {
        return upload(fileName, file, null);
    }

    /**
     * download file from specified bucket.
     *
     * @param client
     * @param bucket
     * @param fileKey
     * @return
     */
    default OSSObject downLoad(OSS client, String bucket, String fileKey) {
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
     * 获取配置文件的导出路径
     *
     * @return
     */
    String getExportPath();
}
