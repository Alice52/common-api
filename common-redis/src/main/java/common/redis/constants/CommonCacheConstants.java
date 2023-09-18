package common.redis.constants;

/**
 * @author zack <br>
 * @create 2021-06-03 16:27 <br>
 * @project custom-test <br>
 */
public interface CommonCacheConstants {

    /** common */
    String PROJECT_PREFIX = "custom";

    /** lock relative */
    String GLOBAL_LOCK_KEY = "DISTRIBUTE_LOCK";

    String MEMBER_LOCK_KEY_PLACE_HOLDER = "{}-MEMBER-ID-{}";

    String MODULE_PHASE_KEY = PROJECT_PREFIX + ":module:phase";
    String REQUEST_LIMIT = "limit";
}
