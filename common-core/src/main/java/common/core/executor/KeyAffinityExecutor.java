package common.core.executor;

import com.google.common.util.concurrent.ListenableFuture;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.util.concurrent.*;

/**
 * 多个线程池并行, 但是某线程池内任务按顺序执行
 *
 * <pre>
 *     1. [不推荐] 直接创建单个线程的线程池, 每个维度都是一个线程池, Map 获取对应的执行线程池 {@code javase#SeqParallelPool}
 *     2. [推荐]  直接创建单个线程的线程池
 *     <pre>
 *         - 本质上还是单个线程池的执行器: new ThreadPoolExecutor(1, 1, 0L, MILLISECONDS, queue, threadFactory);
 *         - 将统一 key 的所有任务都分配到同一执行器: hash && equals 计算 key
 *         - 多个执行器可以提高不同key的执行并行度
 *     </pre>
 * </pre>
 *
 * @see
 * @author T04856 <br>
 * @create 2023-02-28 9:19 AM <br>
 * @project system-design <br>
 */
@Slf4j
public class KeyAffinityExecutor extends AbstractThreadPoolExecutor {
    private KeyAffinityExecutor(
            int corePoolSize,
            int maxPoolSize,
            long keepAliveTime,
            TimeUnit unit,
            BlockingQueue<Runnable> workQueue,
            ThreadFactory threadFactory,
            RejectedExecutionHandler handler) {
        super(corePoolSize, maxPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    public static <K> KeyAffinityExecutor newSerializingExecutor(
            int parallelism, int queueBufferSize, String threadName) {

        return null;
    }

    public static void main(String[] args) {

        KeyAffinityExecutor executorService =
                KeyAffinityExecutor.newSerializingExecutor(1, 200, "MY-POOL-%d");
        executorService.executeEx("key", () -> log.info("xx"));
    }

    public <T, K> ListenableFuture<T> submit(K key, @Nonnull Callable<T> task) {
        return null;
    }

    public <K> void executeEx(K key, Runnable task) {}
}
