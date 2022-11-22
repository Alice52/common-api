package common.swagger.annotation;

import common.swagger.configuration.SwaggerAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

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
