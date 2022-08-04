package top.hubby.openapi.aspect;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import common.core.util.WebUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.hubby.openapi.annotation.OpenApiLog;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
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
public class OpenApiLogAspect {
    private static NamedThreadLocal<LogVO> tl = new NamedThreadLocal<>("open-api-log");

    private static void doBefore(JoinPoint joinPoint) {
        try {
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();

            LogVO optLog = new LogVO();
            optLog.setReqId(IdUtil.fastUUID());
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

    private static void doAfterReturning(Object result) {
        try {
            LogVO vo = Optional.ofNullable(tl.get()).orElseGet(() -> new LogVO());
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

    private static void logRequest(LogVO vo) {
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

    private static void logResponse(LogVO vo) {
        log.info(
                "\n【请求ID】: {}\n" + "【请求结束时间】：{}\n" + "【请求耗时】：{}\n" + "【请求结果】：{}\n",
                vo.getReqId(),
                vo.getRequestEndTime(),
                vo.getRequestDuration(),
                vo.getResult());
    }

    /**
     * 切入去点拦截
     *
     * @see OpenApiLog
     */
    @Pointcut("@annotation(openApiLog)")
    public void pointCut(OpenApiLog openApiLog) {}

    @Around("pointCut(openApiLog)")
    public Object doPoint(ProceedingJoinPoint point, OpenApiLog openApiLog) throws Throwable {

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

    @Data
    static class LogVO {
        private String reqId;

        private String remoteAddr;
        private String url;
        private String uri;
        private Object body;
        private String method;
        private Map<String, ?> params;
        private Map<String, Object> headers;
        private Object result = new Object();

        private String beanName;
        private String methodName;

        private Long requestTime;
        private Long requestEndTime;
        private Long requestDuration;

        public void clearUnNecessaryField() {
            this.setBeanName(null);
            this.setMethodName(null);
            this.setRemoteAddr(null);
            this.setUri(null);
            this.setHeaders(null);
            this.setBody(null);
            this.setParams(null);
        }
    }
}
