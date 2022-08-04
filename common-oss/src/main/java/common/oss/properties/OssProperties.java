package common.oss.properties;

import lombok.Data;

/**
 * @author zack <br>
 * @create 2021-06-22 17:25 <br>
 * @project swagger-3 <br>
 */
@Data
public abstract class OssProperties {
    /** secretId */
    private String accessKey;
    /** secretKey */
    private String accessKeySecret;
    /** region */
    private String endpoint;

    private String bucket;
    private String cdnHost;
    private String host;
    private String exportPath;

    private String durationSeconds;
}
