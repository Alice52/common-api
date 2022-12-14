package top.hubby.test.custom.uid;

import common.uid.generator.DefaultUidGenerator;
import io.micrometer.core.instrument.util.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 去除输出语句吞吐量
 *
 * <pre>
 *     1. testSerialGenerate: 10w/11.402s
 *     2. testParallelGenerate: 10w/11.571s
 * </pre>
 *
 * @author zack <br>
 * @create 2021-06-26<br>
 * @project project-custom <br>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UidApplication.class)
public class DefaultUidGeneratorTest {
    private static final int SIZE = 100000; // 10w
    private static final boolean VERBOSE = true;
    private static final int THREADS = Runtime.getRuntime().availableProcessors() << 1;

    @Resource private DefaultUidGenerator defaultUidGenerator;

    /** Test for serially generate */
    @Test
    public void testSerialGenerate() {
        // Generate UID serially
        Set<Long> uidSet = new HashSet<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            doGenerate(uidSet, i);
        }

        // Check UIDs are all unique
        checkUniqueID(uidSet);
    }

    /**
     * Test for parallel generate
     *
     * @throws InterruptedException
     */
    @Test
    public void testParallelGenerate() throws InterruptedException {
        AtomicInteger control = new AtomicInteger(-1);
        Set<Long> uidSet = new ConcurrentSkipListSet<>();

        // Initialize threads
        List<Thread> threadList = new ArrayList<>(THREADS);
        for (int i = 0; i < THREADS; i++) {
            Thread thread = new Thread(() -> workerRun(uidSet, control));
            thread.setName("UID-generator-" + i);

            threadList.add(thread);
            thread.start();
        }

        // Wait for worker done
        for (Thread thread : threadList) {
            thread.join();
        }

        // Check generate 10w times
        Assert.assertEquals(SIZE, control.get());

        // Check UIDs are all unique
        checkUniqueID(uidSet);
    }

    /** Worker run */
    private void workerRun(Set<Long> uidSet, AtomicInteger control) {
        for (; ; ) {
            int myPosition = control.updateAndGet(old -> (old == SIZE ? SIZE : old + 1));
            if (myPosition == SIZE) {
                return;
            }

            doGenerate(uidSet, myPosition);
        }
    }

    /** Do generating */
    private void doGenerate(Set<Long> uidSet, int index) {
        long uid = defaultUidGenerator.getUID();
        String parsedInfo = defaultUidGenerator.parseUID(uid);
        uidSet.add(uid);

        // Check UID is positive, and can be parsed
        Assert.assertTrue(uid > 0L);
        Assert.assertTrue(StringUtils.isNotBlank(parsedInfo));

        if (VERBOSE) {
            System.out.println(
                    Thread.currentThread().getName() + " No." + index + " >>> " + parsedInfo);
        }
    }

    /** Check UIDs are all unique */
    private void checkUniqueID(Set<Long> uidSet) {
        System.out.println(uidSet.size());
        Assert.assertEquals(SIZE, uidSet.size());
    }
}
