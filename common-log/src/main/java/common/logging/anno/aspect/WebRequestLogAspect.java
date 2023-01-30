package common.logging.anno.aspect;

import common.logging.anno.vo.LogVO;
import common.logging.common.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author zack <br>
 * @create 2021-06-02 12:32 <br>
 * @project custom-test <br>
 * @see LogServiceAspect
 */
@Slf4j
@Aspect
@Component
@Deprecated
public class WebRequestLogAspect {
    private ThreadLocal<LogVO> tlocal = new ThreadLocal<LogVO>();

    @Pointcut("execution(* com.xxx.xxx.controller..*.*(..))")
    public void webRequestLog() {}

    @Before("webRequestLog()")
    public void doBefore(JoinPoint joinPoint) {
        try {
            LogVO logVO = LogUtil.doBefore(joinPoint);
            tlocal.set(logVO);
        } catch (Exception e) {
            log.error("***Operation request logging failed  doBefore()***", e);
        }
    }

    @AfterReturning(returning = "result", pointcut = "webRequestLog()")
    public void doAfterReturning(Object result) {
        try {
            LogVO optLog = tlocal.get();
            LogUtil.doAfterReturning(optLog, result);
            tlocal.remove();
        } catch (Exception e) {
            log.error("***Operation request logging failed doAfterReturning()***", e);
        }
    }
}
