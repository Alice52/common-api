package common.uid.worker;

/**
 * WorkerNodeType
 * <li>CONTAINER: Such as Docker
 * <li>ACTUAL: Actual machine
 *
 * @author zack <br>
 * @create 2021-06-23<br>
 * @project project-custom <br>
 */
public enum WorkerNodeType implements ValuedEnum<Integer> {
    CONTAINER(1),
    ACTUAL(2);

    /** Lock type */
    private final Integer type;

    /** Constructor with field of type */
    private WorkerNodeType(Integer type) {
        this.type = type;
    }

    @Override
    public Integer value() {
        return type;
    }
}
