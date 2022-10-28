package common.security.connfiguration;

import common.security.component.FeignClientInterceptor;
import feign.RequestInterceptor;
import lombok.AllArgsConstructor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.security.oauth2.client.AccessTokenContextRelay;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;

/**
 * Feign 拦截器传递 header 中oauth token, 使用 Hystrix 的信号量模式
 *
 * @author asd <br>
 * @create 2021-06-29 5:25 PM <br>
 * @project custom-upms-grpc <br>
 */
@Configuration
@AllArgsConstructor
@ConditionalOnProperty("security.oauth2.client.client-id")
public class CustomFeignClientConfiguration {
    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor(
            OAuth2ClientContext oAuth2ClientContext,
            OAuth2ProtectedResourceDetails resource,
            AccessTokenContextRelay accessTokenContextRelay) {
        return new FeignClientInterceptor(oAuth2ClientContext, resource, accessTokenContextRelay);
    }
}
