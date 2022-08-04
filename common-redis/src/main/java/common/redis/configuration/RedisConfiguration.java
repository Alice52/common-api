package common.redis.configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import common.core.jackson.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.*;
import org.springframework.scripting.support.ResourceScriptSource;

import javax.annotation.PostConstruct;
import java.time.Duration;

/**
 * @author zack <br>
 * @create 2021-06-03 13:18 <br>
 * @project custom-test <br>
 */
@Slf4j
@AutoConfigureBefore({RedisAutoConfiguration.class, RedissonAutoConfiguration.class})
@Configuration
public class RedisConfiguration {
    StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    /** handle value serialization */
    private Jackson2JsonRedisSerializer<Object> j2jrs =
            new Jackson2JsonRedisSerializer<>(Object.class);

    @PostConstruct
    public void initJ2jrs() {
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.registerModule(new JavaTimeModule());
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        j2jrs.setObjectMapper(om);
    }

    /**
     * Notice: if donnot config redis as CacheManager, it will use memory as CacheManager.
     *
     * @param factory
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {

        RedisCacheConfiguration config =
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ZERO)
                        .serializeKeysWith(
                                RedisSerializationContext.SerializationPair.fromSerializer(
                                        stringRedisSerializer))
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair.fromSerializer(j2jrs))
                        .disableCachingNullValues();

        return RedisCacheManager.builder(factory).cacheDefaults(config).build();
    }

    @Bean("springSessionDefaultRedisSerializer")
    public RedisSerializer<Object> setSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

    /**
     * This serializer will contains Class info.
     *
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // handle key serialization
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        // handle value serialization
        redisTemplate.setValueSerializer(j2jrs);
        redisTemplate.setHashValueSerializer(j2jrs);

        /*
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.json());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(RedisSerializer.json());
        */
        return redisTemplate;
    }

    @Bean
    @SuppressWarnings("unchecked")
    public RedisScript<Long> limitRedisScript() {
        DefaultRedisScript redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(
                new ResourceScriptSource(new ClassPathResource("scripts/redis/limit.lua")));
        redisScript.setResultType(Long.class);
        return redisScript;
    }

    @Bean
    @SuppressWarnings("unchecked")
    public RedisScript<String> batchRegDelete() {
        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(
                new ResourceScriptSource(new ClassPathResource("scripts/redis/reg_batch_del.lua")));
        redisScript.setResultType(String.class);

        return redisScript;
    }

    @Bean
    @SuppressWarnings("unchecked")
    public RedisScript<Boolean> reduceStock() {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(
                new ResourceScriptSource(new ClassPathResource("scripts/redis/reduce_stock.lua")));
        redisScript.setResultType(Boolean.class);

        return redisScript;
    }
}
