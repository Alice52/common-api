package top.hubby.openapi.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import top.hubby.openapi.configuration.OpenApiConfiguration;

import org.springframework.context.annotation.Import;

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
