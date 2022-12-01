package common.encrypt.configuration;

import cn.hutool.crypto.symmetric.AES;
import common.encrypt.advice.EncryptResponseAdvice;
import common.encrypt.configuration.properties.CryptProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@Import(EncryptResponseAdvice.class)
@EnableAutoConfiguration
@ConditionalOnProperty(name = "common.encrypt.enabled")
@EnableConfigurationProperties(CryptProperties.class)
public class DecryptConfig {

    @Resource private CryptProperties cryptProperties;

    @PostConstruct
    public void check() {
        CryptProperties.Properties encrypt = cryptProperties.getDecrypt();
        assert Objects.isNull(encrypt) : "decrypt config can not be null.";

        String iv = encrypt.getIv();
        if (isNotBlank(iv)) {
            assert length(iv) < 16 : "iv must be 16 bytes long";
            encrypt.setIv(sub(iv, 0, 16));
        }
    }

    @Bean
    public AES decryptAes() {
        CryptProperties.Properties decrypt = cryptProperties.getDecrypt();
        String iv = decrypt.getIv();

        return new AES(
                decrypt.getMode(),
                decrypt.getPadding(),
                decrypt.getKey().getBytes(StandardCharsets.UTF_8),
                isBlank(iv) ? null : iv.getBytes(StandardCharsets.UTF_8));
    }
}
