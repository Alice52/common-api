package common.core.util.tpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author asd <br>
 * @create 2022-08-25 10:41 AM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
public class RadicalBlockingQueue<E> extends LinkedBlockingQueue<E> {

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
