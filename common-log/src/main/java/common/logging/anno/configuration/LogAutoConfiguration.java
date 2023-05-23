package common.logging.anno.configuration;

import common.core.filter.RepeatReadHttpServletFilter;
import common.logging.anno.aspect.LogAnnoAspect;
import common.logging.anno.aspect.LogAnnoV2Aspect;
import common.logging.anno.aspect.LogServiceAspect;
import common.logging.anno.aspect.WebRequestLogAspect;
import common.logging.anno.event.SysLogListener;
import common.logging.anno.filter.LogHttpServletFilter;
import common.logging.request.HttpRequestTraceFilter;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zack <br>
 * @create 2021-06-02 12:56 <br>
 * @project custom-test <br>
 */
@Configuration
@AllArgsConstructor
@ConditionalOnWebApplication
public class LogAutoConfiguration {

    /**
     * default enable repeated read input stream.
     *
     * @see RepeatReadHttpServletFilter
     * @return
     */
    @Bean
    public LogHttpServletFilter filter() {
        return new LogHttpServletFilter();
    }

    @Bean
    public LogAnnoV2Aspect openApiLogAspect() {
        return new LogAnnoV2Aspect();
    }

    @Bean
    public SysLogListener sysLogListener() {
        return new SysLogListener();
    }

    // @Bean
    public WebRequestLogAspect webRequestLogAspect() {
        return new WebRequestLogAspect();
    }

    // @Bean
    public LogServiceAspect logServiceAspect() {
        return new LogServiceAspect();
    }

    @Bean
    public LogAnnoAspect sysLogAspect() {
        return new LogAnnoAspect();
    }

    // @Bean
    public HttpRequestTraceFilter requestLoggingFilter() {
        HttpRequestTraceFilter filter = new HttpRequestTraceFilter();
        filter.setIncludeHeaders(true);
        filter.setIncludeClientInfo(true);
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        return filter;
    }
}
