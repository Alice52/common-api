package common.core.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author zack <br>
 * @create 2021-06-03 15:23 <br>
 * @project custom-test <br>
 */
public final class UserUtil {

    /**
     * 获取当前用户的标识
     *
     * @return
     */
    public static String getCurrentMemberId() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(3));
    }
}
