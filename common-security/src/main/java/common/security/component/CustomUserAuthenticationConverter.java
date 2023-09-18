package common.security.component;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;

/**
 * @author asd <br>
 * @create 2021-06-29 5:23 PM <br>
 * @project custom-upms-grpc <br>
 */
@Deprecated
public class CustomUserAuthenticationConverter implements UserAuthenticationConverter {
    @Override
    public Map<String, ?> convertUserAuthentication(Authentication userAuthentication) {
        return null;
    }

    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        return null;
    }
}
