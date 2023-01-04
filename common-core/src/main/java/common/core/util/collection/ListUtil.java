package common.core.util.collection;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * @author asd <br>
 * @create 2022-08-16 08:41 AM <br>
 * @project common-core <br>
 */
@Slf4j
@UtilityClass
public class ListUtil {

    public static <R, T> List<T> diff(List<R> olds, List<R> latests, Function<R, T> keyMapper) {
        return diff(olds, latests, keyMapper, Objects::equals, BeanUtil::copyProperties);
    }

    /**
     * @param olds need updated
     * @param latests need created
     * @param keyMapper
     * @param equalsPre
     * @param updator
     * @param <R>
     * @param <T>
     * @return need deleted
     */
    public static <R, T> List<T> diff(
            List<R> olds,
            List<R> latests,
            Function<R, T> keyMapper,
            BiPredicate<R, R> equalsPre,
            BiConsumer<R, R> updator) {

        if (Objects.isNull(latests)) {
            latests = new ArrayList<>();
        }

        if (Objects.isNull(olds)) {
            olds = new ArrayList<>();
        }

        Map<T, R> latestMap =
                latests.stream()
                        .collect(
                                HashMap::new,
                                (map, value) -> map.putIfAbsent(keyMapper.apply(value), value),
                                HashMap::putAll);

        List<T> needDeleted = new ArrayList<>();
        Iterator<R> oldIterator = olds.iterator();
        while (oldIterator.hasNext()) {
            val old = oldIterator.next();
            val latest = latestMap.get(keyMapper.apply(old));

            if (ObjectUtil.isNull(latest)) {
                oldIterator.remove();
                needDeleted.add(keyMapper.apply(old));
                continue;
            }

            if (equalsPre.test(old, latest)) {
                oldIterator.remove();
                latestMap.remove(keyMapper.apply(latest));
                continue;
            }

            if (Objects.nonNull(updator)) {
                updator.accept(latest, old);
            }

            latestMap.remove(keyMapper.apply(latest));
        }

        latests.clear();
        latests.addAll(latestMap.values());

        needDeleted.remove(null);

        return needDeleted;
    }
}
