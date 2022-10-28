package common.core.test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author zack <br>
 * @create 2021-06-11 14:50 <br>
 * @project custom-test <br>
 */
@Slf4j
public class GuavaBloomFilter {
    private static final int expectedInsertions = 1000000;

    @Test
    public void testSort() {

        List<LocalDateTime> times = new ArrayList<>();

        times.add(LocalDateTime.now());
        times.add(LocalDateTime.MIN);
        times.add(LocalDateTime.MAX);

        times.sort(Comparator.comparing(Function.identity()));

        assert LocalDateTime.MIN.equals(times.get(0));
    }

    @Test
    public void testBloomFilter() {
        BloomFilter<String> bloomFilter =
                BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), expectedInsertions);

        List<String> list = new ArrayList<>(expectedInsertions);

        for (int i = 0; i < expectedInsertions; i++) {
            String uuid = UUID.randomUUID().toString();
            bloomFilter.put(uuid);
            list.add(uuid);
        }

        int mightContainNum1 = 0;
        for (int i = 0; i < 500; i++) {
            String key = list.get(i);
            if (bloomFilter.mightContain(key)) {
                mightContainNum1++;
            }
        }
        log.info("【key真实存在的情况】布隆过滤器认为存在的key值数：" + mightContainNum1);
        assert 480 <= mightContainNum1 && mightContainNum1 <= 500;
    }
}
