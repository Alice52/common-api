package common.redis.component;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RStream;
import org.redisson.api.RedissonClient;
import org.redisson.api.StreamMessageId;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zack <br>
 * @create 2022-05-22 20:34 <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
@Component
public class RedisStreamQueueService {

    private static final String STREAM_NAME = "sensor#4921";
    private static final String STREAM_CONSUMEGROUP_NAME = "sensors_data";
    private static final String STREAM_CONSUMEGROUP_CONSUMER_NAME = "consumer_1";

    @Resource private RedissonClient redissonClient;

    public void sendMessage(Map<String, String> mapMsg) {
        RStream<String, String> stream = redissonClient.getStream(STREAM_NAME);
        stream.addAll(mapMsg);
    }

    public void sendMessage(String key, String message) {
        RStream<String, String> stream = redissonClient.getStream(STREAM_NAME);
        stream.add(key, message);
    }

    public void onMessage() {
        RStream<String, String> stream = redissonClient.getStream(STREAM_NAME);
        stream.createGroup(STREAM_CONSUMEGROUP_NAME, StreamMessageId.ALL);

        while (true) {
            Map<StreamMessageId, Map<String, String>> messages =
                    stream.readGroup(
                            STREAM_CONSUMEGROUP_NAME,
                            STREAM_CONSUMEGROUP_CONSUMER_NAME,
                            30,
                            TimeUnit.DAYS);
            for (Map.Entry<StreamMessageId, Map<String, String>> entry : messages.entrySet()) {
                Map<String, String> msg = entry.getValue();
                log.info("receive msg: {}", msg);
                stream.ack(STREAM_CONSUMEGROUP_NAME, entry.getKey());
            }
        }
    }
}
