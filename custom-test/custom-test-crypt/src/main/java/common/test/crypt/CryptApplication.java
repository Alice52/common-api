package common.test.crypt;

import common.encrypt.annotation.EnableDecrypt;
import common.encrypt.annotation.EnableEncrypt;
import common.redis.config.ExcludeRedisConfig;
import common.swagger.annotation.EnableSwagger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * @author Zack Zhang
 */
@EnableSwagger
@EnableEncrypt
@EnableDecrypt
@Import(ExcludeRedisConfig.class)
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, RabbitAutoConfiguration.class})
public class CryptApplication {
    public static void main(String[] args) {
        SpringApplication.run(CryptApplication.class, args);
    }
}
