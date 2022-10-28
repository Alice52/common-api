package common.redis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import common.redis.constants.CommonCacheConstants;
import common.redis.constants.enums.LockLevel;

/**
 * @author zack <br>
 * @create 2021-06-03 13:14 <br>
 * @project custom-test <br>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RedisLock {

    String key() default CommonCacheConstants.GLOBAL_LOCK_KEY;

    long timeOut() default 0;

    TimeUnit timeUnit() default TimeUnit.MICROSECONDS;

    /**
     * The lock level, default value is GLOBAL.
     *
     * @return
     * @see LockLevel
     */
    LockLevel level() default LockLevel.GLOBAL;
}
