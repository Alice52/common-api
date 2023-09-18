package common.encrypt.annotation;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import common.encrypt.aspect.EncryptAspect;
import common.encrypt.constants.enums.EncryptEnum;

import java.lang.annotation.*;

/**
 * @see EncryptAspect
 * @author Zack Zhang
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Encrypt {

    @Deprecated
    Mode mode() default Mode.NONE;

    @Deprecated
    Padding padding() default Padding.NoPadding;

    @Deprecated
    String key() default "";

    @Deprecated
    String iv() default "";

    EncryptEnum encrypt() default EncryptEnum.NONE;
}
