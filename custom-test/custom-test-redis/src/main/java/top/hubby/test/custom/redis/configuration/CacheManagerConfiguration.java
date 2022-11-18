package top.hubby.test.custom.redis.configuration;

import common.redis.constants.CommonCacheConstants;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zack <br>
 * @create 2021-06-03 16:27 <br>
 * @project custom-test <br>
 */
@EnableCaching
@Configuration
public class CacheManagerConfiguration {

    /**
     * 可以在这里指定某个 key 的过期时间
     *
     * @return
     */
    @Bean
    RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return builder -> {
            Map<String, RedisCacheConfiguration> configurationMap = new HashMap<>(2);

            configurationMap.put(
                    CommonCacheConstants.MODULE_PHASE_KEY,
                    RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(2)));
            builder.withInitialCacheConfigurations(configurationMap);
        };
    }
}
