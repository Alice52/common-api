package custom.test.logging;

import common.swagger.annotation.EnableSwagger;
import io.github.alice52.common.inject.annotation.SimpleBootApplication;
import org.springframework.boot.SpringApplication;

/**
 * @author zack <br>
 * @create 2022-04-08 19:59 <br>
 * @project project-cloud-custom <br>
 */
@EnableSwagger
@SimpleBootApplication
public class LogApplication {
    public static void main(String[] args) {
        SpringApplication.run(LogApplication.class, args);
    }
}
