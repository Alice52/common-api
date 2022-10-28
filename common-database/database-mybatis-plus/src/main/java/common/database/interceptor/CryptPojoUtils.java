package common.database.interceptor;

import java.lang.reflect.Field;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import common.core.util.security.AesUtil;
import common.database.sensitive.annotation.SensitiveField;
import lombok.experimental.UtilityClass;

/**
 * @author zack <br>
 * @create 2022-03-31 14:51 <br>
 * @project project-cloud-custom <br>
 */
@UtilityClass
public class CryptPojoUtils {
    /**
     * 对象t注解字段加密
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> T encrypt(T t) {
        if (isEncryptAndDecrypt(t)) {
            Field[] declaredFields = t.getClass().getDeclaredFields();
            try {
                if (declaredFields != null && declaredFields.length > 0) {
                    for (Field field : declaredFields) {
                        if (field.isAnnotationPresent(SensitiveField.class)
                                && field.getType().toString().endsWith("String")) {
                            field.setAccessible(true);
                            String fieldValue = (String) field.get(t);
                            if (StringUtils.isNotEmpty(fieldValue)) {
                                field.set(t, AesUtil.encrypt(fieldValue));
                            }
                            field.setAccessible(false);
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return t;
    }
    /**
     * 加密单独的字符串
     *
     * @param @param t
     * @param @return
     * @return T
     * @throws
     */
    public static <T> T EncryptStr(T t) {
        if (t instanceof String) {
            t = (T) AesUtil.encrypt((String) t);
        }
        return t;
    }
    /**
     * 对含注解字段解密
     *
     * @param t
     * @param <T>
     */
    public static <T> T decrypt(T t) {
        if (isEncryptAndDecrypt(t)) {
            Field[] declaredFields = t.getClass().getDeclaredFields();
            try {
                if (declaredFields != null && declaredFields.length > 0) {
                    for (Field field : declaredFields) {
                        if (field.isAnnotationPresent(SensitiveField.class)
                                && field.getType().toString().endsWith("String")) {
                            field.setAccessible(true);
                            String fieldValue = (String) field.get(t);
                            if (StringUtils.isNotEmpty(fieldValue)) {
                                field.set(t, AesUtil.decryptOrNull(fieldValue));
                            }
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return t;
    }
    /**
     * 判断是否需要加密解密的类
     *
     * @param @param t
     * @param @return
     * @return Boolean
     * @throws
     */
    public static <T> Boolean isEncryptAndDecrypt(T t) {
        Boolean reslut = false;
        if (t != null) {
            Object object = t.getClass().getAnnotation(SensitiveField.class);
            if (object != null) {
                reslut = true;
            }
        }
        return reslut;
    }
}
