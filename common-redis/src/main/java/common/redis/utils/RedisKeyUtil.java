package common.redis.utils;

import cn.hutool.core.util.StrUtil;
import common.redis.key.KeyPrefix;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author zack <br>
 * @create 2021-06-03 14:32 <br>
 * @project custom-test <br>
 */
@Component
public class RedisKeyUtil {
    private static String module;

    /**
     * build key by prefix and addition info with modules.
     *
     * @param prefix
     * @param keys
     * @return
     */
    public static String buildKey(KeyPrefix prefix, String... keys) {
        return buildKey(module, prefix, keys);
    }

    protected static String buildKey(String module, KeyPrefix prefix, String... params) {

        StringBuilder realKey =
                new StringBuilder(
                        StrUtil.removeSuffix(
                                StrUtil.format(prefix.getPrefix(), module), StrUtil.COLON));
        for (String param : params) {
            realKey.append(StrUtil.COLON).append(param);
        }

        return realKey.toString();
    }

    @Value("${common.core.redis.module:common}")
    public void setModule(String module) {
        RedisKeyUtil.module = module;
    }
}
