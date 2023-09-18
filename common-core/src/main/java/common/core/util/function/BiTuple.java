package common.core.util.function;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @see reactor.util.function.Tuple2
 * @author asd <br>
 * @create 2021-12-03 3:53 PM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BiTuple<F, S> {
    private F f;
    private S s;

    public static <C1, C2> BiTuple<C1, C2> of(C1 c1, C2 c2) {
        return new BiTuple<>(c1, c2);
    }

    /**
     * Swaps the element of this Tuple.
     *
     * @return a new Tuple where the first element is the second element of this Tuple and the
     *     second element is the first element of this Tuple.
     */
    public BiTuple<S, F> swap() {
        return new BiTuple<>(this.s, this.f);
    }
}
