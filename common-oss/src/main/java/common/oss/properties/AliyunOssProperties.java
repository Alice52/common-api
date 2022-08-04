package common.oss.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zack <br>
 * @create 2021-06-22 14:57 <br>
 * @project swagger-3 <br>
 */
@ConfigurationProperties(prefix = "common.oss.aliyun")
public class AliyunOssProperties extends OssProperties {}
