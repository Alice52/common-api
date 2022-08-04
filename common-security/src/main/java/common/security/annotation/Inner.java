package common.security.annotation;

import common.security.annotation.aspect.InnerAspect;

import java.lang.annotation.*;

/**
 * Not check permissions.
 *
 * @author asd <br>
 * @create 2021-06-29 2:51 PM <br>
 * @project custom-upms-grpc <br>
 * @see InnerAspect
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Inner {

    /**
     * 是否AOP统一处理
     *
     * @return false, true
     */
    boolean value() default true;

    /**
     * 需要特殊判空的字段(预留)
     *
     * @return {}
     */
    String[] field() default {};
}
