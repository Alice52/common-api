package common.http.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import common.http.constant.enums.RedisHttpEnum;
import common.http.exception.HttpException;
import common.http.model.TokenVO;
import common.http.service.TokenService;
import common.redis.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static common.http.constant.Constants.API_AUTH_FLAG;
import static common.http.constant.Constants.AUTHORIZATION;

/**
 * @author asd <br>
 * @create 2021-11-24 9:47 AM <br>
 * @project mc-platform <br>
 */
@Slf4j
@Component
public class BasicAuthInterceptor implements Interceptor {

    @Resource private TokenService middlewareService;
    @Resource private RedisUtil redisUtil;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Headers headers = request.headers();

        String contentType = headers.get("Content-Type");
        if (StrUtil.isBlank(contentType)) {
            request =
                    request.newBuilder()
                            .addHeader("Content-Type", "application/json;charset=UTF-8")
                            .build();
        }

        String needAuth = headers.get(API_AUTH_FLAG);
        if (StrUtil.isNotBlank(needAuth) && needAuth.equalsIgnoreCase(Boolean.TRUE.toString())) {
            return chain.proceed(request);
        }

        request =
                request.newBuilder()
                        .addHeader(AUTHORIZATION, buildHeader().get(AUTHORIZATION))
                        .build();

        Response response = chain.proceed(request);
        if (HttpStatus.HTTP_UNAUTHORIZED == response.code()) {
            // TODO: considering execute  again
            middlewareService.refreshToken();
            throw new HttpException("Invalid Token");
        }

        return response;
    }

    private Map<String, String> buildHeader() {
        String accessToken = redisUtil.get(RedisHttpEnum.TOKEN_KEY, String.class);
        if (accessToken == null || accessToken.isEmpty()) {
            accessToken =
                    Optional.ofNullable(middlewareService.token())
                            .orElse(new TokenVO())
                            .getAccessToken();
        }

        if (accessToken == null || accessToken.isEmpty()) {
            log.error("刷新之后仍然无法获取HMP Token");
            return new HashMap<>();
        }

        Map<String, String> headers = new HashMap<>(2);
        headers.put(AUTHORIZATION, "Bearer " + accessToken);

        return headers;
    }
}
