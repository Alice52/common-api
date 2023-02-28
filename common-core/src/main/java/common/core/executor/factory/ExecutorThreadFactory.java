package common.core.executor.factory;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import static cn.hutool.core.text.CharSequenceUtil.format;

/**
 * @author T04856 <br>
 * @create 2023-02-28 8:58 AM <br>
 * @project system-design <br>
 */
@Slf4j
public class ExecutorThreadFactory implements ThreadFactory {
    private static Thread.UncaughtExceptionHandler defaultExceptionHandler =
            (t, e) -> log.error("handle exception in child thread: ", e);
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final ExecutorEnum threadPrefix;

    public ExecutorThreadFactory(ExecutorEnum threadPrefix) {
        this(threadPrefix, defaultExceptionHandler);
    }

    public ExecutorThreadFactory(
            ExecutorEnum threadPrefix, Thread.UncaughtExceptionHandler exceptionHandler) {
        this.threadPrefix = threadPrefix;
        if (Objects.nonNull(exceptionHandler)) {
            ExecutorThreadFactory.defaultExceptionHandler = exceptionHandler;
        }
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(format("{}-thread-{}", threadPrefix, threadNumber.getAndIncrement()));
        thread.setUncaughtExceptionHandler(defaultExceptionHandler);
        return thread;
    }

    @Getter
    public enum ExecutorEnum {
        Fixed,
        KeyAffinity,
        Radical,
        ;
    }
}
