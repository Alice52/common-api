package common.oss.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.*;
import common.core.constant.enums.CommonResponseEnum;
import common.core.exception.BaseException;
import common.oss.annotation.OssType;
import common.oss.constnats.enums.OssUploadTypeEnum;
import common.oss.properties.AliyunOssProperties;
import common.oss.service.OSSHander;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author zack <br>
 * @create 2021-06-22 16:18 <br>
 * @project swagger-3 <br>
 */
@Slf4j
@Component
@EnableConfigurationProperties(AliyunOssProperties.class)
@ConditionalOnProperty(name = "common.oss.aliyun.enable", matchIfMissing = false)
@OssType(type = OssUploadTypeEnum.aliyun)
public class AliyunOssHandler implements OSSHander {

    @Autowired private AliyunOssProperties ossProperties;
    private OSS ossClient;

    @Override
    @PostConstruct
    public void init() {
        ossClient =
                new OSSClientBuilder()
                        .build(
                                ossProperties.getEndpoint(),
                                ossProperties.getAccessKey(),
                                ossProperties.getAccessKeySecret());
    }

    @Override
    public String getExportPath() {
        return Optional.ofNullable(ossProperties.getExportPath()).orElse("file/download/export/");
    }

    @Override
    public Map<String, String> upload(String fileName, File file, Map<String, String> headers) {
        Map<String, String> uploadResult = new HashMap<>();

        PutObjectResult result;
        try {
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(file.length());
            if (headers != null) {
                headers.forEach(meta::setHeader);
            }
            PutObjectRequest request =
                    new PutObjectRequest(ossProperties.getBucket(), fileName, file);
            request.setMetadata(meta);
            result = ossClient.putObject(request);
            log.debug("OSS上传成功：{}", result);
        } catch (Exception e) {
            throw new BaseException(CommonResponseEnum.OSS_UPLOAD_ERROR, e);
        }

        String host =
                StrUtil.isBlank(ossProperties.getCdnHost())
                        ? ossProperties.getHost()
                        : ossProperties.getCdnHost();
        uploadResult.put("host", host);
        return uploadResult;
    }

    @Override
    public JSONObject signature() throws UnsupportedEncodingException {
        String host =
                "https://" + ossProperties.getBucket() + StrUtil.DOT + ossProperties.getEndpoint();
        String dir = new SimpleDateFormat(DatePattern.NORM_DATE_PATTERN).format(new Date());

        long expireEndTime = System.currentTimeMillis() + 30 * 1000;
        Date expiration = new Date(expireEndTime);
        PolicyConditions policyConds = new PolicyConditions();
        policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
        policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

        String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
        byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
        String encodedPolicy = BinaryUtil.toBase64String(binaryData);
        String postSignature = ossClient.calculatePostSignature(postPolicy);

        JSONObject respMap = new JSONObject();
        respMap.put("accessid", ossProperties.getAccessKey());
        respMap.put("policy", encodedPolicy);
        respMap.put("signature", postSignature);
        respMap.put("dir", dir);
        respMap.put("host", host);
        respMap.put("expire", String.valueOf(expireEndTime / 1000));

        return respMap;
    }
}
