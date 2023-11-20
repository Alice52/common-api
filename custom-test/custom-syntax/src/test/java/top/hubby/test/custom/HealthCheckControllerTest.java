package top.hubby.test.custom;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.alice52.test.BootTestContext;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * @author alice52
 * @date 2023/11/20
 * @project custom-syntax
 */
public class HealthCheckControllerTest extends BootTestContext {

    @MockBean private StringRedisTemplate redisTemplate;

    @Test
    public void testHealthCheck_ok() throws Exception {

        RequestBuilder request =
                MockMvcRequestBuilders.get("/actuator/health").accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.appStatus").value("UP"))
                .andExpect(jsonPath("$.kvStatus").value("UP"))
                .andReturn();
    }

    @Test
    public void testHealthCheck_fail() throws Exception {

        Mockito.doThrow(new RuntimeException("mock kv exception"))
                .when(redisTemplate)
                .execute(ArgumentMatchers.any(RedisCallback.class));

        RequestBuilder request =
                MockMvcRequestBuilders.get("/actuator/health").accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.appStatus").value("UP"))
                .andExpect(jsonPath("$.kvStatus").value("DOWN"));
    }
}
