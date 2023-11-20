package common.core.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.logging.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.logging.DeferredLogFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.io.File;
import java.util.Map;

/**
 * @author alice52
 * @see custom-syntax#HealthCheckControllerTest
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class VaultConfig implements EnvironmentPostProcessor {

    private Log logger;
    private ObjectMapper objectMapper;

    public VaultConfig(DeferredLogFactory logFactory) {
        super();
        this.logger = logFactory.getLog(this.getClass());
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment env, SpringApplication app) {
        try {
            Map<String, Object> pros =
                    objectMapper.readValue(
                            new File("/xxx/secret.json"),
                            new TypeReference<Map<String, Object>>() {});
            logger.info(
                    "[common-core] getting secret key from json file: "
                            + String.join(",", pros.keySet()));
            env.getPropertySources().addFirst(new MapPropertySource("secret", pros));
        } catch (Exception e) {
            logger.error("[common-core] exception: ", e);
        }
    }
}
