package top.hubby.test.custom.oss;

import common.oss.v2.annotation.EnableOssV2;
import common.swagger.annotation.EnableSwagger;
import io.github.alice52.common.inject.annotation.SimpleBootApplication;
import org.springframework.boot.SpringApplication;

/**
 * @author zack <br>
 * @create 2022-04-08<br>
 * @project project-cloud-custom <br>
 */
@EnableOssV2
@EnableSwagger
@SimpleBootApplication
public class OssApplication {

    public static void main(String[] args) {
        SpringApplication.run(OssApplication.class, args);
    }
}
