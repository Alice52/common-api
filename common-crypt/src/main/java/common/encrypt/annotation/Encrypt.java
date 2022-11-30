package common.encrypt.annotation;

import java.lang.annotation.*;

/**
 * @author Zack Zhang
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Encrypt {

    //    Mode mode();
    //
    //    Padding padding();
    //
    //    String key();
    //
    //    String iv();
}
