package common.redis.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 规定时间内同样的参数调用接口两次才会出现幂等问题
 *
 * @author zack <br>
 * @create 2021-06-04 16:20 <br>
 * @project common-redis <br>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Inherited
public @interface RedisIdempotentRequest {
    /**
     * 限制时间: 指定时间段内同一用户调用相同API 多次则处理为幂等<br>
     * 单位: 毫秒
     *
     * @return
     */
    long time() default 6000;

    /**
     * 请求等待时间的单位
     *
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.MICROSECONDS;

    /**
     * 计算请求MD5时忽略的参数
     *
     * @return
     */
    String[] ignoreParams() default {};
}
