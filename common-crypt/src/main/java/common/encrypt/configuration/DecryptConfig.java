package common.encrypt.configuration;

import cn.hutool.crypto.symmetric.AES;
import common.encrypt.configuration.properties.CryptProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author Zack Zhang
 */
@Slf4j
@AllArgsConstructor
@ConditionalOnWebApplication
@Configuration
@EnableAutoConfiguration
@ConditionalOnProperty(name = "common.encrypt.enabled")
@EnableConfigurationProperties(CryptProperties.class)
public class DecryptConfig {

    @Resource private CryptProperties cryptProperties;

    @PostConstruct
    public void check() {
        CryptProperties.Properties encrypt = cryptProperties.getDecrypt();
        assert Objects.isNull(encrypt) : "decrypt config can not be null.";
    }

    @Bean
    public AES decryptAes() {
        CryptProperties.Properties decrypt = cryptProperties.getDecrypt();
        return new AES(
                decrypt.getMode(),
                decrypt.getPadding(),
                decrypt.getKey().getBytes(StandardCharsets.UTF_8),
                decrypt.getIv().getBytes(StandardCharsets.UTF_8));
    }
}
