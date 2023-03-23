package common.oss.v2.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotBlank;

/**
 * @author T04856 <br>
 * @create 2023-03-23 4:36 PM <br>
 * @project project-cloud-custom <br>
 */
@Data
@ConfigurationProperties(prefix = "common.oss")
public class OssProperties {
    /** 对象存储服务的URL */
    @NotBlank private String endpoint;

    /** 区域 */
    private String region;

    /**
     * 只是url的显示不一样
     *
     * <pre>
     *   1. true: path-style
     *          nginx 反向代理和S3默认支持 pathStyle模式 {http://endpoint/bucketname}
     *   2. false: supports virtual-hosted-style
     *          阿里云等需要配置为 virtual-hosted-style 模式{http://bucketname.endpoint}
     * </pre>
     */
    private Boolean pathStyleAccess = true;

    /** Access key */
    @NotBlank private String accessKey;

    /** Secret key */
    @NotBlank private String secretKey;

    /** 最大线程数，默认： 100 */
    private Integer maxConnections = 100;

    private OssVender vender;

    /** 腾讯 bucket-name 有个事要求: bucketName-appid */
    private String cosAppId;

    public enum OssVender {
        COS,
        OSS,
        MINIO,
    }
}
