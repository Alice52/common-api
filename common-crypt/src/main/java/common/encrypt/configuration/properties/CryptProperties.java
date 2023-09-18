package common.encrypt.configuration.properties;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Zack Zhang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("common")
public class CryptProperties {

    private Properties encrypt;
    private Properties decrypt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Properties {

        private boolean enabled = false;
        private Mode mode;
        private Padding padding;
        private String key;
        private String iv;
    }
}
