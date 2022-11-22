package common.core.util.collection;

import cn.hutool.core.lang.Assert;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author asd <br>
 * @create 2021-12-08 10:58 AM <br>
 * @project javase <br>
 */
@Slf4j
@UtilityClass
public class StreamUtil {

    public static <T> List<List<T>> split(List<T> list, int splitSize) {
        Assert.notNull(list, "list cannot be null");
        return Stream.iterate(0, f -> f + 1)
                .limit((list.size() + splitSize - 1) / splitSize)
                .map(
                        a ->
                                list.stream()
                                        .skip((long) a * splitSize)
                                        .limit(splitSize)
                                        .collect(toList()))
                .collect(toList());
    }
}
