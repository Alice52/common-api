package top.hubby.test.custom;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import common.core.configuration.VaultConfig;

import io.github.alice52.test.BootTestContext;

import lombok.SneakyThrows;

import org.apache.commons.logging.Log;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.logging.DeferredLogFactory;
import org.springframework.core.env.ConfigurableEnvironment;

import java.io.File;
import java.util.Collections;
import java.util.Map;

import javax.annotation.Resource;

/**
 * @date 2023/11/20
 * @project custom-syntax
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VaultConfigTest extends BootTestContext {

    @Mock private ObjectMapper objectMapper;
    @Mock private DeferredLogFactory logFactory;
    @Mock private Log logger;
    @Mock private SpringApplication application;

    @Resource private ConfigurableEnvironment environment;

    @InjectMocks private VaultConfig vaultConfig;

    @SneakyThrows
    @Test
    public void postProcessEnvironment_failAddPropertySource() {

        Mockito.doThrow(new RuntimeException("junit manual exception"))
                .when(objectMapper)
                .readValue(
                        ArgumentMatchers.any(File.class),
                        ArgumentMatchers.any(TypeReference.class));
        vaultConfig.postProcessEnvironment(environment, application);

        Mockito.verify(logger, times(1))
                .error(ArgumentMatchers.any(String.class), ArgumentMatchers.any());
        assertEquals(environment.getProperty("key"), null);
    }

    @SneakyThrows
    @Test
    public void postProcessEnvironment_shouldAddPropertySource() {
        Map<String, Object> mockProps = Collections.singletonMap("key", "value");

        Mockito.when(
                        objectMapper.readValue(
                                ArgumentMatchers.any(File.class),
                                ArgumentMatchers.any(TypeReference.class)))
                .thenReturn(mockProps);
        vaultConfig.postProcessEnvironment(environment, application);

        Mockito.verify(objectMapper, times(1))
                .readValue(
                        ArgumentMatchers.any(File.class),
                        ArgumentMatchers.any(TypeReference.class));

        assertEquals(environment.getProperty("key"), "value");
    }
}
