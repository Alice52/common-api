package common.token.interceptor;

import common.token.manage.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

import static common.token.constant.Constants.AUTHENTICATION_HEADER_NAME;
import static common.token.constant.Constants.accessToken;

/**
 * @author T04856 <br>
 * @create 2023-05-22 4:08 PM <br>
 * @project project-cloud-custom <br>
 */
@RequiredArgsConstructor
public class RestTemplateTokenInterceptor implements ClientHttpRequestInterceptor {

  private final TokenManager tokenManager;

  @Override
  public ClientHttpResponse intercept(
      HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    request
        .getHeaders()
        .add(AUTHENTICATION_HEADER_NAME, accessToken(tokenManager.getAccessToken()));
    return execution.execute(request, body);
  }
}
