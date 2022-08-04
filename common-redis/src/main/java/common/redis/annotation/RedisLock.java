package common.redis.annotation;

import common.redis.constants.CommonCacheConstants;
import common.redis.constants.enums.LockLevel;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

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
