package common.core.util.tpool;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author asd <br>
 * @create 2022-08-25 09:25 AM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
@UtilityClass
public class PoolMonitorUtil {

    public ScheduledFuture<?> printStatsFixedRate(ThreadPoolExecutor tp, long period) {
        return Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(() -> printStats(tp), 0, period, TimeUnit.SECONDS);
    }

    public void printStats(ThreadPoolExecutor tp) {
        log.info("=========================");
        log.info("Pool Size: {}", tp.getPoolSize());
        log.info("Active Threads: {}", tp.getActiveCount());
        log.info("Number of Tasks Completed: {}", tp.getCompletedTaskCount());
        log.info("Number of Tasks in Queue: {}", tp.getQueue().size());
        log.info("=========================");
    }
}
