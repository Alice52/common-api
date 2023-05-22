package common.token.interceptor;

import common.token.manage.TokenManager;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

import static common.token.constant.Constants.AUTHENTICATION_HEADER_NAME;
import static common.token.constant.Constants.accessToken;

/**
 * @author T04856 <br>
 * @create 2023-05-22 4:16 PM <br>
 * @project project-cloud-custom <br>
 */
@RequiredArgsConstructor
public class FeignTokenInterceptor implements RequestInterceptor {

  private final TokenManager tokenManager;

  @Override
  public void apply(RequestTemplate template) {
    template.header(AUTHENTICATION_HEADER_NAME, accessToken(tokenManager.getAccessToken()));
  }
}
