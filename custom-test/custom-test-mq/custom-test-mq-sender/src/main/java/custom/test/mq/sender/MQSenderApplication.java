package custom.test.mq.sender;

import common.redis.config.ExcludeRedisConfig;
import common.swagger.annotation.EnableSwagger;
import common.uid.annotation.EnableUID;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import top.hubby.mq.sender.annotation.EnableMQSender;

/**
 * @author zack <br>
 * @create 2022-04-11 20:45 <br>
 * @project project-cloud-custom <br>
 */
@EnableUID
@EnableMQSender
@EnableSwagger
@Import(ExcludeRedisConfig.class)
@SpringBootApplication
public class MQSenderApplication {
    public static void main(String[] args) {
        SpringApplication.run(MQSenderApplication.class, args);
    }
}
