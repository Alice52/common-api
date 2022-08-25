package common.core.test;

import common.core.util.function.BiTuple;
import common.core.util.function.ThirdTuple;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author asd <br>
 * @create 2021-12-03 3:54 PM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
public class BiTupleTests {

    @Test
    public void testBiTuple() {
        // BiTuple<Object, String> biTuple = new BiTuple<>(new Object(), "132");
        BiTuple<Object, String> biTuple =
                BiTuple.<Object, String>builder().f(new Object()).s("132").build();
        Object k = biTuple.getF();
        String v = biTuple.getS();
        log.info("{}", biTuple);
    }

    @Test
    public void testThirdTuple() {
        ThirdTuple<Object, String, Integer> tuple =
                ThirdTuple.<Object, String, Integer>builder()
                        .f(new Object())
                        .s("132")
                        .t(12)
                        .build();
        log.info("{}", tuple);
    }
}
