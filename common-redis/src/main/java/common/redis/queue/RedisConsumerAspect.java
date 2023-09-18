package common.redis.queue;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import common.redis.annotation.RedisConsumer;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RStream;
import org.redisson.api.RedissonClient;
import org.redisson.api.StreamMessageId;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.Map;

/**
 * @author zack <br>
 * @create 2022-05-23 09:27 <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
// @Configuration
public class RedisConsumerAspect {

    @Resource private RedissonClient redissonClient;
    @Resource private ApplicationContext applicationContext;

    @Bean
    public StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ?>
            streamMessageListenerContainerOptions() {
        return StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder()
                .pollTimeout(Duration.ofSeconds(1))
                .build();
    }

    @Bean
    public StreamMessageListenerContainer streamMessageListenerContainer(
            RedisConnectionFactory factory,
            StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ?>
                    lcOptions) {
        StreamMessageListenerContainer container =
                StreamMessageListenerContainer.create(factory, lcOptions);

        Map<String, Object> beansWithAnnotations =
                applicationContext.getBeansWithAnnotation(RedisConsumer.class);
        for (String key : beansWithAnnotations.keySet()) {
            subscribeStream(container, (StreamListener) beansWithAnnotations.get(key));
        }

        container.start();
        return container;
    }

    public void subscribeStream(
            StreamMessageListenerContainer listenerContainer, StreamListener listener) {

        String machineId = RandomUtil.randomString(8);
        try {
            RedisConsumer annotation = listener.getClass().getAnnotation(RedisConsumer.class);
            if (ObjectUtil.isNull(annotation)) {
                return;
            }

            String groupName = annotation.consumerGroup();
            String streamKey = annotation.streamKey();
            int consumerNum = annotation.consumerNum();
            for (int i = 0; i < consumerNum; i++) {
                StreamListener listenerInstance = applicationContext.getBean(listener.getClass());
                String consumerName = machineId + annotation.consumerName() + i;
                initRedisStreamAndGroup(streamKey, groupName);
                listenerContainer.receive(
                        Consumer.from(groupName, consumerName),
                        StreamOffset.create(streamKey, ReadOffset.lastConsumed()),
                        listenerInstance);
            }
        } catch (Exception ex) {
            log.warn("subscribeStreamFailed: {}", ex.getMessage());
        }
    }

    public void initRedisStreamAndGroup(String streamKey, String groupName) {
        try {
            RStream stream = redissonClient.getStream(streamKey);
            // 判断 stream 是否初始化，未初始化则进行初始化
            if (!Boolean.TRUE.equals(stream.isExists())) {
                // 往stream 发送消息，自动创建 stream
                StreamMessageId recordId =
                        stream.addAll(MapUtil.builder().put("init-key", "init-data").build());
                // 删除测试消息
                stream.remove(recordId);
            }
            // 创建group，如果group已经存在会报错，忽略该错误
            stream.createGroup(groupName);
        } catch (Exception ignored) {
        }
    }
}
