package top.hubby.custom.test;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author asd <br>
 * @create 2022-01-11 10:17 AM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
public class ArrayTest {

    @Test
    public void test2ArrayJson() {
        List<Object> list = Arrays.asList(1, "2", 5);
        Object[] objects = list.toArray();

        log.info("json objects: {}", JSONUtil.toJsonStr(objects));
    }
}
