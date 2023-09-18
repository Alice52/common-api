package common.redis.aspect;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import common.core.constant.enums.CommonResponseEnum;
import common.core.exception.BaseException;
import common.redis.annotation.RedisLimitRequest;
import common.redis.constants.enums.RedisKeyCommonEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

import static common.redis.utils.RedisKeyUtil.buildKey;

/**
 * @author zack <br>
 * @create 2021-04-12 14:47 <br>
 * @project common-core <br>
 */
@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class LimitRequestAspect {
    /** this will be autowire */
    @Resource private RedisScript<Long> limitRedisScript;

    @Resource private StringRedisTemplate stringRedisTemplate;

    @Pointcut("@annotation(redisLimitRequest)")
    public void pointCut(RedisLimitRequest redisLimitRequest) {}

    /**
     * full class name and method name will become redis limit key.
     *
     * @param point
     * @param redisLimitRequest
     * @return
     * @throws Throwable
     */
    @Around("pointCut(redisLimitRequest)")
    public Object doPoint(ProceedingJoinPoint point, RedisLimitRequest redisLimitRequest)
            throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        String key = method.getDeclaringClass().getName() + StrUtil.DOT + method.getName();

        if (shouldLimited(key, redisLimitRequest)) {
            throw new BaseException(CommonResponseEnum.REQUEST_LIMIT_ERROR);
        }

        return point.proceed();
    }

    private boolean shouldLimited(String key, RedisLimitRequest redisLimitRequest) {

        int count = redisLimitRequest.count();
        long time = redisLimitRequest.time();
        TimeUnit timeUnit = redisLimitRequest.timeUnit();

        if (time <= 0) {
            return false;
        }

        key = buildKey(RedisKeyCommonEnum.CACHE_LIMIT) + StrUtil.COLON + key;

        // 统一使用单位毫秒, 当前时间毫秒数
        long ttl = timeUnit.toMillis(time);
        long now = Instant.now().toEpochMilli();

        Long executeTimes =
                stringRedisTemplate.execute(
                        limitRedisScript,
                        Lists.newArrayList(key),
                        String.valueOf(now),
                        String.valueOf(ttl),
                        String.valueOf(now - ttl),
                        String.valueOf(count));
        if (executeTimes != null && executeTimes == 0) {
            log.debug("【{}】在单位时间 {} 毫秒内已达到访问上限，当前接口上限 {}", key, ttl, count);
            return true;
        }

        log.debug("【{}】在单位时间 {} 毫秒内访问 {} 次", key, ttl, executeTimes);
        return false;
    }
}
