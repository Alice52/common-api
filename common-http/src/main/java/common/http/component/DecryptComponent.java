package common.http.component;

import cn.hutool.core.text.CharSequenceUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.core.util.security.AesUtil;
import common.http.constant.enums.RedisHttpEnum;
import common.http.exception.DecryptException;
import common.http.service.TokenService;
import common.redis.utils.RedisUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.crypto.spec.SecretKeySpec;
import java.util.Objects;
import java.util.concurrent.*;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author asd <br>
 * @create 2021-12-07 4:36 PM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
@Component
public class DecryptComponent {

    protected static final int TRY_DECRYPT_TIMES = 3;
    public static ExecutorService EXECUTOR =
            new ThreadPoolExecutor(
                    0,
                    200,
                    60L,
                    TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(),
                    Executors.defaultThreadFactory(),
                    new ThreadPoolExecutor.CallerRunsPolicy());

    // 默认解密算法
    private static final String EBC_ALGORITHM = "AES/ECB/PKCS5Padding";
    // 私有解密成员变量
    private static SecretKeySpec secretKey;

    private static TokenService tokenService;
    private static RedisUtil redisUtil;
    private static ObjectMapper objectMapper;

    @Resource
    public void setObjectMapper(ObjectMapper objectMapper) {
        DecryptComponent.objectMapper = objectMapper;
    }

    @Resource
    public void setTokenService(TokenService tokenService) {
        DecryptComponent.tokenService = tokenService;
    }

    @Resource
    public void setRedisUtil(RedisUtil redisUtil) {
        DecryptComponent.redisUtil = redisUtil;
    }

    /**
     * 密钥处理
     *
     * @param myKey
     */
    private static void setKey(String myKey) {
        byte[] key = myKey.getBytes(UTF_8);
        secretKey = new SecretKeySpec(key, "AES");
    }

    /**
     * 解密代码
     *
     * @param strToDecrypt
     * @param secret
     * @return
     */
    public static String decrypt(String strToDecrypt, String secret) {

        String val = AesUtil.decryptOrNull(strToDecrypt, secret);
        if (Objects.isNull(val)) {
            tokenService.refreshDecryptToken();
        }

        return val;
    }

    public static String encrypt(Object data, String secret) {

        return AesUtil.encrypt(data.toString(), secret);
    }

    @SneakyThrows
    public static <T> T tryDecrypt(String origin) {

        int retry = 1;
        while (retry <= TRY_DECRYPT_TIMES) {
            retry++;
            String decrypt = decrypt(origin, getDecryptKey());
            if (CharSequenceUtil.isNotBlank(decrypt)) {
                return objectMapper.readValue(decrypt, new TypeReference<T>() {});
            }
        }

        log.error(
                "[http] Error while decrypting[{}]: with retry {} times.",
                origin,
                TRY_DECRYPT_TIMES);
        throw new DecryptException("[http] Decrypting Response Error");
    }

    private static String getDecryptKey() {
        return redisUtil.get(RedisHttpEnum.DECRYPT_TOKEN_KEY, String.class);
    }
}
