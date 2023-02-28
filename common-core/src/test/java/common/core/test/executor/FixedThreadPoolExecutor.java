package common.core.test.executor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import common.core.executor.reject.CallerBlocksPolicy;
import common.core.util.pool.PoolMonitorUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 结论: 使用 execute + UncaughtExceptionHandler 进行任务提交
 *
 * @author T04856 <br>
 * @create 2023-02-15 3:56 PM <br>
 * @project system-design <br>
 */
@Slf4j
public class FixedThreadPoolExecutor {

    private int threadCount = 1;
    private int queueSize = 1;
    private static final Thread.UncaughtExceptionHandler exceptionHandler =
            (t, e) -> log.error(String.format("handle exception in child thread. %s", e));

    public ThreadPoolExecutor executor() {
        ThreadFactory f =
                new ThreadFactoryBuilder()
                        .setUncaughtExceptionHandler(exceptionHandler)
                        .setNameFormat("radical-thread-pool-%d")
                        .build();

        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(
                        threadCount / 10,
                        threadCount,
                        30,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(queueSize),
                        f,
                        new CallerBlocksPolicy());

        PoolMonitorUtil.printStatsFixedRate(threadPoolExecutor, 10);

        return threadPoolExecutor;
    }

    @SneakyThrows
    @Test
    public void testPoolException() {
        ThreadPoolExecutor executor = executor();

        executor.submit(
                () -> {
                    log.info("1");
                    throw new RuntimeException("zaxxs");
                });

        executor.execute(
                () -> {
                    log.info("2");
                    throw new RuntimeException("zaxxs2");
                });

        executor.execute(
                () -> {
                    log.info("3");
                    throw new RuntimeException("zaxxs3");
                });

        TimeUnit.SECONDS.sleep(50);
    }
}
