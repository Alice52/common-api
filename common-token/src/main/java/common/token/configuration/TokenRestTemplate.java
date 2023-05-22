package common.token.configuration;

import common.token.interceptor.RestTemplateTokenInterceptor;
import common.token.manage.TokenManager;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author T04856 <br>
 * @create 2023-05-22 2:38 PM <br>
 * @project project-cloud-custom <br>
 */
@RequiredArgsConstructor
public class TokenRestTemplate extends RestTemplate {

  private final TokenManager tokenManager;

  @NotNull
  @Override
  public List<ClientHttpRequestInterceptor> getInterceptors() {
    List<ClientHttpRequestInterceptor> interceptors = super.getInterceptors();
    if (CollectionUtils.isEmpty(interceptors)) {
      interceptors = new ArrayList<>();
    }

    if (interceptors.stream().noneMatch(RestTemplateTokenInterceptor.class::isInstance)) {
      interceptors.add(new RestTemplateTokenInterceptor(tokenManager));
    }
    return interceptors;
  }
}
