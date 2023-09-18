package common.uid.annotation;

import common.uid.configuration.UidConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author zack <br>
 * @create 2021-06-25<br>
 * @project project-custom <br>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({UidConfiguration.class})
public @interface EnableUID {}
