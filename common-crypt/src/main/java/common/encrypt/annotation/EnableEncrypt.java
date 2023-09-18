package common.encrypt.annotation;

import common.encrypt.configuration.EncryptConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Zack Zhang
 */
@Import({EncryptConfig.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableEncrypt {}
