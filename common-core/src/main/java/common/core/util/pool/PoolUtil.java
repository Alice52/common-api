package common.core.util.pool;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 1. 正常提交到容器线程池内的任务都会被执行, 但是在函数计算内由于容器会被回收(task没有执行完)<br>
 * 2. 因此需要阻塞请求, 直到所有任务都完成才会饭(请求会阻止容器回收)
 */
@Slf4j
@UtilityClass
public class PoolUtil {
    public void printStats(ThreadPoolExecutor tp) {
        log.info("=========================");
        log.info("Pool Size: {}", tp.getPoolSize());
        log.info("Active Threads: {}", tp.getActiveCount());
        log.info("Number of Tasks Completed: {}", tp.getCompletedTaskCount());
        log.info("Number of Tasks in Queue: {}", tp.getQueue().size());
        log.info("=========================");
    }

    public ScheduledFuture<?> printStatsFixedRate(ThreadPoolExecutor tp, long period) {
        return Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(() -> printStats(tp), 0, period, TimeUnit.SECONDS);
    }

    /**
     * this is easy method to wait all task finished: it will have impact to pool(shutdown the pool)
     *
     * @param poolTaskExecutor
     */
    @SneakyThrows
    public void shutdown(ThreadPoolExecutor poolTaskExecutor) {
        poolTaskExecutor.shutdown();
        poolTaskExecutor.awaitTermination(1, TimeUnit.HOURS);
    }

    /**
     * 1. 此方法实用于所有任务都先放入 queue 内(非异步提交)<br>
     * 2. 若非异步提交消费: 则需要设置合理的sleep(异步提交的最小时间间隔)
     *
     * @param poolTaskExecutor
     * @param submitInterval 异步提交的最小时间间隔
     */
    @SneakyThrows
    public void waitAllTaskFinished(ThreadPoolExecutor poolTaskExecutor, int submitInterval) {
        long count = 0, temp;
        while (
        //  ensure submit finished
        count != (temp = poolTaskExecutor.getTaskCount())
                //  ensure submitted task execute finished
                || poolTaskExecutor.getTaskCount() != poolTaskExecutor.getCompletedTaskCount()
        //  not necessary: ensure submitted task execute finished
        /*|| !poolTaskExecutor.getQueue().isEmpty()*/ ) {

            try {
                count = temp;
                log.info(
                        "task in queue: {}, acount: {}, ccount: {}",
                        poolTaskExecutor.getQueue().size(),
                        poolTaskExecutor.getTaskCount(),
                        poolTaskExecutor.getCompletedTaskCount());
                TimeUnit.SECONDS.sleep(submitInterval);
            } catch (InterruptedException e) {
                log.error("InterruptedException by sleep for wait queue");
            }
        }

        log.info(
                "task in queue: {}, acount: {}, ccount: {}",
                poolTaskExecutor.getQueue().size(),
                poolTaskExecutor.getTaskCount(),
                poolTaskExecutor.getCompletedTaskCount());
    }
}
