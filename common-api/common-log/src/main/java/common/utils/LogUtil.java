package common.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import common.core.util.UserUtil;
import common.core.util.WebUtil;
import common.model.vo.LogVO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

/**
 * @author zack <br>
 * @create 2021-06-02 12:12 <br>
 * @project custom-test <br>
 */
@Slf4j
@Component
public class LogUtil {

    private static String requestIdKey;

    public static LogVO doBefore(JoinPoint joinPoint) {
        try {
            long beginTime = System.currentTimeMillis();
            // 接收到请求，记录请求内容
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            assert attributes != null;
            HttpServletRequest request = attributes.getRequest();
            String beanName = joinPoint.getSignature().getDeclaringTypeName();
            String methodName = joinPoint.getSignature().getName();
            String uri = request.getRequestURI();
            String remoteAddr = WebUtil.getIpAddr(request);
            String sessionId = request.getSession().getId();
            String username = UserUtil.getCurrentMemberId();
            String method = request.getMethod();
            StringBuilder params = new StringBuilder();
            if (Method.POST.name().equalsIgnoreCase(method)) {
                Object[] paramsArray = joinPoint.getArgs();
                params = new StringBuilder(argsArrayToString(paramsArray));
            } else {
                Map<String, ?> paramsMap = request.getParameterMap();

                if (paramsMap != null && !paramsMap.isEmpty()) {
                    Set<String> keySet = paramsMap.keySet();
                    params.append("{");
                    for (String key : keySet) {
                        String[] values = (String[]) paramsMap.get(key);
                        for (String value : values) {
                            params.append(", ").append(key).append("=").append(value);
                        }
                    }
                    params.append("}");
                }
            }

            LogVO optLog = new LogVO();
            optLog.setReqId(MDC.get(requestIdKey));
            optLog.setBeanName(beanName);
            optLog.setUser(username);
            optLog.setMethodName(methodName);
            optLog.setParams(params.toString());
            optLog.setRemoteAddr(remoteAddr);
            optLog.setSessionId(sessionId);
            optLog.setUri(uri);
            optLog.setRequestTime(beginTime);

            log.info("[enter] optLog: {}", optLog);

            return optLog;
        } catch (Exception e) {
            log.error("***Operation request logging failed  doBefore()***", e);
        }

        return null;
    }

    public static void doAfterReturning(LogVO optLog, Object result) {
        try {
            if (ObjectUtil.isEmpty(result)) {
                log.info(" response result is null");
                return;
            }
            optLog.setResult(result.toString());
            long beginTime = optLog.getRequestTime();
            long requestTime = System.currentTimeMillis() - beginTime;
            optLog.setRequestTime(requestTime);
            if (result instanceof JSON) {
                log.info(" response result : {}", JSONUtil.toJsonPrettyStr(result));
            }

            log.info("[exit] optLog: {}", optLog);
        } catch (Exception e) {
            log.error("***Operation request logging failed doAfterReturning()***", e);
        }
    }

    /**
     * Request parameter assembly
     *
     * @param paramsArray
     * @return
     */
    private static String argsArrayToString(Object[] paramsArray) {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null && paramsArray.length > 0) {
            for (Object o : paramsArray) {
                Object jsonObj = JSONUtil.toJsonStr(o);
                params.append(jsonObj.toString()).append(" ");
            }
        }
        return params.toString().trim();
    }

    @Value("${common.core.global.request-id.key:req-id}")
    public void setRequestIdKey(String requestIdKey) {
        LogUtil.requestIdKey = requestIdKey;
    }
}
