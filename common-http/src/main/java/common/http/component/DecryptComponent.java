package common.http.component;

import common.http.service.BusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * @author asd <br>
 * @create 2021-12-07 4:36 PM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
@Component
public class DecryptComponent {
    // 默认解密算法
    private static final String CIPHER_ALGORITHM = "AES/GCM/NoPadding";
    // 私有解密成员变量
    private static SecretKeySpec secretKey;
    private static byte[] key;
    @Resource private BusinessService middlewareService;

    // 密钥处理
    private static void setKey(String myKey) {
        MessageDigest sha = null;
        key = myKey.getBytes(StandardCharsets.UTF_8);
        secretKey = new SecretKeySpec(key, "AES");
    }

    // 解密代码
    public String decrypt(String strToDecrypt, String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            log.info("Error while decrypting: " + e.toString());
            middlewareService.refreshDecryptToken();
        }
        return null;
    }
}
