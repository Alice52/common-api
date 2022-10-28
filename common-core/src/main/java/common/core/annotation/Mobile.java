package common.core.annotation;

/**
 * @author zack <br>
 * @create 2020-08-01 14:43 <br>
 * @project common-core <br>
 */
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import common.core.annotation.discriptor.MobileDescriptor;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Documented
@Retention(RUNTIME)
@Constraint(validatedBy = MobileDescriptor.class)
public @interface Mobile {

    boolean required() default false;

    String message() default "Invalid phone number, please check again";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
