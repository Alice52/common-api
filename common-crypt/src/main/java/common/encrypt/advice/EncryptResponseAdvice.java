package common.encrypt.advice;

import cn.hutool.crypto.symmetric.AES;
import common.core.util.security.AesUtils;
import common.encrypt.annotation.FullEncrypt;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.Resource;

/**
 * @author Zack Zhang
 */
@ControllerAdvice
public class EncryptResponseAdvice implements ResponseBodyAdvice<Object> {

    @Resource private AES encryptAes;

    @Override
    public boolean supports(
            MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {

        return returnType.hasMethodAnnotation(FullEncrypt.class);
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {

        return AesUtils.encryptHex(encryptAes, body);
    }
}
