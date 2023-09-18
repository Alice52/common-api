package common.core.executor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.*;

/**
 * handle thread context of child-and-parent.
 *
 * @author T04856 <br>
 * @create 2023-02-28 9:01 AM <br>
 * @project system-design <br>
 */
@Slf4j
public abstract class AbstractThreadPoolExecutor extends ThreadPoolExecutor {

    protected static final Long KEEP_ALIVE_TIME_SECONDS = 120L;

    protected AbstractThreadPoolExecutor(
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
}
