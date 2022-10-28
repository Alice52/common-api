package common.core.util.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

/**
 * 1.单元测试类继承此类；<br>
 * 2.重写concurrentCode方法，定义自己的并发代码；<br>
 * 3.重写encapsulatingData方法，并在concurrentCode方法中调用，定义数据的拼装；<br>
 * 4.重写blockingMainThread方法，定义主线程阻塞策略<br>
 */
@Slf4j
public abstract class AbstractConcurrentTest {

    /** 默认并发线程数为2000个，子类可重写 */
    private static final int DEFAULT_CONCURRENT_CONTROL = 2000;

    private CountDownLatch blockLatch;
    /** 并发线程数量，默认2000 */
    private int concurrentThreadNum;

    private ExecutorService executorService;

    public AbstractConcurrentTest() {
        this(DEFAULT_CONCURRENT_CONTROL);
    }

    public AbstractConcurrentTest(int concurrentThreadNum) {
        this.concurrentThreadNum = concurrentThreadNum;
        blockLatch = new CountDownLatch(concurrentThreadNum);
        executorService = Executors.newCachedThreadPool();
    }

    /** 并发执行线程 */
    public final void process() {
        for (int i = 0; i < concurrentThreadNum; i++) {
            executorService.submit(
                    () -> {
                        try {
                            blockLatch.await();
                            concurrentCode();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
            blockLatch.countDown();
        }

        blockingMainThread();
    }

    /**
     * 并发代码 @Title: concurrentCode
     *
     * @date 2018年12月26日 下午2:05:25
     * @author yz
     */
    protected abstract void concurrentCode();

    /**
     * 阻塞主线程, 防止JVM关闭
     *
     * <pre>
     *  1. 不建议使用Xxx.class.wait，可以使用TimeUnit.SECONDS.sleep(200);
     * </pre>
     */
    protected void blockingMainThread() {
        if (this.executorService == null) {
            return;
        }
        this.executorService.shutdown();
        while (!this.executorService.isTerminated()) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.info("线程池关闭");
    }
}
