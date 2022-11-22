package common.http.support;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.Method;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Maps;
import common.core.util.JacksonUtil;
import common.http.configuration.HttpProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.internal.http.RealResponseBody;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static cn.hutool.core.util.CharUtil.AMP;
import static common.http.constant.Constants.DECRYPT_TYPE_FLAG;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static okhttp3.MediaType.parse;

/**
 * @param <T> api response class.
 * @author asd <br>
 * @create 2021-12-07 4:46 PM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
@Component
public class HttpSupport<T> {
    private static final String QUESTION_MARK = "?";
    private static final String EQUALS = "=";
    private static HttpProperties properties;
    private static ObjectMapper objectMapper;
    private static OkHttpClient httpClient;

    @Resource
    public void setHttpClient(OkHttpClient httpClient) {
        HttpSupport.httpClient = httpClient;
    }

    @Resource
    public void setHmpProperties(HttpProperties hmpProperties) {
        HttpSupport.properties = hmpProperties;
    }

    @Resource
    public void setObjectMapper(ObjectMapper objectMapper) {
        HttpSupport.objectMapper = objectMapper;
    }

    public static String doRequest(
            Method method,
            String uri,
            Map<String, String> params,
            Map<String, String> header,
            HttpProperties.DecryptTypeEnum typeEnum) {
        return doRequest(method, null, uri, params, header, CharSequenceUtil.EMPTY, typeEnum);
    }

    public static <E> E doRequest(
            @NotNull Method method,
            @Nullable String host,
            @NotNull String uri,
            @Nullable Supplier<Map<String, String>> paramSupplier,
            @Nullable Supplier<Map<String, String>> headerSupplier,
            @NotNull Supplier<JsonNode> payloadSupplier,
            @NotNull Function<String, E> payloadParser,
            HttpProperties.DecryptTypeEnum typeEnum) {

        Map<String, String> params =
                ofNullable(paramSupplier).map(Supplier::get).orElse(Maps.newHashMap());
        Map<String, String> headerMap =
                ofNullable(headerSupplier).map(Supplier::get).orElse(Maps.newHashMap());
        String bodyStr = prepareBody(payloadSupplier.get());

        String responseBody =
                HttpSupport.doRequest(method, host, uri, params, headerMap, bodyStr, typeEnum);

        return payloadParser.apply(responseBody);
    }

    private static String prepareBody(JsonNode payload) {

        ObjectNode root = objectMapper.createObjectNode();
        JsonNode merge = JacksonUtil.merge(root, payload);

        return merge.toString();
    }

    @SneakyThrows
    public static String doRequest(
            Method method,
            String host,
            String uri,
            Map<String, String> params,
            Map<String, String> headers,
            String requestBodyStr,
            HttpProperties.DecryptTypeEnum typeEnum) {
        params = ofNullable(params).orElseGet(HashMap::new);
        headers = ofNullable(headers).orElseGet(HashMap::new);
        headers.put(DECRYPT_TYPE_FLAG, typeEnum.name());
        host = CharSequenceUtil.isBlank(host) ? properties.getHost() : host;
        String url = buildUrl(host, uri, params);
        Headers headerMap = okhttp3.Headers.of(of(headers).orElseGet(HashMap::new));

        Request request;
        if (Method.GET.equals(method)) {
            request = new Request.Builder().url(url).headers(headerMap).get().build();
        } else {
            RequestBody body = buildBody(headers, requestBodyStr);
            request = new Request.Builder().url(url).headers(headerMap).post(body).build();
        }

        try (ResponseBody response = httpClient.newCall(request).execute().body()) {
            return ofNullable(response).orElse(RealResponseBody.create(null, "")).string();
        }
    }

    private static RequestBody buildBody(
            @NotNull Map<String, String> headers, String requestBodyStr) {
        MediaType mediaType =
                parse(ofNullable(headers.get("Content-Type")).orElse("application/json"));
        return RequestBody.create(mediaType, requestBodyStr);
    }

    private static String buildUrl(String host, String uri, @NotNull Map<String, String> params) {
        StringBuilder paramStr = new StringBuilder(QUESTION_MARK);
        params.keySet()
                .forEach(x -> paramStr.append(x).append(EQUALS).append(params.get(x)).append(AMP));
        paramStr.setLength(paramStr.length() - 1);

        return String.format("%s%s%s", host, uri, paramStr);
    }
}
