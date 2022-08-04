package common.security.component;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.annotation.Resource;

/**
 * @author asd <br>
 * @create 2021-06-29 3:15 PM <br>
 * @project custom-upms-grpc <br>
 */
public class CustomResourceTokenService implements ResourceServerTokenServices {

    @Resource private TokenStore tokenStore;

    @Override
    public OAuth2Authentication loadAuthentication(String token)
            throws AuthenticationException, InvalidTokenException {
        OAuth2Authentication result = tokenStore.readAuthentication(token);
        if (result == null) {
            throw new InvalidTokenException("Invalid access token: " + token);
        }
        return result;
    }

    @Override
    public OAuth2AccessToken readAccessToken(String token) {
        return tokenStore.readAccessToken(token);
    }
}
