package top.hubby.openapi.configuration.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("common.openapi")
public class OpenApiProperties {

    private Boolean enabled;

    private List<ThirdApp> thirdParty;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ThirdApp {
        private String clientId;
        private String clientSecret;
    }
}
