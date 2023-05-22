package common.token.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.token.interceptor.FeignTokenInterceptor;
import common.token.manage.TokenHttpRequests;
import common.token.manage.TokenManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author T04856 <br>
 * @create 2023-05-22 2:35 PM <br>
 * @project project-cloud-custom <br>
 */
@Configuration
@ConditionalOnProperty(name = "common.token.enabled", matchIfMissing = false)
@EnableConfigurationProperties(TokenProviderProperties.class)
public class TokenAutoConfiguration {

  @Resource private TokenProviderProperties tokenProperties;

  @Bean
  public TokenHttpRequests tokenHttpRequests(
      TokenProviderProperties properties, ObjectMapper objectMapper) {
    return new TokenHttpRequests(properties, objectMapper);
  }

  @Bean
  public TokenManager tokenManager(TokenHttpRequests tokenHttpRequests) {
    return new TokenManager(tokenHttpRequests);
  }

  /**
   * usage sample: do http request by using TokenRestTemplate.
   *
   * @param tokenManager
   * @return
   */
  @Bean
  public TokenRestTemplate tokenRestTemplate(TokenManager tokenManager) {
    return new TokenRestTemplate(tokenManager);
  }

  /**
   * usage sample
   *
   * <pre>
   * @FeignClient(name = "xxx-client", url = "${xx.xx.url}", configuration = FeignTokenInterceptor.class)
   * </pre>
   *
   * @param tokenManager
   * @return
   */
  @Bean
  public FeignTokenInterceptor feignTokenInterceptor(TokenManager tokenManager) {
    return new FeignTokenInterceptor(tokenManager);
  }
}
