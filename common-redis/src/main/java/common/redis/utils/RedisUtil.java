package common.redis.utils;

import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import common.core.util.se.LocalDateTimeUtil;
import common.redis.constants.enums.RedisKeyCommonEnum;
import common.redis.key.KeyPrefix;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static common.core.constant.CommonConstants.ZERO;
import static common.redis.utils.RedisKeyUtil.buildKey;

/**
 * // TODO:
 *
 * <pre>
 *     1. need add exception catch and handle
 *     2. 30 * 6 need configured
 *     3. remove expire from KeyPrefix
 * </pre>
 *
 * <pre>
 *      1. list 可以直接修改指定下标的元素: set(K key, long index, V value)
 *      2. set 不可以修改: UPDATE = SREM + SADD
 *  </pre>
 *
 * @author zack <br>
 * @create 2021-06-03 14:26 <br>
 * @project custom-test <br>
 */
@Component
@Slf4j
public class RedisUtil {

    @Resource private RedisTemplate<String, Object> redisTemplate;
    @Resource private RedisScript<String> batchRegDelete;
    @Resource private RedisScript<Boolean> reduceStock;
    @Resource private StringRedisTemplate stringRedisTemplate;

    /**
     * Batch Delete According To Regex: 只是减小了连接消耗, <br>
     * LUA 叫本事原子性的, 还是会长时间 Block, 因此还是多次连接去删除<br>
     * 问题: 如果能匹配到但是删除不掉则会一直执行
     *
     * @param prefix prefix
     * @param keys
     * @param batchCount
     * @return
     */
    public Boolean batchDeleteKey(KeyPrefix prefix, int batchCount, String... keys) {

        String matchKey = buildKey(prefix, keys);
        String cursor =
                stringRedisTemplate.execute(
                        batchRegDelete,
                        Lists.newArrayList(),
                        ZERO,
                        matchKey,
                        String.valueOf(batchCount));

        while (ObjectUtil.isNotNull(cursor) && !cursor.equals(ZERO)) {

            log.debug("cursor: {}", cursor);
            cursor =
                    stringRedisTemplate.execute(
                            batchRegDelete,
                            Lists.newArrayList(),
                            cursor,
                            matchKey,
                            String.valueOf(batchCount));
        }

        return Boolean.TRUE;
    }

    @Deprecated
    public Boolean batchDeleteKeyV0(KeyPrefix prefix, int batchCount, String... keys) {

        DefaultRedisScript<String> batchRegDelete = new DefaultRedisScript<>();
        String luaScript =
                "local keys = {};\n"
                        + "local cursor = \"0\"\n"
                        + "\n"
                        + "local result = redis.call(\"SCAN\", ARGV[1], \"match\", ARGV[2], \"count\", ARGV[3])\n"
                        + "cursor = result[1];\n"
                        + "keys = result[2];\n"
                        + "for i, key in ipairs(keys) do\n"
                        + "    redis.call(\"DEL\", key);\n"
                        + "end\n"
                        + "\n"
                        + "return cursor;";

        batchRegDelete.setScriptText(luaScript);
        batchRegDelete.setResultType(String.class);

        String matchKey = buildKey(prefix, keys);
        String cursor =
                stringRedisTemplate.execute(
                        batchRegDelete,
                        Lists.newArrayList(),
                        ZERO,
                        matchKey,
                        String.valueOf(batchCount));

        while (ObjectUtil.isNotNull(cursor) && !cursor.equals(ZERO)) {
            cursor =
                    stringRedisTemplate.execute(
                            batchRegDelete,
                            Lists.newArrayList(),
                            cursor,
                            matchKey,
                            String.valueOf(batchCount));
        }

        return Boolean.TRUE;
    }

    /**
     * 抽奖的实现.
     *
     * @param prefix
     * @param count
     * @param keys
     * @return
     */
    public Set<Object> randomMembers(KeyPrefix prefix, long count, String... keys) {

        return redisTemplate.opsForSet().distinctRandomMembers(buildKey(prefix, keys), count);
    }

    /**
     * 原子的设置一个值.
     *
     * @param prefix
     * @param key
     * @param value
     * @param time
     * @param timeUnit
     * @return
     */
    public Boolean setIfAbsent(
            KeyPrefix prefix, Object value, long time, TimeUnit timeUnit, String... key) {

        return redisTemplate
                .opsForValue()
                .setIfAbsent(buildKey(prefix, key), value, time, timeUnit);
    }

