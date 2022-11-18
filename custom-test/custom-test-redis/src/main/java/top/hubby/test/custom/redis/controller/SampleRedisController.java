package top.hubby.test.custom.redis.controller;

import common.core.util.R;
import common.redis.constants.enums.RedisKeyCommonEnum;
import common.redis.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Set;

import static common.redis.constants.enums.RedisKeyCommonEnum.GOODS_STOCK;
import static common.redis.constants.enums.RedisKeyCommonEnum.SCAN_SEARCH;

/**
 * @author zack <br>
 * @create 2021-06-25 09:57 <br>
 * @project swagger-3 <br>
 */
@Slf4j
@Api(tags = {"Redis Sample Api"})
@RestController
@RequestMapping("/redis")
public class SampleRedisController {

    @Resource private RedisUtil redisUtil;

    @PostMapping
    @ApiOperation("Batch Insert Sample")
    public @NotNull R<Boolean> batchInsert(@RequestParam("count") Integer count) {

        return R.<Boolean>builder()
                .data(redisUtil.batchInsert(RedisKeyCommonEnum.BATCH_DELETE, count))
                .build();
    }

    @DeleteMapping
    @ApiOperation("Batch Reg Delete Sample")
    public @NotNull R<Boolean> batchDelete(@RequestParam("key") String key) {

        return R.<Boolean>builder()
                .data(redisUtil.batchDeleteKey(RedisKeyCommonEnum.BATCH_DELETE, 1000, key + "*"))
                .build();
    }

    @GetMapping("/keys")
    @ApiOperation("Scan Search Sample")
    public @NotNull R<Set> search(@RequestParam("key") String key) {

        return R.<Set>builder().data(redisUtil.scanSearch(SCAN_SEARCH, 1000, key + "*")).build();
    }

    @GetMapping("/values")
    @ApiOperation("Scan Search With Value Sample")
    public @NotNull R<Map<String, Object>> searchWithValue(@RequestParam("key") String key) {

        return R.<Map<String, Object>>builder()
                .data(redisUtil.scanSearchWithValue(SCAN_SEARCH, 1000, key + "*"))
                .build();
    }

    @PostMapping("/init-stock")
    public @NotNull R initStock(@RequestParam("key") String key) {
        redisUtil.set(GOODS_STOCK, 100, key);
        return R.success();
    }

    @PutMapping("/stock")
    @ApiOperation("Reduce Stock By String")
    public @NotNull R<Boolean> reduceStock(
            @RequestParam("key") String key, @RequestParam("delta") Integer delta) {
        return R.<Boolean>builder().data(redisUtil.reduceStock(GOODS_STOCK, key)).build();
    }
}
