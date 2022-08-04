package common.uid.utils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Represents a padded {@link AtomicLong} to prevent the FalseSharing problem
 *
 * <p>The CPU cache line commonly be 64 bytes, here is a sample of cache line after padding:<br>
 * 64 bytes = 8 bytes (object reference) + 6 * 8 bytes (padded long) + 8 bytes (a long value)
 *
 * <p>such as object layout<br>
 * 64 bytes = 8 bytes(_mark) + 8[/4] byte(*cp) + 5 * 8 bytes(padded long) + 8 bytes(a long value)
 *
 * @author zack <br>
 * @create 2021-06-23<br>
 * @project project-custom <br>
 */
public class PaddedAtomicLong extends AtomicLong {
    private static final long serialVersionUID = -3415778863941386253L;

    /**
     * todo: should fill 5*long? <br>
     * Padded 6 long (48 bytes)
     */
    public volatile long p1, p2, p3, p4, p5, p6 = 7L;

    /** Constructors from {@link AtomicLong} */
    public PaddedAtomicLong() {
        super();
    }

    public PaddedAtomicLong(long initialValue) {
        super(initialValue);
    }

    /** To prevent GC optimizations for cleaning unused padded references */
    public long sumPaddingToPreventOptimization() {
        return p1 + p2 + p3 + p4 + p5 + p6;
    }
}
