package common.encrypt.configuration;

import cn.hutool.crypto.symmetric.AES;
import common.encrypt.configuration.properties.CryptProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
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
@ConditionalOnExpression("${common.decrypt.enabled:true} || ${common.encrypt.enabled:true}")
@EnableConfigurationProperties(CryptProperties.class)
public class EncryptConfig {

    @Resource private CryptProperties cryptProperties;

    @PostConstruct
    public void check() {
        CryptProperties.Properties encrypt = cryptProperties.getEncrypt();
        assert Objects.isNull(encrypt) : "encrypt config can not be null.";
    }

    @Bean
    public AES encryptAes() {
        CryptProperties.Properties encrypt = cryptProperties.getEncrypt();
        return new AES(
                encrypt.getMode(),
                encrypt.getPadding(),
                encrypt.getKey().getBytes(StandardCharsets.UTF_8),
                encrypt.getIv().getBytes(StandardCharsets.UTF_8));
    }
}
