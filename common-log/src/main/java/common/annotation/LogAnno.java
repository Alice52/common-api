package common.annotation;

import java.lang.annotation.*;

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
