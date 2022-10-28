package common.swagger.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import common.swagger.configuration.SwaggerAutoConfiguration;

import org.springframework.context.annotation.Import;

/**
 * @author zack <br>
 * @create 2021-06-01 16:26 <br>
 * @project common-swagger <br>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({SwaggerAutoConfiguration.class})
public @interface EnableSwagger {}
