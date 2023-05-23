package common.core.test.annotation;

import common.core.annotation.EnumValue;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author T04856 <br>
 * @create 2023-03-27 10:00 AM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
public class EnumValueTests {

    @Test
    public void testBiTuple() {
        // validate request
        // ....
    }

    private enum Status3 {
        A,
        B,
        C;
    }

    private class Request {
        @EnumValue(
                strValues = {"UNSHIPPED", "SHIPPED", ""},
                message = "xxx")
        private String status1;

        @EnumValue(
                intValues = {1, 2},
                message = "xxx")
        private String status2;

        @EnumValue(values = Status3.class, message = "xxx")
        private Status3 status3;
    }
}
