package common.logging.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;

/**
 * should use common-log module for req-id.
 *
 * @author T04856 <br>
 * @create 2023-03-27 10:10 AM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
@Deprecated
public class HttpRequestTraceFilter extends CommonsRequestLoggingFilter {

    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        return true;
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        log.info("before request, " + message);
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        log.info("after request, " + message);
    }
}
