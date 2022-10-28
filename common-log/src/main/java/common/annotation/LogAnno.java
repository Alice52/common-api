package common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zack <br>
 * @create 2021-06-02 10:33 <br>
 * @project custom-test <br>
 */
@Deprecated
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnno {

    /**
     * description
     *
     * @return
     */
    String value() default "";
}
