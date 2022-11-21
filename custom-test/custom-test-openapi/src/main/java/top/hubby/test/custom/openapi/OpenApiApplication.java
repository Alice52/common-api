package top.hubby.test.custom.openapi;

import common.redis.config.ExcludeRedisConfig;
import common.swagger.annotation.EnableSwagger;
import common.uid.annotation.EnableUID;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;
import top.hubby.openapi.annotation.EnableOpenApi;

/**
 * @author zack <br>
 * @create 2022-04-08 19:59 <br>
 * @project project-cloud-custom <br>
 */
@EnableOpenApi
@EnableSwagger
@Import(ExcludeRedisConfig.class)
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, RabbitAutoConfiguration.class})
public class OpenApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(OpenApiApplication.class, args);
    }
}
