package top.hubby.openapi.configuration;

import common.core.filter.RepeatReadHttpServletFilter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.hubby.openapi.aspect.OpenApiSignatureAspect;
import top.hubby.openapi.configuration.properties.OpenApiProperties;
import top.hubby.openapi.filter.OpenApiHttpServletFilter;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zack <br>
 * @create 2021-06-02 12:56 <br>
 * @project custom-test <br>
 */
@Slf4j
@AllArgsConstructor
@ConditionalOnWebApplication
@Configuration
@EnableAutoConfiguration
@ConditionalOnProperty(name = "common.openapi.enabled", matchIfMissing = true)
@EnableConfigurationProperties(OpenApiProperties.class)
public class OpenApiConfiguration {

    public static Map<String, String> APP_MAP = new HashMap<>();
    @Resource private OpenApiProperties properties;

    @PostConstruct
    public void init() {
        properties
                .getThirdParty()
                .forEach(x -> APP_MAP.putIfAbsent(x.getClientId(), x.getClientSecret()));
    }

    @Bean
    public OpenApiSignatureAspect openApiSignatureAspect() {
        return new OpenApiSignatureAspect();
    }

    /**
     * default enable repeated read input stream.
     *
     * @see RepeatReadHttpServletFilter
     * @return
     */
    // @Bean
    public OpenApiHttpServletFilter filter() {
        return new OpenApiHttpServletFilter();
    }
}
