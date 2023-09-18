package common.core.util.security;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.symmetric.AES;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
@Component
public class AesUtils {

    private static ObjectMapper om;

    public static String encryptHex(AES aes, byte[] data) {
        return aes.encryptHex(data);
    }

    public static String encryptHex(AES aes, InputStream data) {
        return aes.encryptHex(data);
    }

    public static String encryptHex(AES aes, String data) {
        return aes.encryptHex(data, UTF_8);
    }

    @SneakyThrows
    public static String encryptHex(AES aes, Object data) {

        if (ObjectUtil.isNull(data)) {
            return StrUtil.EMPTY;
        }

        if (ObjectUtil.isBasicType(data)) {
            return aes.encryptHex(data.toString(), UTF_8);
        }

        return aes.encryptHex(om.writeValueAsString(data), UTF_8);
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

    @Resource
    public void setOm(ObjectMapper objectMapper) {
        AesUtils.om = objectMapper;
    }
}
