package common.core.executor;

import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zack <br>
 * @create 2022-04-08 11:56 <br>
 * @project mc-platform <br>
 */
public class FixedThreadPoolExecutor extends ThreadPoolExecutor {

    private static final Long KEEP_ALIVE_TIME_SECONDS = 120L;

    private FixedThreadPoolExecutor(
            int corePoolSize,
            int maxPoolSize,
            long keepAliveTime,
            TimeUnit unit,
            BlockingQueue<Runnable> workQueue,
            ThreadFactory threadFactory,
            RejectedExecutionHandler handler) {
        super(corePoolSize, maxPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    /**
     * Creates a new {@code FixedThreadPoolExecutor} with the given initial parameters.
     *
     * @param corePoolSize 处于空闲状态也会保留在池中的线程数
     * @param maxPoolSize 线程池中允许的最大线程数
     * @param handler 因为达到了线程界限和队列容量而在执行被阻止时使用的处理程序
     */
    public static FixedThreadPoolExecutor newFixedThreadPoolExecutor(
            int corePoolSize, int maxPoolSize, int queueSize, RejectedExecutionHandler handler) {

        return new FixedThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                KEEP_ALIVE_TIME_SECONDS,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(queueSize),
                new FixedThreadFactory(),
                handler);
    }

    /**
     * 在子线程中使用父线程的 MDC#log 上线文
     *
     * @param runnable
     * @return
     */
    private static Runnable executor(final Runnable runnable) {

        final Map<String, String> context = MDC.getCopyOfContextMap();

        return () -> {
            Map<String, String> previous = MDC.getCopyOfContextMap();
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            try {
                runnable.run();
            } finally {
                if (previous == null) {
                    MDC.clear();
                } else {
                    MDC.setContextMap(previous);
                }
            }
        };
    }

    @Override
    public void execute(Runnable command) {
        super.execute(executor(command));
    }

    static class FixedThreadFactory implements ThreadFactory {

        private final AtomicInteger threadNumber = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("fixed-thread-" + threadNumber.getAndIncrement());
            return thread;
        }
    }
}
