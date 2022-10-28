package common.http.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import common.http.configuration.HttpClientConfiguration;

import org.springframework.context.annotation.Import;

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
