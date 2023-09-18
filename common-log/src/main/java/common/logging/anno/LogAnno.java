package common.logging.anno;

import common.logging.anno.aspect.LogAnnoAspect;

import java.lang.annotation.*;

/**
 * @see LogAnnoV2
 * @author zack <br>
 * @create 2021-06-02 10:33 <br>
 * @project custom-test <br>
 * @see LogAnnoAspect
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
