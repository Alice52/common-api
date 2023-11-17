package io.github.alice52.test;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

/**
 * @author zack <br>
 * @create 2023/11/18 00:01 <br>
 * @project common-api <br>
 */
@ActiveProfiles("test")
@SpringBootTest(properties = {"spring.cloud.nacos.config.enabled=false"})
@RunWith(SpringRunner.class)
public class BootTestContext {

    @Resource private WebApplicationContext webApplicationContext;

    protected MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @AfterClass
    public static void end() {
        // empty
    }
}
