package common.token.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author T04856 <br>
 * @create 2023-05-22 2:36 PM <br>
 * @project project-cloud-custom <br>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "common.token.provider")
public class TokenProviderProperties {

    private String authenticationUrl;

    private String keyName = "accessKeyId";
    private String keyValue;

    private String secretName = "accessKeySecret";
    private String secretValue;
}
