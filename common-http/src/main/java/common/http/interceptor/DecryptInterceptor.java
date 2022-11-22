package common.http.interceptor;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import common.http.component.DecryptComponent;
import common.http.configuration.HttpProperties;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

import static common.http.constant.Constants.API_AUTH_FLAG;
import static common.http.constant.Constants.DECRYPT_TYPE_FLAG;

/**
 * Considering use strategy to customize handler. - split and get encrypted data. - combine
 * response.
 *
 * @author asd <br>
 * @create 2021-11-30 3:05 PM <br>
 * @project mc-middleware-api <br>
 */
@Slf4j
@Component
public class DecryptInterceptor implements Interceptor {

    @Resource private HttpProperties p;

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Response proceed = chain.proceed(request);
        ResponseBody body = proceed.body();
        String bodyStr = LoggingRequestInterceptor.getResponseBody(proceed);
        log.debug("[[http] debug encrypt response: {}", bodyStr);

        boolean authapi = BooleanUtil.toBoolean(request.headers().get(API_AUTH_FLAG));
        if (ObjectUtil.isNull(body) || CharSequenceUtil.isBlank(bodyStr) || authapi) {
            return proceed;
        }

        String decryptType = request.headers().get(DECRYPT_TYPE_FLAG);
        decryptType = StrUtil.isBlank(decryptType) ? p.getDecryptType().name() : decryptType;
        val typeEnum = HttpProperties.DecryptTypeEnum.getByName(decryptType);
        if (HttpProperties.DecryptTypeEnum.FULL.equals(typeEnum)) {
            try {
                // decrypt response
                String decrypt = DecryptComponent.tryDecrypt(bodyStr);
                ResponseBody decryptResponse = ResponseBody.create(body.contentType(), decrypt);
                return proceed.newBuilder().body(decryptResponse).build();
            } catch (Exception ex) {
                body.close();
                throw ex;
            }
        }

        return proceed;
    }
}
