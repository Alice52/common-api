package common.logging.anno.aspect;

import cn.hutool.json.JSONUtil;
import common.core.util.web.WebUtil;
import common.logging.anno.LogAnnoV2;
import common.logging.anno.vo.LogVOV2;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author zack <br>
 * @create 2022-04-07 17:43 <br>
 * @project mc-platform <br>
 */
@Order(-10)
@Aspect
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class LogAnnoV2Aspect {
    private static NamedThreadLocal<LogVOV2> tl = new NamedThreadLocal<>("open-api-log");

    @Value("${common.core.global.request-id.key:req-id}")
    private String requestIdKey;

    private static void logRequest(LogVOV2 vo) {
        log.info(
                "\n【请求ID】: {}\n"
                        + "【请求URL】: {}\n"
                        + "【请求URI】: {}\n"
                        + "【请求方式】：{}\n"
                        + "【请求头】：{}\n"
                        + "【请求param】：{}\n"
                        + "【请求body】：{}\n"
                        + "【请求IP】：{}\n"
                        + "【请求类】：{}\n"
                        + "【请求方法】：{}\n"
                        + "【请求开始时间】：{}\n",
                vo.getReqId(),
                vo.getUrl(),
                vo.getUri(),
                vo.getMethod(),
                vo.getHeaders(),
                JSONUtil.toJsonStr(vo.getParams()),
                vo.getBody(),
                vo.getRemoteAddr(),
                vo.getBeanName(),
                vo.getMethodName(),
                vo.getRequestTime());
        vo.clearUnNecessaryField();
    }

    private static void logResponse(LogVOV2 vo) {
        log.info(
                "\n【请求ID】: {}\n" + "【请求结束时间】：{}\n" + "【请求耗时】：{}\n" + "【请求结果】：{}\n",
                vo.getReqId(),
                vo.getRequestEndTime(),
                vo.getRequestDuration(),
                vo.getResult());
    }

    private void doBefore(JoinPoint joinPoint) {
        try {
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();

            LogVOV2 optLog = new LogVOV2();
            optLog.setReqId(WebUtil.getRequestId(request, requestIdKey));
            optLog.setRequestTime(System.currentTimeMillis());
            optLog.setBeanName(joinPoint.getSignature().getDeclaringTypeName());
            optLog.setMethodName(joinPoint.getSignature().getName());
            optLog.setParams(WebUtil.getParams(request));
            optLog.setRemoteAddr(WebUtil.getIpAddr());
            optLog.setUri(WebUtil.getFullUri(request));
            optLog.setHeaders(WebUtil.getHeaders(request));
            optLog.setMethod(request.getMethod());
            optLog.setBody(WebUtil.getBody(request));
            optLog.setUrl(request.getRequestURL().toString());
            logRequest(optLog);
            tl.set(optLog);
        } catch (Exception e) {
            log.error("***Operation request logging failed  doBefore()***", e);
        }
    }

    private void doAfterReturning(Object result) {
        try {
            LogVOV2 vo = Optional.ofNullable(tl.get()).orElseGet(LogVOV2::new);
            Long beginTime = Optional.ofNullable(vo.getRequestTime()).orElseGet(() -> 0L);
            long endTime = System.currentTimeMillis();
            vo.setRequestEndTime(endTime);
            vo.setResult(JSONUtil.toJsonStr(result));
            vo.setRequestDuration(endTime - beginTime);
            logResponse(vo);
        } catch (Exception e) {
            log.error("***Operation response logging failed doAfterReturning()***", e);
        } finally {
            tl.remove();
        }
    }

    /**
     * 切入去点拦截
     *
     * @see LogAnnoV2
     */
    @Pointcut("@annotation(openApiLog)")
    public void pointCut(LogAnnoV2 openApiLog) {}

    @Around("pointCut(openApiLog)")
    public Object doPoint(ProceedingJoinPoint point, LogAnnoV2 openApiLog) throws Throwable {

        doBefore(point);
        Object res = null;
        try {
            res = point.proceed();
        } catch (Throwable throwable) {
            throw throwable;
        } finally {
            doAfterReturning(res);
        }

        return res;
    }
}
