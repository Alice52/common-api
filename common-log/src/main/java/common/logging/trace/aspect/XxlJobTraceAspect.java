package common.logging.trace.aspect;

import static common.logging.trace.TraceIdConstants.TRACE_ID_NAME;

import cn.hutool.core.util.IdUtil;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * XxlJob can be traced by thread, such as JobThread-171-1698962400040.
 *
 * @author alice52
 * @date 2023/11/3
 * @project common-api
 */
@Deprecated
@Aspect
@Component
@Slf4j
public class XxlJobTraceAspect {

    @Around("@annotation(com.xxl.job.core.handler.annotation.XxlJob)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        try {
            MDC.put(TRACE_ID_NAME, IdUtil.fastSimpleUUID());
            return point.proceed();
        } finally {
            MDC.remove(TRACE_ID_NAME);
        }
    }
}
