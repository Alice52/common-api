package top.hubby.openapi.configuration;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import top.hubby.openapi.aspect.OpenApiLogAspect;
import top.hubby.openapi.aspect.OpenApiSignatureAspect;
import top.hubby.openapi.component.OpenApiSecretComponent;
import top.hubby.openapi.filter.OpenApiHttpServletFilter;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
public class OpenApiConfiguration {

    @Bean
    public OpenApiLogAspect openApiLogAspect() {
        return new OpenApiLogAspect();
    }

    @Bean
    public OpenApiSignatureAspect openApiSignatureAspect() {
        return new OpenApiSignatureAspect();
    }

    @Bean
    public OpenApiSecretComponent secretComponent() {
        return new OpenApiSecretComponent();
    }

    @Bean
    public OpenApiHttpServletFilter filter() {
        return new OpenApiHttpServletFilter();
    }
}
