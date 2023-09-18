package top.hubby.test.custom.redis;

import common.swagger.annotation.EnableSwagger;
import common.uid.annotation.EnableUID;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zack <br>
 * @create 2022-04-08<br>
 * @project project-cloud-custom <br>
 */
@EnableUID
@MapperScan("top.hubby.**.redis.mapper")
@EnableSwagger
@SpringBootApplication
public class RedisApplication {
    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }
}
