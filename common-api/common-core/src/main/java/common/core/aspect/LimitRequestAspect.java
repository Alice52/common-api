package common.core.aspect;

import cn.hutool.core.util.NumberUtil;
import com.google.common.util.concurrent.RateLimiter;
import common.core.annotation.LocalLimitRequest;
import common.core.constant.enums.CommonResponseEnum;
import common.core.exception.BaseException;
import common.core.util.WebUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    /** 根据请求地址保存不同的令牌桶 */
    private static final Map<String, RateLimiter> map = new ConcurrentHashMap<>(16);

    /**
     * 切入去点拦截
     *
     * @see LocalLimitRequest
     */
    @Pointcut("@annotation(localLimitRequest)")
    public void pointCut(LocalLimitRequest localLimitRequest) {}

    @Around("pointCut(localLimitRequest)")
    public Object doPoint(ProceedingJoinPoint joinPoint, LocalLimitRequest localLimitRequest)
            throws Throwable {

        int count = localLimitRequest.count();
        long time = localLimitRequest.time();

        if (count == 0 || time == 0) {
            return joinPoint.proceed();
        }

        // 获取 request
        HttpServletRequest request = WebUtil.getCurrentRequest();
        // 获取请求 uri
        String uri = request.getRequestURI();
        if (!map.containsKey(uri)) {
            // 为当前请求创建令牌桶
            double per = NumberUtil.div(count, localLimitRequest.timeUnit().toSeconds(time));
            map.put(uri, RateLimiter.create(per));
        }
        // 根据请求 uri 获取令牌桶
        RateLimiter rateLimiter = map.get(uri);
        if (localLimitRequest.acquireTokenTimeout() <= 0) {
            double acquire = rateLimiter.acquire();
            return joinPoint.proceed();
        }

        boolean acquire =
                rateLimiter.tryAcquire(
                        localLimitRequest.acquireTokenTimeout(),
                        localLimitRequest.acquireTokenTimeUnit());
        if (acquire) {
            // 调用目标方法
            return joinPoint.proceed();
        }
        // 获取不到令牌抛出异常
        throw new BaseException(CommonResponseEnum.REQUEST_LIMIT_ERROR);
    }
}
