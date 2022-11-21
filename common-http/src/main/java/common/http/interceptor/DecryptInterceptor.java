package common.http.interceptor;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import common.http.component.DecryptComponent;
import common.http.configuration.HttpProperties;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author asd <br>
 * @create 2021-11-30 3:05 PM <br>
 * @project mc-middleware-api <br>
 */
@Slf4j
@Component
public class DecryptInterceptor implements Interceptor {

    @Resource private HttpProperties p;
    @Resource private DecryptComponent decryptComponent;

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {

        Response proceed = chain.proceed(chain.request());
        ResponseBody body = proceed.body();
        if (ObjectUtil.isNull(body) || StrUtil.isBlank(body.toString())) {
            return proceed;
        }

        HttpProperties.DecryptTypeEnum typeEnum = p.getDecryptType();
        if (HttpProperties.DecryptTypeEnum.FULL.equals(typeEnum)) {
            // decrypt response
            Object decrypt = DecryptComponent.tryDecrypt(body.toString());
            ResponseBody decryptResponse =
                    ResponseBody.create(body.contentType(), decrypt.toString());
            return proceed.newBuilder().body(decryptResponse).build();
        }

        return proceed;
    }
}
