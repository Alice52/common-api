package common.token.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author T04856 <br>
 * @create 2023-05-22 4:05 PM <br>
 * @project project-cloud-custom <br>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    public static final String AUTHENTICATION_HEADER_NAME = "Authorization";

    public static String accessToken(String accessToken) {
        return "Bearer " + accessToken;
    }
}
