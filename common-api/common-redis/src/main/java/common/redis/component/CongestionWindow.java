package common.redis.component;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author zack <br>
 * @create 2022-05-01 14:56 <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
public class CongestionWindow {

    private static final int QPS = 1000;
    private static final TimeUnit UNIT = TimeUnit.SECONDS;
    private static final PriorityQueue<Long> QUEUE = new PriorityQueue<>(QPS);

    public boolean check() {
        long now = System.currentTimeMillis();
        long startTime = Optional.ofNullable(QUEUE.peek()).orElseGet(() -> now);
        ;
        // 窗口未满
        if (startTime + UNIT.toMillis(1) > now) {
            // 判断是否达到阈值
            if (QUEUE.size() >= QPS) {
                return false;
            }

            QUEUE.offer(now);
            return true;
        }

        QUEUE.poll();
        return check();
    }
}
