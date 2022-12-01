package common.encrypt.constants.enums;

import cn.hutool.crypto.symmetric.AES;
import common.core.util.R;
import common.core.util.security.AesUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * @author Zack Zhang
 */
@Getter
@AllArgsConstructor
public enum EncryptEnum {
    NONE((aes, x) -> x),
    @Deprecated
    FULL((aes, x) -> AesUtils.encryptHex(aes, x)),
    CUSTOM(
            (aes, x) -> {
                if (Objects.isNull(x)) {
                    return x;
                }

                if (x instanceof R) {
                    return encryptRData(aes, x);
                }

                return x;
            });

    public BiFunction<AES, Object, Object> encrypt;

    private static R encryptRData(@NotNull AES aes, @NotNull Object x) {
        R r = (R) x;
        Object data = r.getData();
        r.setData(AesUtils.encryptHex(aes, data));
        return r;
    }
}
