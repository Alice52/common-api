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
 * @create 2021-06-21 13:12 <br>
 * @project swagger-3 <br>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({SwaggerAutoConfiguration.class})
@Deprecated
public @interface EnableSwagger {}
