package common.logging.anno.aspect;

import common.core.component.AppContextHolder;
import common.core.constant.CommonConstants;
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
 * This jus show the usage of aspect, and will not real use.
 *
 * @author zack <br>
 * @create 2021-06-04 16:55 <br>
 * @project custom-test <br>
 * @see WebRequestLogAspect
 */
@Deprecated
@Aspect
@Component
@Slf4j
public class LogServiceAspect {
    private static final String ASPECT_PREFIX = "service:";

    @Pointcut(
            "execution (* cn.ed--ail.*.*(..)) "
                    + "|| execution (* cn.e--ll.service.*.*(..))"
                    + "|| execution (* cn.ed-ponent.*.*(..))")
    public void requestLogAspect() {}

    @Before("requestLogAspect()")
    public void validateBefore(JoinPoint joinPoint) {

        try {
            if (log.isDebugEnabled()) {
                LogVO logVO = LogUtil.doBefore(joinPoint);
                AppContextHolder.upsertByKey(ASPECT_PREFIX, CommonConstants.APP_CONTEXT_LOG, logVO);
            }
        } catch (Exception e) {
            log.error("***Operation request logging failed  doBefore()***", e);
        }
    }

    @AfterReturning(returning = "result", pointcut = "requestLogAspect()")
    public void doAfterReturning(Object result) {
        try {
            if (log.isDebugEnabled()) {
                LogVO optLogVO =
                        AppContextHolder.getByKey(ASPECT_PREFIX, CommonConstants.APP_CONTEXT_LOG);
                LogUtil.doAfterReturning(optLogVO, result);
            }
        } catch (Exception e) {
            log.error("***Operation service logging failed doAfterReturning()***", e);
        } finally {
            AppContextHolder.removeByKey(ASPECT_PREFIX, CommonConstants.APP_CONTEXT_LOG);
        }
    }
}
