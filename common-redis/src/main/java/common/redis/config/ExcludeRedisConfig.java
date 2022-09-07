package common.redis.config;

import common.redis.aspect.IdempotentRequestAspect;
import common.redis.aspect.LimitRequestAspect;
import common.redis.aspect.RedisLockAspect;
import common.redis.component.RedisListQueueService;
import common.redis.component.RedisStreamQueueService;
import common.redis.configuration.RedisConfiguration;
import common.redis.queue.RedisConsumerAspect;
import common.redis.queue.RedisProducer;
import common.redis.utils.RedisKeyUtil;
import common.redis.utils.RedisUtil;
import org.redisson.spring.starter.RedissonAutoConfiguration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;

/**
 * @author zack <br>
 * @create 2022-09-07<br>
 * @project graphql <br>
 */
@EnableAutoConfiguration(
        exclude = {
            RedisConfiguration.class,
            RedisLockAspect.class,
            IdempotentRequestAspect.class,
            LimitRequestAspect.class,
            RedisAutoConfiguration.class,
            RedisRepositoriesAutoConfiguration.class,
            RedissonAutoConfiguration.class,
            RedisKeyUtil.class,
            RedisListQueueService.class,
            RedisStreamQueueService.class,
            RedisConsumerAspect.class,
            RedisProducer.class,
            IdempotentRequestAspect.class,
            RedisLockAspect.class,
            RedisUtil.class
        })
public class ExcludeRedisConfig {}
