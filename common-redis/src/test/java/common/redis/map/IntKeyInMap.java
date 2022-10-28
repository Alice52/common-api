package common.redis.map;

import java.io.IOException;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Map 的泛型擦除: 与 Redis 使用特别需要注意
 *
 * @author zack <br>
 * @create 2021-06-14<br>
 * @project project-custom <br>
 */
public class IntKeyInMap {

    @Test
    public void testMap() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<Integer, String> map = new HashMap<>();
        map.put(1, "a");
        map.put(2, "b");

        String s = mapper.writeValueAsString(map);
        // {"1":"a","2":"b"}
        System.out.println(s);

        map = (HashMap) mapper.readValue(s, Object.class);
        assertEquals(map.get("1"), "a");
        assertNotEquals(map.get(1), "a");
    }
}
