package common.security.component;

import cn.hutool.core.collection.CollectionUtil;
import common.core.configuration.properties.SecurityIgnoreProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.client.RestTemplate;

/**
 * @author asd <br>
 * @create 2021-06-29 3:03 PM <br>
 * @project custom-upms-grpc <br>
 */
@Slf4j
@Import({ResourceAuthExceptionEntryPoint.class, CustomResourceTokenService.class})
public class CustomResourceServerConfigurerAdapter extends ResourceServerConfigurerAdapter {
    @Autowired protected ResourceAuthExceptionEntryPoint resourceAuthExceptionEntryPoint;

    @Autowired protected CustomResourceTokenService customResourceTokenService;

    @Autowired private SecurityIgnoreProperties securityIgnoreProperties;

    @Autowired private AccessDeniedHandler accessDeniedHandler;

    @Autowired private RestTemplate lbRestTemplate;

    @Autowired private OAuth2WebSecurityExpressionHandler expressionHandler;

    /**
     * 默认的配置，对外暴露
     *
     * @param httpSecurity
     */
    @Override
    @SneakyThrows
    public void configure(HttpSecurity httpSecurity) {
        // 允许使用iframe 嵌套，避免swagger-ui 不被加载的问题
        httpSecurity.headers().frameOptions().disable();
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry =
                httpSecurity.authorizeRequests();

        if (CollectionUtil.isNotEmpty(securityIgnoreProperties.getIgnoreMatchers())) {
            securityIgnoreProperties
                    .getIgnoreMatchers()
                    .forEach(
                            matcher -> {
                                if (matcher.getMethod() != null) {
                                    registry.antMatchers(
                                                    matcher.getMethod(), matcher.getAntPattern())
                                            .permitAll();
                                } else {
                                    registry.antMatchers(matcher.getAntPattern()).permitAll();
                                }
                            });
        }

        registry.anyRequest().authenticated().and().csrf().disable();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources
                .expressionHandler(expressionHandler)
                .authenticationEntryPoint(resourceAuthExceptionEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .tokenServices(customResourceTokenService);
    }
}
