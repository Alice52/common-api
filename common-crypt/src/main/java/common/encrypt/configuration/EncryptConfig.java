package common.encrypt.configuration;

import cn.hutool.crypto.symmetric.AES;
import common.encrypt.advice.EncryptResponseAdvice;
import common.encrypt.aspect.EncryptAspect;
import common.encrypt.configuration.properties.CryptProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static cn.hutool.core.text.CharSequenceUtil.*;

/**
 * @author Zack Zhang
 */
@Slf4j
@AllArgsConstructor
@ConditionalOnWebApplication
@Configuration
@Import({EncryptAspect.class, EncryptResponseAdvice.class})
@EnableAutoConfiguration
@ConditionalOnExpression("${common.encrypt.enabled:true}")
@EnableConfigurationProperties(CryptProperties.class)
public class EncryptConfig {

    @Resource private CryptProperties cryptProperties;

    @PostConstruct
    public void check() {
        CryptProperties.Properties encrypt = cryptProperties.getEncrypt();
        assert Objects.isNull(encrypt) : "encrypt config can not be null.";

        String iv = encrypt.getIv();
        if (isNotBlank(iv)) {
            assert length(iv) < 16 : "iv must be 16 bytes long";
            encrypt.setIv(sub(iv, 0, 16));
        }
    }

    @Bean
    public AES encryptAes() {
        CryptProperties.Properties encrypt = cryptProperties.getEncrypt();
        String iv = encrypt.getIv();
        return new AES(
                encrypt.getMode(),
                encrypt.getPadding(),
                encrypt.getKey().getBytes(StandardCharsets.UTF_8),
                isBlank(iv) ? null : iv.getBytes(StandardCharsets.UTF_8));
    }
}
