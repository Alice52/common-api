package common.http.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author asd <br>
 * @create 2021-12-07 4:27 PM <br>
 * @project project-cloud-custom <br>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private Long expiresIn;

    private String scope;
}
