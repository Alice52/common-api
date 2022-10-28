package common.redis.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

/**
 * @author zack <br>
 * @create 2021-06-04 16:21 <br>
 * @project common-redis <br>
 */
@Target({TYPE, METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RedisLimitRequest {
    /** 可以访问的次数 */
    int count() default 0;

    /** 超时时长，默认1分钟 */
    long time() default 1;

    /** 超时时间单位，默认 分钟 */
    TimeUnit timeUnit() default TimeUnit.MINUTES;

    /** 错误提示 */
    String message() default "调用频繁";
}
