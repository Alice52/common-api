package common.core.executor;

import common.core.executor.factory.ExecutorThreadFactory;

import java.util.concurrent.*;

/**
 * 固定队列大小的执行器
 *
 * @author zack <br>
 * @create 2022-04-08 11:56 <br>
 * @project mc-platform <br>
 */
public class FixedThreadPoolExecutor extends AbstractThreadPoolExecutor {

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
                new ExecutorThreadFactory(ExecutorThreadFactory.ExecutorEnum.Fixed),
                handler);
    }
}
