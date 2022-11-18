package top.hubby.test.custom;

import common.swagger.annotation.EnableSwagger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author zack <br>
 * @create 2022-04-08<br>
 * @project project-cloud-custom <br>
 */
@EnableSwagger
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SyntaxApplication {

    public static void main(String[] args) {
        SpringApplication.run(SyntaxApplication.class, args);
    }
}
