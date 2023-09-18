package common.encrypt.annotation;

import common.encrypt.configuration.DecryptConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @see DecryptConfig
 * @author Zack Zhang
 */
@Import({DecryptConfig.class})
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableDecrypt {}
