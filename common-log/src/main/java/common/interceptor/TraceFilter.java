package common.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.UUID;

/**
 * @author asd <br>
 * @create 2021-12-07 3:51 PM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
@WebFilter(filterName = "traceIdFilter", urlPatterns = "/*")
public class TraceFilter implements Filter {

    /** 日志跟踪标识 */
    private static final String TRACE_ID = "TRACE_ID";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(
            ServletRequest request, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        MDC.put(TRACE_ID, UUID.randomUUID().toString());
        filterChain.doFilter(request, servletResponse);
    }

    @Override
    public void destroy() {
        MDC.clear();
    }
}
