package common.core.constant.enums;

import java.util.Arrays;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author asd <br>
 * @create 2021-12-03 1:55 PM <br>
 * @project mc-middleware-api <br>
 */
@Slf4j
@AllArgsConstructor
@Getter
public enum EnvEnums {
    UNKNOWN(""),
    DEV("dev"),
    PROD("prod"),
    DEMO("test"),
    ;

    private String name;

    @NotNull
    public static EnvEnums getByName(String name) {
        return Arrays.stream(EnvEnums.values())
                .filter(x -> x.name.equals(name))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
