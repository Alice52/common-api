package top.hubby.test.custom;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.github.alice52.test.BootTestContext;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * @author zack <br>
 * @create 2022-04-07 17:40 <br>
 * @project custom-syntax <br>
 */
public class PingControllerTest extends BootTestContext {

    @MockBean private JdbcTemplate jdbcTemplate;

    @Test
    public void testPing_ok() throws Exception {

        Mockito.doNothing().when(jdbcTemplate).execute("select 1");
        MockHttpServletRequestBuilder req =
                MockMvcRequestBuilders.get("/health/ping").accept(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(req)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string("{\"code\":0,\"data\":\"pong\"}"))
                .andExpect(jsonPath("$.data").value("pong"))
                .andExpect(jsonPath("$.code").value(0));
    }
}
