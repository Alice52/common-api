package common.core.util;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author zack <br>
 * @create 2021-06-21 13:22 <br>
 * @project swagger-3 <br>
 */
public final class SetUtil {

    @NotNull
    public final <T> Set<T> newHashSet(T... ts) {
        if (ts.length > 0) {
            return new LinkedHashSet<>(Arrays.asList(ts));
        }
        return new HashSet<>();
    }
}
