package common.http.annotation;

import common.http.configuration.HttpClientConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author asd <br>
 * @create 2021-12-07 4:53 PM <br>
 * @project project-cloud-custom <br>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({HttpClientConfiguration.class})
public @interface EnableHttpClient {}
