package common.token.annotation;

import common.token.configuration.TokenAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author T04856 <br>
 * @create 2023-05-22 2:32 PM <br>
 * @project project-cloud-custom <br>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({TokenAutoConfiguration.class})
public @interface EnableToken {}
