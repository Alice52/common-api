package common.core.util.tpool.reject;

import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * 1. 线程池的创建 <br>
 * 2. 激进线程的使用 <br>
 *
 * <pre>
 *   ThreadFactory f = new ThreadFactoryBuilder()
 *           .setNameFormat("threadpool-name-%d")
 *           .setPriority(4)
 *           .get();
 *
 *   ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2,5, 5
 *                          , TimeUnit.SECONDS, new ArrayBlockingQueue<>(10)
 *                          , f
 *                          , new CallerBlocksPolicy());
 * </pre>
 *
 * 3. 提交任务是需要 try..catch, 防止异常使线程退出
 *
 * <pre>
 *      try (tp.submit(xxx)) catch (Exception x) {}
 * </pre>
 *
 * @author asd <br>
 * @create 2022-08-25 09:26 AM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
public class CallerBlocksPolicy implements RejectedExecutionHandler {

    private final Long maxWait;

    /**
     * @param maxWait The maximum time to wait for a queue slot to be available, in milliseconds.
     */
    public CallerBlocksPolicy(long maxWait) {
        this.maxWait = maxWait;
    }

    /** This will block caller thread until queue has slot for new task. */
    public CallerBlocksPolicy() {
        this.maxWait = null;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        if (executor.isShutdown()) {
            throw new RejectedExecutionException("Executor has been shut down");
        }

        try {
            BlockingQueue<Runnable> queue = executor.getQueue();

            if (Objects.isNull(this.maxWait)) {
                queue.put(r);
            } else if (!queue.offer(r, this.maxWait, TimeUnit.MILLISECONDS)) {
                throw new RejectedExecutionException("Max wait time expired to queue task");
            }
            if (log.isDebugEnabled()) {
                log.debug("Task execution queued");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RejectedExecutionException("Interrupted", e);
        }
    }
}
