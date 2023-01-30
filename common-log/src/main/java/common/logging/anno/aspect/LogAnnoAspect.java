package common.logging.anno.aspect;

import common.core.component.SpringContextHolder;
import common.logging.anno.LogAnno;
import common.logging.anno.event.SysLogEvent;
import common.logging.anno.vo.LogVO;
import common.logging.common.LogUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @see LogAnnoV2Aspect
 * @author zack <br>
 * @create 2021-06-02 10:35 <br>
 * @project custom-test <br>
 */
@Aspect
@Slf4j
@Deprecated
public class LogAnnoAspect {

    /** https://www.cnblogs.com/liqbk/p/13497502.html */
    @SneakyThrows
    @Around(value = "@annotation(logAnno)")
    public Object around(ProceedingJoinPoint point, LogAnno logAnno) {
        LogVO vo = LogUtil.doBefore(point);
        Object result = point.proceed();
        LogUtil.doAfterReturning(vo, result);

        SpringContextHolder.publishEvent(new SysLogEvent(logAnno));

        return result;
    }
}
