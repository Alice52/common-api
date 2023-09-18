package common.redis.constants.enums;

import common.redis.key.KeyPrefix;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author zack <br>
 * @create 2021-06-04 17:16 <br>
 * @project custom-test <br>
 */
@NoArgsConstructor
@Getter
public enum RedisKeyCommonEnum implements KeyPrefix {
    VERIFY_CODE("verify_code"),
    CACHE_LIMIT("request_limit"),
    BATCH_DELETE("batch_delete"),
    SCAN_SEARCH("search"),
    GOODS_STOCK("stock"),
    ;

    private String prefix = KeyPrefix.prefix;

    RedisKeyCommonEnum(String prefix) {
        this.prefix += prefix;
    }
}
