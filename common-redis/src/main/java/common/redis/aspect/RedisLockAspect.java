package common.redis.aspect;

import java.util.Optional;

import javax.annotation.Resource;

import cn.hutool.core.util.StrUtil;
import common.core.constant.enums.BusinessResponseEnum;
import common.core.util.UserUtil;
import common.redis.annotation.RedisLock;
import common.redis.constants.CommonCacheConstants;
import common.redis.constants.enums.LockLevel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

/**
 * @author zack <br>
 * @create 2021-06-03 13:18 <br>
 * @project custom-test <br>
 */
@Aspect
@Slf4j
public class RedisLockAspect {

    @Resource private RedissonClient redissonClient;

    @Pointcut("@annotation(redisLock)")
    public void redisLockPointCut(RedisLock redisLock) {}

    @SneakyThrows
    @Around("redisLockPointCut(redisLock)")
    public Object around(ProceedingJoinPoint joinPoint, RedisLock redisLock) {
        String key = Optional.of(redisLock.key()).orElse(CommonCacheConstants.GLOBAL_LOCK_KEY);
        if (redisLock.level().getCode() == LockLevel.MEMBER.getCode()) {
            key =
                    StrUtil.format(
                            CommonCacheConstants.MEMBER_LOCK_KEY_PLACE_HOLDER,
                            key,
                            UserUtil.getCurrentMemberId());
        }

        Object object = null;
        // thread block and lock() method will retry to get lock in while(true)
        RLock lock = redissonClient.getLock(key);
        try {
            if (redisLock.timeOut() > 0) {
                if (!lock.tryLock(redisLock.timeOut(), redisLock.timeUnit())) {
                    BusinessResponseEnum.LOCK_ERROR.assertFail();
                }
            } else {
                lock.lock();
            }
            object = joinPoint.proceed();
        } finally {
            // IllegalMonitorStateException: attempt to unlock lock, not locked by currentId
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

        return object;
    }
}