    @Deprecated
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public Boolean hasKey(KeyPrefix prefix, String... key) {

        return redisTemplate.hasKey(buildKey(prefix, key));
    }

    @Deprecated
    public Long increment(String key, long delta) {

        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * atomic update value.
     *
     * @param prefix
     * @param delta
     * @param key
     * @return
     */
    public Long increment(KeyPrefix prefix, long delta, String... key) {

        return redisTemplate.opsForValue().increment(buildKey(prefix, key), delta);
    }

    /**
     * atomic update value and expire info.
     *
     * @param prefix
     * @param delta
     * @param seconds
     * @param unit
     * @param keys
     * @return
     */
    @Deprecated
    public long increment(
            KeyPrefix prefix, long delta, final long seconds, final TimeUnit unit, String... keys) {

        Long increment = increment(prefix, delta, keys);
        boolean expire = expire(buildKey(prefix, keys), seconds, unit);

        if (expire) {
            return increment;
        }

        return 0;
    }

    /**
     * unit is {@link TimeUnit#HOURS}
     *
     * @param prefix
     * @param e
     * @param keys
     * @param <E>
     */
    public <E> void set(KeyPrefix prefix, E e, String... keys) {

        set(e, buildKey(prefix, keys), -1, TimeUnit.SECONDS);
    }

    /**
     * set k-v with expire info.
     *
     * @param prefix
     * @param e
     * @param seconds
     * @param unit
     * @param keys
     * @param <E>
     */
    public <E> void set(
            KeyPrefix prefix, E e, final long seconds, final TimeUnit unit, String... keys) {

        set(e, buildKey(prefix, keys), seconds, unit);
    }

    /**
     * expire specified key.
     *
     * @param prefix
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    public Boolean expire(KeyPrefix prefix, String key, final long timeout, final TimeUnit unit) {

        return redisTemplate.expire(buildKey(prefix, key), timeout, unit);
    }

    /**
     * get specified key.
     *
     * @param prefix
     * @param clazz
     * @param key
     * @param <E>
     * @return
     */
    public <E> E get(KeyPrefix prefix, Class<E> clazz, String... key) {

        return get(buildKey(prefix, key), clazz);
    }

    /**
     * remove keys.
     *
     * @param prefix
     */
    public void removeAll(KeyPrefix prefix) {
        Set<String> keys = redisTemplate.keys(buildKey(prefix, "*"));
        Optional.ofNullable(keys).ifPresent(x -> redisTemplate.delete(keys));
    }

    /**
     * Remove all cache keys found by makers.
     *
     * @param prefix
     * @param keys
     */
    public void remove(KeyPrefix prefix, String... keys) {
        Arrays.stream(keys).forEach(x -> remove(prefix, x));
    }

    /**
     * Set specified key expiration.
     *
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    private Boolean expire(String key, final long timeout, final TimeUnit unit) {

        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * real set key with expire.
     *
     * @param obj
     * @param realKey
     * @param timeout
     * @param unit
     */
    private void set(Object obj, String realKey, final long timeout, final TimeUnit unit) {
        Optional.ofNullable(obj)
                .ifPresent(
                        x -> {
                            if (timeout == -1) {
                                redisTemplate.opsForValue().set(realKey, obj);
                            } else {
                                redisTemplate.opsForValue().set(realKey, obj, timeout, unit);
                            }
                        });
    }

    /**
     * get specified k's value.
     *
     * @param realKey
     * @param <E>
     * @return
     */
    private <E> E get(String realKey, Class<E> clazz) {

        Object object = redisTemplate.opsForValue().get(realKey);
        if (ObjectUtil.isNotNull(object)) {
            E e = null;
            try {
                e = clazz.cast(object);
            } catch (ClassCastException ex) {
                log.warn("cast object: {} to type: {} error: {}", object, clazz, ex);
            }

            return e;
        }

        return null;
    }

    /**
     * batch remove keys.
     *
     * @param prefix
     * @param x
     */
    @Deprecated
    private void remove(KeyPrefix prefix, String x) {
        Set<String> keySet = redisTemplate.keys(buildKey(prefix, x));
        Optional.ofNullable(keySet).ifPresent(y -> redisTemplate.delete(keySet));
    }

    @Deprecated
    public Boolean batchInsert(KeyPrefix prefix, Integer count) {

        int loop = 0;
        int batchSize = 100000;

        while (count - loop * batchSize >= 0) {
            val map = new HashMap<String, String>();
            IntStream.rangeClosed(loop * batchSize, count - loop * batchSize)
                    .forEach(
                            i ->
                                    map.put(
                                            buildKey(prefix, "mc_fb:user_notice_" + i),
                                            String.valueOf(i)));

            LocalDateTime startTime = LocalDateTime.now();
            redisTemplate.opsForValue().multiSet(map);
            Duration between = Duration.between(startTime, LocalDateTime.now());

            log.info("set 100000 key cost {} ", between);
            loop++;
        }

        return Boolean.TRUE;
    }

    /**
     * Scan search.
     *
     * @param prefix
     * @param batchCount
     * @param keys
     * @return
     */
    public Set<String> scanSearch(RedisKeyCommonEnum prefix, int batchCount, String... keys) {

        ScanOptions scanOptions =
                ScanOptions.scanOptions().match(buildKey(prefix, keys)).count(batchCount).build();
        ConvertingCursor<byte[], String> cursor =
                redisTemplate.executeWithStickyConnection(
                        conn ->
                                new ConvertingCursor<>(
                                        conn.scan(scanOptions),
                                        new StringRedisSerializer()::deserialize));

        if (ObjectUtil.isNotNull(cursor)) {
            Set<String> set = Sets.newHashSet();
            cursor.forEachRemaining(set::add);
            return set;
        }
        return Sets.newHashSet();
    }

    public Map<String, Object> scanSearchWithValue(
            RedisKeyCommonEnum prefix, int batchCount, String... keys) {

        Set<String> matchedKeys = scanSearch(prefix, batchCount, keys);
        Map<String, Object> map = new HashMap<>();
        matchedKeys.forEach(x -> map.put(x, get(x, Object.class)));

        return map;
    }

    public Boolean reduceStock(RedisKeyCommonEnum prefix, String... keys) {

        String matchKey = buildKey(prefix, keys);
        return stringRedisTemplate.execute(reduceStock, Lists.newArrayList(), matchKey);
    }

    public Boolean reduceStock(RedisKeyCommonEnum prefix, int delta, String... keys) {

        String matchKey = buildKey(prefix, keys);
        return stringRedisTemplate.execute(
                reduceStock, Lists.newArrayList(), matchKey, String.valueOf(delta));
    }

    /**
     * <code>
     *     LocalDateTime dateTime = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(7, 0));
     * </code>
     *
     * @param key
     * @param dateTime
     * @return
     */
    public boolean expireAt(String key, LocalDateTime dateTime) {
        try {
            Date date = LocalDateTimeUtil.localDateTime2Date(dateTime);
            return redisTemplate.expireAt(key, date);
        } catch (Exception e) {
            log.error("expireAt failed: ", e);
            return false;
        }
    }
    /**
     * 查看是否已签到
     *
     * @param prefix
     * @param offset
     * @param key
     * @return
     */
    public Boolean getBit(KeyPrefix prefix, int offset, String... key) {

        String buildKey = buildKey(prefix, key);
        return redisTemplate.opsForValue().getBit(buildKey, offset);
    }

    /**
     * 签到
     *
     * @param prefix
     * @param offset
     * @param key
     * @return
     */
    public Boolean setBit(KeyPrefix prefix, int offset, String... key) {

        Boolean aBoolean = redisTemplate.opsForValue().setBit(buildKey(prefix, key), offset, true);

        return aBoolean;
    }

    /**
     * Get sign info of period.
     *
     * @param prefix
     * @param start
     * @param offset
     * @param key
     * @return
     */
    public Long bitField(KeyPrefix prefix, int start, int offset, String... key) {
        // bitfield user:sgin:5:202011 u30 0
        BitFieldSubCommands bitFieldSubCommands =
                BitFieldSubCommands.create()
                        .get(BitFieldSubCommands.BitFieldType.unsigned(offset))
                        .valueAt(0);
        List<Long> list =
                redisTemplate.execute(
                        (RedisCallback<List<Long>>)
                                con ->
                                        con.bitField(
                                                buildKey(prefix, key).getBytes(),
                                                bitFieldSubCommands));

        if (list == null || list.isEmpty()) {
            return 0L;
        }

        return list.get(0) == null ? 0L : list.get(0);
    }

    public Long bitCount(KeyPrefix prefix, String key) {
        // e.g. BITCOUNT user:sign:5:202011
        return redisTemplate.execute(
                (RedisCallback<Long>) con -> con.bitCount(buildKey(prefix, key).getBytes()));
    }
}
