package common.core.annotation.discriptor;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

/**
 * @author zack <br>
 * @create 2021-06-09 09:39 <br>
 * @project custom-test <br>
 */
@Getter
@AllArgsConstructor
public enum SensitiveStrategy {
    /** Username sensitive strategy. */
    USERNAME(s -> s.replaceAll("(\\S)\\S(\\S*)", "$1*$2")),
    /** Id card sensitive type. */
    ID_CARD(s -> s.replaceAll("(\\d{4})\\d{10}(\\w{4})", "$1****$2")),
    /** Phone sensitive type. */
    PHONE(s -> s.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2")),
    /** Address sensitive type. */
    ADDRESS(s -> s.replaceAll("(\\S{8})\\S{4}(\\S*)\\S{4}", "$1****$2****"));

    private final Function<String, String> deSensitiver;
}
