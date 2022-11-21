package common.http.configuration;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static common.http.configuration.HttpProperties.DecryptTypeEnum.FULL;

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

    private String decryptClientId = StrUtil.EMPTY;
    private String decryptClientSecret = StrUtil.EMPTY;
    private String decryptTokenUrl;
    private DecryptTypeEnum decryptType = FULL;

    private Integer maxRetryTimes = 1;
    /**
     * @see java.util.concurrent.TimeUnit#MILLISECONDS
     */
    private Integer retrySleep = 10;

    @Getter
    @AllArgsConstructor
    public enum DecryptTypeEnum {
        FULL("full response is encrypted data"),
        DATA("part response is encrypted data"),
        ITEM("content response is encrypted data"),
        ;

        String desc;
    }
}
