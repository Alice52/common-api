package common.encrypt.advice;

import cn.hutool.crypto.symmetric.AES;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.core.exception.BusinessException;
import common.core.util.security.AesUtils;
import common.core.util.web.WebUtil;
import common.encrypt.annotation.Decrypt;
import common.encrypt.aspect.DecryptAspect;
import common.encrypt.constants.CryptConstant;
import common.encrypt.constants.enums.CryptExceptionEnums;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;

import static common.encrypt.constants.enums.CryptExceptionEnums.DECRYPT_FIELD_NOT_FOUND;

/**
 * just work for @see RequestBody
 *
 * @see DecryptAspect
 * @author Zack Zhang
 */
@Slf4j
@ControllerAdvice
public class DecryptRequestAdvice extends RequestBodyAdviceAdapter {
    @Resource private ObjectMapper objectMapper;
    @Resource private AES decryptAes;

    /**
     * 方法上有DecryptionAnnotation注解的，进入此拦截器
     *
     * @param methodParameter 方法参数对象
     * @param targetType 参数的类型
     * @param converterType 消息转换器
     * @return true，进入，false，跳过
     */
    @Override
    public boolean supports(
            MethodParameter methodParameter,
            Type targetType,
            Class<? extends HttpMessageConverter<?>> converterType) {
        return methodParameter.hasMethodAnnotation(Decrypt.class);
    }

    /**
     * 转换之后，执行此方法，解密，赋值
     *
     * @param body spring解析完的参数
     * @param inputMessage 输入参数
     * @param parameter 参数对象
     * @param targetType 参数类型
     * @param converterType 消息转换类型
     * @return 真实的参数
     */
    @SneakyThrows
    @Override
    public Object afterBodyRead(
            Object body,
            HttpInputMessage inputMessage,
            MethodParameter parameter,
            Type targetType,
            Class<? extends HttpMessageConverter<?>> converterType) {

        Decrypt decrypt = parameter.getMethodAnnotation(Decrypt.class);
        String field = decrypt.field();
        HttpServletRequest request = WebUtil.getCurrentRequest();

        // 获取数据
        ServletInputStream inputStream = request.getInputStream();
        JsonNode node = objectMapper.readValue(inputStream, JsonNode.class);
        JsonNode dnode;
        if (!node.has(field) || !(dnode = node.get(field)).isTextual()) {
            throw new BusinessException(DECRYPT_FIELD_NOT_FOUND);
        }
        String origin = dnode.asText();

        // 放入解密之前的数据
        request.setAttribute(CryptConstant.INPUT_ORIGINAL_DATA, origin);

        // 解密
        String decryptText;
        try {
            decryptText = AesUtils.decrypt(decryptAes, origin);
            // 放入解密之后的数据
            request.setAttribute(CryptConstant.INPUT_DECRYPT_DATA, decryptText);
            // 获取结果
            return objectMapper.readValue(decryptText, body.getClass());
        } catch (Exception e) {
            log.error("decrypt request occurs errors:  ", e);
            throw new BusinessException(CryptExceptionEnums.DECRYPT_FAILED);
        }
    }

    /**
     * 如果body为空，转为空对象
     *
     * @param body spring解析完的参数
     * @param inputMessage 输入参数
     * @param parameter 参数对象
     * @param targetType 参数类型
     * @param converterType 消息转换类型
     * @return 真实的参数
     */
    @Override
    @Nullable
    public Object handleEmptyBody(
            @Nullable Object body,
            HttpInputMessage inputMessage,
            MethodParameter parameter,
            Type targetType,
            Class<? extends HttpMessageConverter<?>> converterType) {

        return body;
    }
}
