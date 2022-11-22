package common.redis.component;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author zack <br>
 * @create 2022-05-01 14:55 <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
public class LeakBucketComponent {

    private static final int QPS = 1000;
    private static final TimeUnit UNIT = TimeUnit.SECONDS;
    private static long latestPassedTime = 0;

    /**
     * 这里实现不能以桶的角度思考
     *
     * @param acquireCount
     * @return
     */
    @SneakyThrows
    public boolean check(int acquireCount) {
        if (acquireCount <= 0) {
            return true;
        }

        long currentTime = System.currentTimeMillis();
        // 根据配置计算两次请求之间的最小时间: 1000 qps 则两个请求之间至少要有1/1000的间隔
        Double tmp = 1.0 * (acquireCount) / QPS * 1000;
        long constTime = tmp.longValue();
        // 计算上一次请求之后，下一次允许通过的最小时间
        long expectedTime = constTime + latestPassedTime;
        if (expectedTime <= currentTime) {
            // 如果当前时间大于计算的时间，那么可以直接放行
            latestPassedTime = currentTime;
            return true;
        }

        // 如果没有，则计算相应需要等待的时间
        long waitTime = constTime + latestPassedTime - System.currentTimeMillis();
        if (waitTime > UNIT.toMillis(1)) {
            return false;
        }
        // 在并发条件下等待时间可能会小于等于0
        if (waitTime > 0) {
            latestPassedTime = currentTime + waitTime;
            TimeUnit.MILLISECONDS.sleep(waitTime);
        }
        return true;
    }
}
