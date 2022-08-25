package common.core.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Method;
import common.core.annotation.LocalIdempotentRequest;
import common.core.util.ee.WebUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
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
 * @create 2021-04-12 17:47 <br>
 * @project common-core <br>
 */
@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class IdempotentRequestAspect {

    /** <uri, <token, count>> */
    private static final Map<String, ExpiringMap<String, Integer>> map =
            new ConcurrentHashMap<>(16);

    /**
     * 切入去点拦截
     *
     * @see LocalIdempotentRequest
     */
    @Pointcut("@annotation(localIdempotentRequest)")
    public void pointCut(LocalIdempotentRequest localIdempotentRequest) {}

    @Around("pointCut(localIdempotentRequest)")
    public Object doPoint(ProceedingJoinPoint point, LocalIdempotentRequest localIdempotentRequest)
            throws Throwable {

        HttpServletRequest request = WebUtil.getCurrentRequest();
        String token = WebUtil.getCurrentToken();
        // if token is null, will not do any limit.
        if (Method.GET.name().equalsIgnoreCase(request.getMethod()) || StrUtil.isEmpty(token)) {
            return point.proceed();
        }

        String md5 = WebUtil.deDupParamMD5(request, localIdempotentRequest.ignoreParams());
        ExpiringMap<String, Integer> em =
                map.getOrDefault(
                        request.getRequestURI(),
                        ExpiringMap.builder().variableExpiration().build());
        Integer count = em.getOrDefault(md5, 0);

        // 超过次数，不执行目标方法
        // 可以直接返回, 也可以抛异常
        if (count >= 1) {
            log.error(
                    "接口请求太过频繁, PATH: {}, IP: {}",
                    request.getRequestURI(),
                    WebUtil.getIpAddr(request));
            return null;
        }

        em.put(
                md5,
                1,
                ExpirationPolicy.CREATED,
                localIdempotentRequest.time(),
                localIdempotentRequest.timeUnit());
        map.put(request.getRequestURI(), em);

        return point.proceed();
    }

    /**
     * Deprecated Version
     *
     * @param point
     * @param localIdempotentRequest
     * @return
     * @throws Throwable
     */
    @Deprecated
    public Object doPointV0(
            ProceedingJoinPoint point, LocalIdempotentRequest localIdempotentRequest)
            throws Throwable {

        HttpServletRequest request = WebUtil.getCurrentRequest();
        String token = WebUtil.getCurrentToken();
        // if token is null, will not do any limit.
        if (StrUtil.isEmpty(token)) {
            return point.proceed();
        }

        ExpiringMap<String, Integer> em =
                map.getOrDefault(
                        request.getRequestURI(),
                        ExpiringMap.builder().variableExpiration().build());
        Integer count = em.getOrDefault(token, 0);

        // 超过次数，不执行目标方法
        // 可以直接返回, 也可以抛异常
        if (count >= 1) {
            return null;
        }

        em.put(
                token,
                1,
                ExpirationPolicy.CREATED,
                localIdempotentRequest.time(),
                localIdempotentRequest.timeUnit());
        map.put(request.getRequestURI(), em);

        return point.proceed();
    }
}
