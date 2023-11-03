package common.logging.reqid;

import common.logging.trace.TraceIdConstants;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.MDC;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;

/**
 * @author asd <br>
 * @create 2021-12-07 3:51 PM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
@Deprecated
@WebFilter(filterName = "traceIdFilter", urlPatterns = "/*")
public class TraceFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(
            ServletRequest request, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        MDC.put(TraceIdConstants.TRACE_ID_NAME, UUID.randomUUID().toString());
        filterChain.doFilter(request, servletResponse);
    }

    @Override
    public void destroy() {
        MDC.clear();
    }
}
