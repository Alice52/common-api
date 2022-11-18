package top.hubby.test.custom.redis.consumer;

import common.redis.annotation.RedisConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static top.hubby.test.custom.redis.constants.RedisStreamConstants.*;

/**
 * @author zack <br>
 * @create 2022-05-23 15:10 <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
// @Component
// @Scope(BeanDefinition.SCOPE_PROTOTYPE) //如果RedisStreamConsumer.consumerNum >
// 1。需要设置scope，不然就只会产生一个消费者，无法生成多个实例
@RedisConsumer(
        streamKey = TESTING_STREAM_KEY,
        consumerGroup = TESTING_STREAM_CONSUMER_GROUP,
        consumerName = TESTING_STREAM_CONSUMER_GROUP_CONSUMER_NAME)
public class TestConsumer implements StreamListener<String, MapRecord<String, String, String>> {

    @Autowired private RedisTemplate redisTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onMessage(MapRecord<String, String, String> message) {
        log.warn("Start--TESTING-STREAM-CONSUMER");
        RecordId recordId = message.getId();
        Map<String, String> body = message.getValue();
        log.info("receive msg: {}", body);
        redisTemplate
                .opsForStream()
                .acknowledge(
                        TESTING_STREAM_KEY,
                        TESTING_STREAM_CONSUMER_GROUP,
                        TESTING_STREAM_CONSUMER_GROUP_CONSUMER_NAME);

        log.warn("End--TESTING-STREAM-CONSUMER, ConsumeMessage: [{}]", body);

        CompletableFuture.runAsync(
                        () -> redisTemplate.opsForStream().delete(TESTING_STREAM_KEY, recordId))
                .exceptionally(
                        ex -> {
                            log.error(
                                    "delete msg[{}] from stream[{}] failed.",
                                    recordId,
                                    TESTING_STREAM_KEY,
                                    ex);
                            return null;
                        });
    }
}
