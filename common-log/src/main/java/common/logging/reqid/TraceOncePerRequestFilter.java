package common.logging.reqid;

import common.logging.trace.TraceIdConstants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @author asd <br>
 * @create 2021-12-07 3:54 PM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
@Component
@Deprecated
public class TraceOncePerRequestFilter extends OncePerRequestFilter {

    /** 日志跟踪标识 */
    private static final String TRACE_ID = TraceIdConstants.TRACE_ID_NAME;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        MDC.put(TRACE_ID, UUID.randomUUID().toString());
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        MDC.clear();
    }
}
