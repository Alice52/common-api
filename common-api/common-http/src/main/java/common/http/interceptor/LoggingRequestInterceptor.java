package common.http.interceptor;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.Buffer;
import okio.BufferedSource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static io.lettuce.core.protocol.LettuceCharsets.UTF8;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author asd <br>
 * @create 2021-11-24 9:24 AM <br>
 * @project mc-platform <br>
 */
@Component
@Slf4j
public class LoggingRequestInterceptor implements Interceptor {

    public static String getResponseBody(Response response) {

        try {
            ResponseBody responseBody = response.body();
            if (!ObjectUtil.isNull(responseBody)) {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE);
                Buffer buffer = source.buffer();
                return buffer.clone().readString(UTF_8);
            }
        } catch (Throwable e) {
            log.warn("get response body failed: ", e);
        }

        return null;
    }

    public static String getRequestBody(Request request) {
        try {
            RequestBody requestBody = request.body();
            if (!ObjectUtil.isNull(requestBody)) {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                return buffer.readString(UTF8);
            }
        } catch (Throwable e) {
            log.warn("get request body failed: ", e);
        }

        return null;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        LocalDateTime start = LocalDateTime.now();
        Request request = chain.request();
        Response response;
        try {
            response = chain.proceed(request);
            log.info("request cost: {}", Duration.between(LocalDateTime.now(), start));
        } catch (Exception ex) {
            LocalDateTime end = LocalDateTime.now();
            log.error(
                    "\n【请求地址】: {}\n【请求方式】：{}\n【请求耗时】：{}\n【请求参数】：{}\n【请求头】：{}\n【异常原因】:",
                    request.url(),
                    request.method(),
                    Duration.between(start, end),
                    getRequestBody(request),
                    request.headers(),
                    ex);

            throw ex;
        }
        LocalDateTime end = LocalDateTime.now();
        log.debug(
                "\n【请求地址】: {}\n【请求方式】：{}\n【请求耗时】：{}\n【请求参数】：{}\n【请求头】：{}\n【响应数据】：{}\n",
                request.url(),
                request.method(),
                Duration.between(start, end),
                getRequestBody(request),
                request.headers(),
                getResponseBody(response));

        return response;
    }
}
