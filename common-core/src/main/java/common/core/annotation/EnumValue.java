package common.core.annotation;

import common.core.annotation.discriptor.EnumValueDescriptor;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {EnumValueDescriptor.class})
public @interface EnumValue {

    /**
     * error msg.
     *
     * @return
     */
    String message() default "";

    /**
     * Enum valuse.
     *
     * @return
     */
    Class<? extends Enum> values() default Enum.class;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Deprecated
    String[] strValues() default {};

    @Deprecated
    int[] intValues() default {};

    @Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        EnumValue[] value();
    }
}
