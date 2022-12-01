package common.core.util.security;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * base hutool
 *
 * @see cn.hutool.crypto.symmetric.AES
 * @author Zack Zhang
 */
@Slf4j
@Data
@UtilityClass
public class AesUtils {

    public static String encryptHex(AES aes, byte[] data) {
        return aes.encryptHex(data);
    }

    public static String encryptHex(AES aes, InputStream data) {
        return aes.encryptHex(data);
    }

    public static String encryptHex(AES aes, String data) {
        return aes.encryptHex(data, UTF_8);
    }

    public static String encryptHex(AES aes, Object data) {

        if (ObjectUtil.isNull(data)) {
            return StrUtil.EMPTY;
        }

        if (ObjectUtil.isBasicType(data)) {
            return aes.encryptHex(data.toString(), UTF_8);
        }

        return aes.encryptHex(JSONUtil.toJsonStr(data), UTF_8);
    }

    /** 解密 */
    public static String decrypt(AES aes, byte[] data) {
        return aes.decryptStr(data, UTF_8);
    }

    public static String decrypt(AES aes, InputStream data) {
        return aes.decryptStr(data);
    }

    public static String decrypt(AES aes, String data) {
        return aes.decryptStr(data, UTF_8);
    }
}
