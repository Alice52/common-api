package common.http.configuration;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author asd <br>
 * @create 2021-12-07 4:42 PM <br>
 * @project project-cloud-custom <br>
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "common.http")
public class HttpProperties {
    private String host;

    private String grantType = StrUtil.EMPTY;
    private String clientId = StrUtil.EMPTY;
    private String clientSecret = StrUtil.EMPTY;
    private String accessTokenUrl;
    private String decryptTokenUrl;

    private String decryptClientId = StrUtil.EMPTY;
    private String decryptClientSecret = StrUtil.EMPTY;

    private Integer maxRetryTimes = 1;
    /** @see java.util.concurrent.TimeUnit#MILLISECONDS */
    private Integer retrySleep = 10;
}
