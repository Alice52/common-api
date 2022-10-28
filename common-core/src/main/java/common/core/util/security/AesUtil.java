package common.core.util.security;

import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author asd <br>
 * @create 2021-12-17 9:28 AM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
@UtilityClass
public class AesUtil {

    private static final String AES = "AES";
    private static final String DEFAULT_ALG = "AES/ECB/PKCS5PADDING";
    private static final String DEFAULT_SECRET = "f6631853b09511ecbedf00163e10bd4c";

    public static String encrypt(Object data) {

        if (ObjectUtil.isNull(data)) {
            return StrUtil.EMPTY;
        }

        if (ObjectUtil.isBasicType(data)) {
            return encrypt(data.toString(), DEFAULT_SECRET, DEFAULT_ALG);
        }

        return encrypt(JSONUtil.toJsonStr(data), DEFAULT_SECRET, DEFAULT_ALG);
    }

    public static String encrypt(String data, String secret) {
        return encrypt(data, secret, DEFAULT_ALG);
    }
    // 加密
    public static String encrypt(String data, String secret, String alg) {

        Assert.notEmpty(secret, "Encrypt Key is Empty");
        String decryptAlgorithm = Optional.ofNullable(alg).orElse(DEFAULT_ALG);
        try {
            byte[] key = Arrays.copyOf(secret.getBytes(UTF_8), 16);
            SecretKeySpec secretKey = new SecretKeySpec(key, AES);
            Cipher cipher = Cipher.getInstance(decryptAlgorithm);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] original = cipher.doFinal(data.getBytes(UTF_8));
            // return new BASE64Encoder().encode(original);
            return Base64.getEncoder().encodeToString(original);
        } catch (Exception e) {
            log.info("Error while encrypting: ", e);
            // throw new BaseException(CommonResponseEnum.ENCODING_ERROR);
        }

        return null;
    }

    public static String decryptOrNull(String data) {
        return decryptOrNull(data, DEFAULT_SECRET, DEFAULT_ALG);
    }

    public static String decryptOrNull(String data, String secret) {
        return decryptOrNull(data, secret, DEFAULT_ALG);
    }

    public static <T> T decryptOrNull(String data, Class<T> type) {
        String decryptStr = decryptOrNull(data, DEFAULT_SECRET, DEFAULT_ALG);
        if (JSONUtil.isJson(decryptStr)) {
            return JSONUtil.toBean(decryptStr, type);
        }

        return Convert.convert(type, decryptStr);
    }

    /**
     * @param data
     * @param secret
     * @param alg AES/ECB/PKCS5PADDING
     * @return
     * @throws Exception
     */
    public static String decryptOrNull(String data, String secret, String alg) {

        Assert.notEmpty(secret, "Decrypt Key is Empty");
        String decryptAlgorithm = Optional.ofNullable(alg).orElse(DEFAULT_ALG);
        try {
            byte[] key = Arrays.copyOf(secret.getBytes(UTF_8), 16);
            SecretKeySpec secretKey = new SecretKeySpec(key, AES);
            Cipher cipher = Cipher.getInstance(decryptAlgorithm);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            // byte[] original = cipher.doFinal(new BASE64Decoder().decodeBuffer(data));
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(data));
            return new String(original, UTF_8);
        } catch (Exception e) {
            log.info("Error while decrypting: ", e);
            // throw new BaseException(CommonResponseEnum.DECODING_ERROR);
        }

        return null;
    }
}
