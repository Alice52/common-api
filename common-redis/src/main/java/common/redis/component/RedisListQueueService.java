package common.redis.component;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RedissonClient;

import org.springframework.stereotype.Component;

/**
 * @author zack <br>
 * @create 2022-05-22 20:34 <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
@Component
public class RedisListQueueService {

    private static final String QUEUE_NAME = "redis-queue";

    @Resource private RedissonClient redissonClient;

    /**
     * 发送消息到队列头部
     *
     * @param message
     */
    public void sendMessage(String message) {
        RBlockingDeque<String> blockingDeque = redissonClient.getBlockingDeque(QUEUE_NAME);

        try {
            blockingDeque.putFirst(message);
            log.info("将消息: {} 插入到队列。", message);
        } catch (InterruptedException e) {
            log.error("push message to {} failed", QUEUE_NAME, e);
        }
    }

    /** 从队列尾部阻塞读取消息, 若没有消息线程就会阻塞等待新消息插入 + 防止 CPU 空转 */
    public void onMessage() {
        RBlockingDeque<String> blockingDeque = redissonClient.getBlockingDeque(QUEUE_NAME);
        while (true) {
            try {
                String message = blockingDeque.takeLast();
                log.info("从队列 {} 中读取到消息：{}.", QUEUE_NAME, message);
            } catch (InterruptedException e) {
                log.error("consume message from {} failed", QUEUE_NAME, e);
            }
        }
    }
}
