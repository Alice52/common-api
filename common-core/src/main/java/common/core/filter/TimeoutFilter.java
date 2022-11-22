package common.core.filter;

import cn.hutool.core.util.ObjectUtil;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 1. 只能处理非异步的线程<br>
 * 2. AsyncContext.html#setTimeout(long) 或者设置 spring.mvc.async.request-timeout
 *
 * @author zack <br>
 * @create 2021-06-22 13:34 <br>
 * @project swagger-3 <br>
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TimeoutFilter extends OncePerRequestFilter {

    @Value("${spring.mvc.async.request-timeout:-1}")
    private Long timeout;

    private ScheduledExecutorService timeoutsPool = Executors.newScheduledThreadPool(10);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (ObjectUtil.isNull(timeout) || new Long(-1).equals(timeout)) {
            filterChain.doFilter(request, response);
            return;
        }

        val completed = new AtomicBoolean(false);
        val requestHandlingThread = Thread.currentThread();
        val timeoutFuture =
                timeoutsPool.schedule(
                        () -> {
                            if (completed.compareAndSet(false, true)) {
                                requestHandlingThread.interrupt();
                            }
                        },
                        timeout,
                        TimeUnit.SECONDS);

        try {
            filterChain.doFilter(request, response);
            timeoutFuture.cancel(false);
        } finally {
            completed.set(true);
        }
    }
}
