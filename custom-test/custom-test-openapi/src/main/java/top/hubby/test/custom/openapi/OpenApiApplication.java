package top.hubby.test.custom.openapi;

import common.swagger.annotation.EnableSwagger;
import common.uid.annotation.EnableUID;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import top.hubby.openapi.annotation.EnableOpenApi;

/**
 * @author zack <br>
 * @create 2022-04-08 19:59 <br>
 * @project project-cloud-custom <br>
 */
@EnableUID
@EnableOpenApi
@EnableSwagger
@SpringBootApplication
public class OpenApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(OpenApiApplication.class, args);
    }
}
