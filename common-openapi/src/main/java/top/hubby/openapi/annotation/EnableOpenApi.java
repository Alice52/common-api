package top.hubby.openapi.annotation;

import org.springframework.context.annotation.Import;
import top.hubby.openapi.configuration.OpenApiConfiguration;

import java.lang.annotation.*;

/**
 * @author zack <br>
 * @create 2022-04-08 19:41 <br>
 * @project project-cloud-custom <br>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({OpenApiConfiguration.class})
public @interface EnableOpenApi {}
