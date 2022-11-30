package common.encrypt.util;

import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import common.encrypt.annotation.Decrypt;
import common.encrypt.annotation.Encrypt;
import common.encrypt.configuration.properties.CryptProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author Zack Zhang
 */
@Component
public class CryptUtil {

    @Resource private CryptProperties cryptProperties;

    @Autowired(required = false)
    private AES encryptAes;

    @Autowired(required = false)
    private AES decryptAes;

    public CryptProperties.Properties getCryptProperties(@NotNull Encrypt encrypt) {
        assert Objects.nonNull(encrypt) : "encrypt value can not null";
        CryptProperties.Properties ecp = cryptProperties.getEncrypt();
        String key = Objects.isNull(encrypt.key()) ? ecp.getKey() : encrypt.key();
        String iv = Objects.isNull(encrypt.iv()) ? ecp.getIv() : encrypt.iv();
        Mode mode = Objects.isNull(encrypt.mode()) ? ecp.getMode() : encrypt.mode();
        Padding padding = Objects.isNull(encrypt.padding()) ? ecp.getPadding() : encrypt.padding();

        return CryptProperties.Properties.builder()
                .key(key)
                .iv(iv)
                .padding(padding)
                .mode(mode)
                .build();
    }

    public CryptProperties.Properties getCryptProperties(@NotNull Decrypt decrypt) {
        assert Objects.nonNull(decrypt) : "decrypt value can not null";
        CryptProperties.Properties dcp = cryptProperties.getDecrypt();
        String key = Objects.isNull(decrypt.key()) ? dcp.getKey() : decrypt.key();
        String iv = Objects.isNull(decrypt.iv()) ? dcp.getIv() : decrypt.iv();
        Mode mode = Objects.isNull(decrypt.mode()) ? dcp.getMode() : decrypt.mode();
        Padding padding = Objects.isNull(decrypt.padding()) ? dcp.getPadding() : decrypt.padding();

        return CryptProperties.Properties.builder()
                .key(key)
                .iv(iv)
                .padding(padding)
                .mode(mode)
                .build();
    }
}
