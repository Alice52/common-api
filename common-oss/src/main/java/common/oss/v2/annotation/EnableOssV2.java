package common.oss.v2.annotation;

import common.oss.v2.configuration.OssAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author T04856 <br>
 * @create 2023-03-23 4:34 PM <br>
 * @project project-cloud-custom <br>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({OssAutoConfiguration.class})
public @interface EnableOssV2 {}
