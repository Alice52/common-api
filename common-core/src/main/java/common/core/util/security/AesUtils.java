package common.core.util.security;

import cn.hutool.crypto.symmetric.AES;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * base hutool
 *
 * @see cn.hutool.crypto.symmetric.AES
 * @author Zack Zhang
 */
@Slf4j
@Data
public class AesUtils {

    private static AES aes = new AES();

    public static String encryptHex(byte[] data) {
        return aes.encryptHex(data);
    }

    public static String encryptHex(InputStream data) {
        return aes.encryptHex(data);
    }

    public static String encryptHex(String data) {
        return aes.encryptHex(data, StandardCharsets.UTF_8);
    }

    /** 解密 */
    public static String decrypt(byte[] data) {
        return aes.decryptStr(data, StandardCharsets.UTF_8);
    }

    public static String decrypt(InputStream data) {
        return aes.decryptStr(data);
    }

    public static String decrypt(String data) {
        return aes.decryptStr(data, StandardCharsets.UTF_8);
    }
}
