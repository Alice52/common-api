package common.core.filter;

import common.core.util.wrapper.RepeatedlyReadRequestWrapper;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zack <br>
 * @create 2022-04-08 11:56 <br>
 * @project mc-platform <br>
 */
public class RepeatReadHttpServletFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain)
            throws ServletException, IOException {

        RepeatedlyReadRequestWrapper requestWrapper =
                new RepeatedlyReadRequestWrapper(httpServletRequest);
        filterChain.doFilter(requestWrapper, httpServletResponse);

        // write guideline here, such as @WebFilter
        /*
            if (httpServletRequest.getRequestURI().contains("/openapi")) {
                RepeatedlyReadRequestWrapper requestWrapper =
                        new RepeatedlyReadRequestWrapper(httpServletRequest);
                filterChain.doFilter(requestWrapper, httpServletResponse);
                return;
            }
        */

        // get target method:

        // then get decorated annotations

    }

    @Override
    public void destroy() {}
}
