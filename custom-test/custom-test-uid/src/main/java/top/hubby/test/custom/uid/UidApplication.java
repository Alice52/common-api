package top.hubby.test.custom.uid;

import common.uid.annotation.EnableUID;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zack <br>
 * @create 2022-04-08<br>
 * @project project-cloud-custom <br>
 */
@EnableUID
@SpringBootApplication
public class UidApplication {
    public static void main(String[] args) {
        SpringApplication.run(UidApplication.class, args);
    }
}
