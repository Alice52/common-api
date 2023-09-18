package common.core.exception.handler;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author asd <br>
 * @create 2021-11-15 4:00 PM <br>
 * @project swagger-3 <br>
 */
@Slf4j
public final class ExceptionHandlerSupport {

    public static void printContext() {
        // 对于 Web 项目我们可以从上下文中获取到额外的一些信息来丰富我们的日志
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (ObjectUtil.isNotNull(requestAttributes)) {
            HttpServletRequest request =
                    ((ServletRequestAttributes) requestAttributes).getRequest();

            Map<String, String> headerMap = new HashMap<>();
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String value = request.getHeader(name);
                headerMap.put(name, value);
            }

            BasicUserInfo userInfo = BasicUserInfo.builder().memberId(1L).build();
            RequestContext context =
                    RequestContext.builder()
                            .userInfo(userInfo)
                            .url(request.getRequestURL().toString())
                            .parameterMap(request.getParameterMap())
                            .method(request.getMethod())
                            .headerMap(headerMap)
                            .build();

            log.error("exception context: {}", context);
        }
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class RequestContext {
    private String url;
    /** user info. */
    private BasicUserInfo userInfo;

    private Map<String, String[]> parameterMap;
    private String method;
    private Map<String, String> headerMap;
}

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
class BasicUserInfo {
    String name;
    Long memberId;
}
