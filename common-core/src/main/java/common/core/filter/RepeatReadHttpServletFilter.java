package common.core.filter;

import common.core.util.wrapper.RepeatedHttpServletRequestWrapper;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Enable repeated read input stream by default.
 *
 * @see openapi.xx.OpenApiHttpServletFilter
 * @author zack <br>
 * @create 2022-04-08 11:56 <br>
 * @project mc-platform <br>
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RepeatReadHttpServletFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain)
            throws ServletException, IOException {

        RepeatedHttpServletRequestWrapper requestWrapper =
                new RepeatedHttpServletRequestWrapper(httpServletRequest);
        filterChain.doFilter(requestWrapper, httpServletResponse);

        // write guideline here, such as @WebFilter
        /*
            if (httpServletRequest.getRequestURI().contains("/openapi")) {
                RepeatedHttpServletRequestWrapper requestWrapper =
                        new RepeatedHttpServletRequestWrapper(httpServletRequest);
                filterChain.doFilter(requestWrapper, httpServletResponse);
                return;
            }
        */
    }

    @Override
    public void destroy() {}
}
