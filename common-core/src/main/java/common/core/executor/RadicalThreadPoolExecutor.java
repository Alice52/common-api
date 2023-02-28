package common.core.executor;

import common.core.executor.factory.ExecutorThreadFactory;
import common.core.executor.reject.CallerBlocksPolicy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.*;

/**
 * 激进执行器:
 *
 * <pre>
 *      1. 优先创建线程
 *      2. 达到最大线程后, 放入队列
 *      3. 队列满则执行拒绝策略
 *   </pre>
 *
 * @author T04856 <br>
 * @create 2023-02-28 8:53 AM <br>
 * @project system-design <br>
 */
@Slf4j
public class RadicalThreadPoolExecutor extends AbstractThreadPoolExecutor {
    private static final int threadCount = 200;
    private static final int queueSize = 2000;

    private RadicalThreadPoolExecutor(
            int corePoolSize,
            int maxPoolSize,
            long keepAliveTime,
            TimeUnit unit,
            BlockingQueue<Runnable> workQueue,
            ThreadFactory threadFactory,
            RejectedExecutionHandler handler) {
        super(corePoolSize, maxPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    public static RadicalThreadPoolExecutor newRadicalThreadPoolExecutor() {

        return new RadicalThreadPoolExecutor(
                0,
                threadCount,
                30,
                TimeUnit.SECONDS,
                new RadicalBlockingQueue<>(queueSize),
                new ExecutorThreadFactory(ExecutorThreadFactory.ExecutorEnum.Radical),
                new CallerBlocksPolicy());
    }

    /**
     * @param corePoolSize 处于空闲状态也会保留在池中的线程数
     * @param maxPoolSize 线程池中允许的最大线程数
     * @param queueSize queue size
     * @return
     */
    public static RadicalThreadPoolExecutor newRadicalThreadPoolExecutor(
            int corePoolSize, int maxPoolSize, int queueSize) {

        return new RadicalThreadPoolExecutor(
                0,
                maxPoolSize,
                30,
                TimeUnit.SECONDS,
                new RadicalBlockingQueue<>(queueSize),
                new ExecutorThreadFactory(ExecutorThreadFactory.ExecutorEnum.Radical),
                new CallerBlocksPolicy());
    }

    @Bean("radicalThreadPool")
    public ThreadPoolExecutor executor() {

        return new ThreadPoolExecutor(
                0,
                threadCount,
                30,
                TimeUnit.SECONDS,
                new RadicalBlockingQueue<>(queueSize),
                new ExecutorThreadFactory(ExecutorThreadFactory.ExecutorEnum.Radical),
                new CallerBlocksPolicy());
    }
}

@Slf4j
class RadicalBlockingQueue<E> extends LinkedBlockingQueue<E> {

    public RadicalBlockingQueue(int capacity) {
        super(capacity);
    }

    @Override
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        return super.offer(e, timeout, unit);
    }

    /**
     * 必须和 CallerBlocksPolicy 结合使用, 否则就没有队列概念了, 会导致任务的丢失.
     *
     * <pre>
     *   1. 先返回 false: 造成队列满的假象, 让线程池优先扩容
     *   2. 此时显示队列已满, 则尝试添加工作线程
     *   3. 若工作线程达到最大值, 则会执行拒绝策略.
     * </pre>
     *
     * @param e
     * @return
     */
    @Override
    public boolean offer(E e) {
        return false;
    }
}
