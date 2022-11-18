package top.hubby.test.custom.db;

import common.redis.config.ExcludeRedisConfig;
import common.swagger.annotation.EnableSwagger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * 这里不需要写 @MapperScan 是由于没有使用 @EnableUID
 *
 * @author zack <br>
 * @create 2022-04-08<br>
 * @project project-cloud-custom <br>
 */
@EnableSwagger
@Import(ExcludeRedisConfig.class)
@SpringBootApplication(exclude = {RabbitAutoConfiguration.class})
public class DBApplication {
    public static void main(String[] args) {
        SpringApplication.run(DBApplication.class, args);
    }
}
