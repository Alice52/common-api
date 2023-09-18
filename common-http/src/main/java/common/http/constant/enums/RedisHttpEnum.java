package common.http.constant.enums;

import common.redis.key.KeyPrefix;
import lombok.extern.slf4j.Slf4j;

/**
 * @author asd <br>
 * @create 2021-12-07 4:34 PM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
public enum RedisHttpEnum implements KeyPrefix {
    TOKEN_KEY("token"),
    DECRYPT_TOKEN_KEY("decrypt_token"),
    ;

    private String prefix = KeyPrefix.prefix;

    RedisHttpEnum(String prefix) {
        this.prefix += prefix;
    }
}
