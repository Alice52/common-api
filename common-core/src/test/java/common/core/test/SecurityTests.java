package common.core.test;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

import common.core.util.security.AesUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author asd <br>
 * @create 2021-12-17 1:27 PM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
public class SecurityTests {
    private static final String original = "www.gowhere.so";
    // * =====================================
    // AES: 秘钥16位, 加解密
    // * =====================================
    @Test
    public void testAes() {

        String cKey = "jkl;POIU1234++==";

        log.info(original);
        String enString = AesUtil.encrypt(original, cKey);
        log.info("加密后的字串是：" + enString);
        String DeString = AesUtil.decryptOrNull(enString, cKey);
        log.info("解密后的字串是：" + DeString);
    }

    // * =====================================
    // MD5是一种单向加密算法, 只能加密不能解密
    // * =====================================
    @Test
    @SneakyThrows
    public void testMd5() {
        MessageDigest md5 = MessageDigest.getInstance("MD5");

        // md5.update(text.getBytes());
        // digest()最后返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
        // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
        byte[] bytes = md5.digest(original.getBytes());
        BigInteger digest = new BigInteger(bytes);
        // 32位
        String s = digest.toString(16);
        log.info(s);
    }

    // * =====================================
    // BASE64 通常用作对二进制数据进行加密
    // * =====================================
    @Test
    public void testBase64() {

        byte[] encode = Base64.getEncoder().encode(original.getBytes());
        byte[] decode = Base64.getDecoder().decode(encode);
        log.info(new String(decode, StandardCharsets.UTF_8));
    }

    // * =====================================
    // DES 有秘钥的BASE64, 8位以上, 加解密
    // * =====================================
    @Test
    public void testDES() {
        assert true;
    }

    // * =====================================
    // RSA 非对称加密/解密, 用于加密、解密的密钥是不同的
    // * =====================================

    // * =====================================
    // SHA 数字签名等密码学应用中重要的工具
    // * =====================================
    @SneakyThrows
    @Test
    public void SHAEncrypt() {
        MessageDigest sha = MessageDigest.getInstance("SHA");
        byte[] sha_byte = sha.digest(original.getBytes());
        StringBuilder hexValue = new StringBuilder();
        for (byte b : sha_byte) {
            // 将其中的每个字节转成十六进制字符串：
            // byte 类型的数据最高位是符号位, 通过和 0xff 进行与操作, 转换为 int 类型的正整数
            String toHexString = Integer.toHexString(b & 0xff);
            hexValue.append(toHexString.length() == 1 ? "0" + toHexString : toHexString);
        }
        String s = hexValue.toString();
        log.info(s);

        // v2
        StringBuilder hexValue2 = new StringBuilder();
        for (byte b : sha_byte) {
            int val = ((int) b) & 0xff;
            if (val < 16) {
                hexValue2.append("0");
            }
            hexValue2.append(Integer.toHexString(val));
        }
        log.info(hexValue2.toString());
    }
}
