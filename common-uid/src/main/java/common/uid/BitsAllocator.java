package common.uid;

import lombok.Getter;
import org.springframework.util.Assert;

/**
 * Allocate 64 bits for the UID(long)<br>
 * sign (fixed 1bit) -> deltaSecond -> workerId -> sequence(within the same second)
 *
 * @author zack <br>
 * @create 2021-06-23<br>
 * @project project-custom <br>
 */
@Getter
public class BitsAllocator {
    /** Total 64 bits */
    public static final int TOTAL_BITS = 1 << 6;

    private final int timestampBits;
    private final int workerIdBits;
    private final int sequenceBits;
    /** Max value for workId & sequence */
    private final long maxDeltaSeconds;

    private final long maxWorkerId;
    private final long maxSequence;
    /** Shift for timestamp & workerId */
    private final int timestampShift;

    private final int workerIdShift;
    /** Bits for [sign-> second-> workId-> sequence] */
    private int signBits = 1;

    /**
     * Constructor with timestampBits, workerIdBits, sequenceBits<br>
     * The highest bit used for sign, so <code>63</code> bits for timestampBits, workerIdBits,
     * sequenceBits
     */
    public BitsAllocator(int timestampBits, int workerIdBits, int sequenceBits) {
        // make sure allocated 64 bits
        int allocateTotalBits = signBits + timestampBits + workerIdBits + sequenceBits;
        Assert.isTrue(allocateTotalBits == TOTAL_BITS, "allocate not enough 64 bits");

        // initialize bits
        this.timestampBits = timestampBits;
        this.workerIdBits = workerIdBits;
        this.sequenceBits = sequenceBits;

        // initialize max value
        this.maxDeltaSeconds = ~(-1L << timestampBits);
        this.maxWorkerId = ~(-1L << workerIdBits);
        this.maxSequence = ~(-1L << sequenceBits);

        // initialize shift
        this.timestampShift = workerIdBits + sequenceBits;
        this.workerIdShift = sequenceBits;
    }

    /**
     * Allocate bits for UID according to delta seconds & workerId & sequence<br>
     * <b>Note that: </b>The highest bit will always be 0 for sign
     *
     * @param deltaSeconds
     * @param workerId
     * @param sequence
     * @return
     */
    public long allocate(long deltaSeconds, long workerId, long sequence) {
        return (deltaSeconds << timestampShift) | (workerId << workerIdShift) | sequence;
    }
}
