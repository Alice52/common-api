package common.security.connfiguration;

import common.core.constant.SecurityConstants;
import common.security.exception.handler.AccessDeniedHandler;
import lombok.SneakyThrows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

/**
 * @author asd <br>
 * @create 2021-06-29 2:56 PM <br>
 * @project custom-upms-grpc <br>
 */
@Import(AccessDeniedHandler.class)
public class ResourceServerAutoConfiguration {
    @Autowired private RedisConnectionFactory redisConnectionFactory;

    /**
     * Feign load balancer<br>
     * // TODO: add grpc load balancer
     *
     * @return
     */
    @Bean
    @Primary
    @LoadBalanced
    public RestTemplate lbRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(
                new DefaultResponseErrorHandler() {
                    @Override
                    @SneakyThrows
                    public void handleError(ClientHttpResponse response) {
                        if (response.getRawStatusCode() != HttpStatus.BAD_REQUEST.value()) {
                            super.handleError(response);
                        }
                    }
                });
        return restTemplate;
    }

    @Bean
    public OAuth2WebSecurityExpressionHandler oAuth2WebSecurityExpressionHandler(
            ApplicationContext applicationContext) {
        OAuth2WebSecurityExpressionHandler expressionHandler =
                new OAuth2WebSecurityExpressionHandler();
        expressionHandler.setApplicationContext(applicationContext);
        return expressionHandler;
    }

    @Bean
    public TokenStore tokenStore() {
        RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
        tokenStore.setPrefix(SecurityConstants.OAUTH_PREFIX);
        return tokenStore;
    }
}
