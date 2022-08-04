package common.redis.key;

/**
 * @author zack <br>
 * @create 2021-06-03 14:28 <br>
 * @project custom-test <br>
 */
@Deprecated
public abstract class BasePrefix implements KeyPrefix {

    @Override
    @Deprecated
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className + ":" + prefix;
    }
}
