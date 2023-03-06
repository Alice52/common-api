package common.oss.service.impl;

import cn.hutool.core.util.StrUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.region.Region;
import com.tencent.cloud.CosStsClient;
import common.core.constant.enums.CommonResponseEnum;
import common.core.exception.BaseException;
import common.oss.annotation.OssType;
import common.oss.constnats.enums.OssUploadTypeEnum;
import common.oss.properties.TencentOSSProperties;
import common.oss.service.OSSHander;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * @author zack <br>
 * @create 2021-06-22 16:18 <br>
 * @project swagger-3 <br>
 */
@Slf4j
@Component
@EnableConfigurationProperties(TencentOSSProperties.class)
@ConditionalOnProperty(name = "common.oss.tencent.enable", matchIfMissing = false)
@OssType(type = OssUploadTypeEnum.tencent)
public class TencentOssHandler implements OSSHander {

    @Resource private TencentOSSProperties ossProperties;
    private COSClient cosClient;

    @Override
    @PostConstruct
    public void init() {
        COSCredentials cred =
                new BasicCOSCredentials(
                        ossProperties.getAccessKey(), ossProperties.getAccessKeySecret());
        Region region = new Region(ossProperties.getEndpoint());
        ClientConfig clientConfig = new ClientConfig(region);
        clientConfig.setHttpProtocol(HttpProtocol.https);
        cosClient = new COSClient(cred, clientConfig);
    }

    @Override
    public String getExportPath() {
        return Optional.ofNullable(ossProperties.getExportPath()).orElse("file/download/export/");
    }

    /**
     * TODO: check necessary properties.
     *
     * @return
     */
    @Override
    @SneakyThrows
    public JSONObject signature() {
        TreeMap<String, Object> config = new TreeMap<String, Object>();
        config.put("SecretId", ossProperties.getAccessKey());
        config.put("SecretKey", ossProperties.getAccessKeySecret());
        // 临时密钥有效时长，单位是秒，默认1800秒，目前主账号最长2小时（即7200秒），子账号最长36小时（即129600秒）
        config.put("durationSeconds", Integer.parseInt(ossProperties.getDurationSeconds()));
        config.put("bucket", ossProperties.getBucket());
        config.put("region", ossProperties.getEndpoint());
        config.put("allowPrefix", "*");
        // 密钥的权限列表。简单上传、表单上传和分片上传需要以下的权限，其他权限列表请看
        // https://cloud.tencent.com/document/product/436/31923
        String[] allowActions = new String[] {"name/cos:*"};
        config.put("allowActions", allowActions);
        try {
            JSONObject credential = CosStsClient.getCredential(config);
            credential.put("region", ossProperties.getEndpoint());
            credential.put("bucket", ossProperties.getBucket());
            credential.put("cdnHost", ossProperties.getCdnHost());
            credential.put("host", ossProperties.getHost());

            return credential;
        } catch (Exception e) {
            throw new BaseException(CommonResponseEnum.OSS_COS_CREDENTIAL_ERROR, e);
        }
    }

    @Override
    public Map<String, String> upload(String fileName, File file, Map<String, String> headers) {
        Map<String, String> uploadResult = new HashMap<>();

        try {
            com.qcloud.cos.model.PutObjectResult result;
            com.qcloud.cos.model.PutObjectRequest request =
                    new com.qcloud.cos.model.PutObjectRequest(
                            ossProperties.getBucket(), fileName, file);
            result = cosClient.putObject(request);
            log.debug("COS上传成功：{}", result);
        } catch (Exception e) {
            throw new BaseException(CommonResponseEnum.OSS_UPLOAD_ERROR, e);
        }
        //        finally {
        //            cosClient.shutdown();
        //        }

        String host =
                StrUtil.isBlank(ossProperties.getCdnHost())
                        ? ossProperties.getHost()
                        : ossProperties.getCdnHost();
        uploadResult.put("host", host);
        return uploadResult;
    }
}
