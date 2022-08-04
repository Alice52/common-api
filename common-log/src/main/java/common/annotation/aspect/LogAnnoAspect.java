package common.annotation.aspect;

import common.annotation.LogAnno;
import common.core.component.SpringContextHolder;
import common.event.SysLogEvent;
import common.model.vo.LogVO;
import common.utils.LogUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author zack <br>
 * @create 2021-06-02 10:35 <br>
 * @project custom-test <br>
 */
@Deprecated
@Aspect
@Slf4j
public class LogAnnoAspect {

    /** https://www.cnblogs.com/liqbk/p/13497502.html */
    @Around(value = "@annotation(logAnno)")
    @SneakyThrows
    public Object around(ProceedingJoinPoint point, LogAnno logAnno) {
        LogVO vo = LogUtil.doBefore(point);
        Object result = point.proceed();
        LogUtil.doAfterReturning(vo, result);

        SpringContextHolder.publishEvent(new SysLogEvent(logAnno));

        return result;
    }
}
