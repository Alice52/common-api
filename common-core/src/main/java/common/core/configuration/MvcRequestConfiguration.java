package common.core.configuration;

import common.core.filter.HttpRequestTraceFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author T04856 <br>
 * @create 2023-03-27 10:12 AM <br>
 * @project project-cloud-custom <br>
 */
@Deprecated
@Configuration
public class MvcRequestConfiguration {
    @Bean
    public HttpRequestTraceFilter requestLoggingFilter() {
        HttpRequestTraceFilter filter = new HttpRequestTraceFilter();
        filter.setIncludeHeaders(true);
        filter.setIncludeClientInfo(true);
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        return filter;
    }
}
