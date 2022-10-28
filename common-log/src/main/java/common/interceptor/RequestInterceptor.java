package common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.core.util.ee.WebUtil;
import org.slf4j.MDC;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author zack <br>
 * @create 2021-06-02 12:35 <br>
 * @project custom-test <br>
 */
@ConditionalOnProperty(
        prefix = "common.core.global.request-id",
        value = {"enabled"},
        havingValue = "true",
        matchIfMissing = true)
@Component
public class RequestInterceptor extends HandlerInterceptorAdapter implements WebMvcConfigurer {

    /** if use responseProperties, will throw exception due to responseProperties is null now. */
    @Value("${common.core.global.request-id.key:req-id}")
    private String requestIdKey;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this).order(1);
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String requestId = WebUtil.getRequestId(request, requestIdKey);
        MDC.put(requestIdKey, requestId);
        response.addHeader(requestIdKey, requestId);

        return super.preHandle(request, response, handler);
    }

    /**
     * link:
     * https://stackoverflow.com/questions/48823794/spring-interceptor-doesnt-add-header-to-restcontroller-services
     *
     * <p>This addHeader will not work due to annotation of @RestController. <br>
     * But it can work by using @Controller<br>
     *
     * <p>HandlerInterceptorAdapters can not working with @ResponseBody and ResponseEntity methods,
     * <br>
     * because those are handled by HttpMessageConverter which writes to response <br>
     * before postHandle is called which makes it difficult to change the response.<br>
     *
     * <p>@RestController can use ResponseBodyAdvice to make addHeader worked.
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     */
    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex) {
        MDC.remove(requestIdKey);
    }
}
