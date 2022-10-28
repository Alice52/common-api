package common.http.interceptor;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.http.component.DecryptComponent;
import common.http.constant.enums.RedisHttpEnum;
import common.http.exception.DecryptException;
import common.redis.utils.RedisUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.ResponseBody;

import org.springframework.stereotype.Component;

import static common.http.constant.Constants.PAYLOAD;
import static common.http.constant.Constants.RESPONSE_DATA;

/**
 * @author asd <br>
 * @create 2021-11-30 3:05 PM <br>
 * @project mc-middleware-api <br>
 */
@Slf4j
@Component
public class DecryptInterceptor implements Interceptor {

    private static final int TRY_DECRYPT_TIMES = 3;
    private static ExecutorService EXECUTOR =
            new ThreadPoolExecutor(
                    0,
                    200,
                    60L,
                    TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(),
                    Executors.defaultThreadFactory(),
                    new ThreadPoolExecutor.CallerRunsPolicy());
    @Resource private DecryptComponent decryptComponent;
    @Resource private RedisUtil redisUtil;
    @Resource private ObjectMapper objectMapper;

    @Override
    public Response intercept(Chain chain) throws IOException {

        Response proceed = chain.proceed(chain.request());
        ResponseBody body = proceed.body();
        if (ObjectUtil.isNull(body)) {
            return proceed;
        }
        LocalDateTime now = LocalDateTime.now();
        String string = body.string();
        if (!JSONUtil.isJson(string)) {
            log.warn("decrypt hmp response error, and origin data: {}", string);
            throw new DecryptException("hmp response is not json string");
        }

        JSONObject jsonObject = JSONUtil.parseObj(string);
        Object payload = jsonObject.get(PAYLOAD);
        Response clonedResponse =
                proceed.newBuilder().body(ResponseBody.create(body.contentType(), string)).build();
        if (ObjectUtil.isNull(payload)) {
            return clonedResponse;
        }

        String str = String.valueOf(payload);
        if (!JSONUtil.isJson(str)) {
            log.warn("decrypt hmp response error, and origin data: {}", string);
            throw new DecryptException("hmp response's payload is not json string");
        }

        JSONObject payloadObj = JSONUtil.parseObj(str);
        Object responseData = payloadObj.get(RESPONSE_DATA);

        if (!ObjectUtil.isNotNull(responseData)) {
            log.warn("decrypt hmp response error, and origin data: {}", string);
            throw new DecryptException(
                    "hmp payload's response_data is null, due to: " + payloadObj.get("message"));
        }

        Object decrypt;
        try {
            decrypt = tryDecrypt(responseData);
        } catch (Exception ex) {
            log.warn("decrypt hmp response error, and origin data: {}", string);
            throw ex;
        }
        payloadObj.set(RESPONSE_DATA, decrypt);
        jsonObject.set(PAYLOAD, payloadObj);

        ResponseBody decryptResponse =
                ResponseBody.create(body.contentType(), jsonObject.toString());
        log.info("decrypt cost: {}", Duration.between(LocalDateTime.now(), now));
        return proceed.newBuilder().body(decryptResponse).build();
    }

    @SneakyThrows
    private Object tryDecrypt(Object origin) {

        if (origin instanceof JSONArray) {
            JSONArray array = (JSONArray) origin;
            List<Object> list = Collections.synchronizedList(new ArrayList<>());
            List<Future<Boolean>> collect =
                    array.parallelStream()
                            .map(x -> EXECUTOR.submit(() -> list.add(tryDecrypt(x.toString()))))
                            .collect(Collectors.toList());

            for (val a : collect) {
                a.get();
            }

            return list;
        }

        return tryDecrypt(origin.toString());
    }

    private String tryDecrypt(String origin) {

        int retry = 1;
        while (retry <= TRY_DECRYPT_TIMES) {
            retry++;
            String decrypt = decryptComponent.decrypt(origin, getDecryptKey());
            if (StrUtil.isNotBlank(decrypt)) {
                return decrypt;
            }
        }

        log.error("Error while decrypting: with retry {} times.", origin);
        throw new DecryptException("Decrypting Response Error");
    }

    private String getDecryptKey() {
        return redisUtil.get(RedisHttpEnum.DECRYPT_TOKEN_KEY, String.class);
    }
}
