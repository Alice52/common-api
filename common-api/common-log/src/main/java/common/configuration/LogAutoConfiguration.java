package common.configuration;

import common.annotation.aspect.LogAnnoAspect;
import common.annotation.aspect.LogServiceAspect;
import common.annotation.aspect.WebRequestLogAspect;
import common.event.SysLogListener;
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
}
