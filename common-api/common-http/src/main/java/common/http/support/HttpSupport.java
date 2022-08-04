package common.http.support;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import common.http.exception.HttpException;
import common.http.model.HttpProperties;
import common.http.model.PageResponseVO;
import common.http.model.PageVO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.internal.http.RealResponseBody;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static cn.hutool.core.util.CharUtil.AMP;
import static common.http.constant.Constants.*;
import static okhttp3.MediaType.parse;

/**
 * @author asd <br>
 * @create 2021-12-07 4:46 PM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
@Component
public class HttpSupport {
    private static final String QUESTION_MARK = "?";
    private static final String EQUALS = "=";
    private static HttpProperties httpProperties;
    private static ObjectMapper objectMapper;
    private static OkHttpClient httpClient;

    public static String doPost(
            String url, Map<String, String> params, Map<String, String> header) {
        return doPost(url, params, header, StrUtil.EMPTY);
    }

    @SneakyThrows
    public static String doPost(
            String url,
            Map<String, String> params,
            Map<String, String> headers,
            String requestBodyStr) {
        url = buildUrl(url, params);
        RequestBody body = buildBody(headers, requestBodyStr);
        Headers headerMap = okhttp3.Headers.of(headers);
        Request request = new Request.Builder().url(url).headers(headerMap).post(body).build();

        ResponseBody response = httpClient.newCall(request).execute().body();
        return Optional.ofNullable(response).orElse(RealResponseBody.create(null, "")).string();
    }

    public static <E> PageResponseVO<E> mwPageRequest(
            @NotNull String urlServerCode,
            @NotNull String interfaceCode,
            @NotNull Supplier<JsonNode> payloadSupplier,
            @NotNull Function<String, E> payloadParser) {

        return mwPageRequest(
                urlServerCode, interfaceCode, null, null, payloadSupplier, payloadParser);
    }

    public static <E> PageResponseVO<E> mwPageRequest(
            @NotNull String urlServerCode,
            @NotNull String interfaceCode,
            @Nullable Supplier<Map<String, String>> paramSupplier,
            @Nullable Supplier<Map<String, String>> headerSupplier,
            @NotNull Supplier<JsonNode> payloadSupplier,
            @NotNull Function<String, E> payloadParser) {

        String realUrl = getRealUrl(urlServerCode);
        Map<String, String> paramMap =
                Optional.ofNullable(paramSupplier)
                        .map(Supplier::get)
                        .orElse(Collections.emptyMap());
        Map<String, String> headerMap =
                Optional.ofNullable(headerSupplier)
                        .map(Supplier::get)
                        .orElse(Collections.emptyMap());
        String bodyStr = prepareBody(interfaceCode, payloadSupplier.get());

        String responseBody = HttpSupport.doPost(realUrl, paramMap, headerMap, bodyStr);

        PageResponseVO<E> response = doParseResponse(responseBody, payloadParser);
        if (!isHttpSuccess(response)) {
            log.info("assert request failed, response detail is {}", response);
            throw new HttpException("assert request failed due to response payload.");
        }

        return response;
    }

    private static <E> boolean isHttpSuccess(PageResponseVO<E> response) {

        PageVO<E> payload = response.getPayload();
        if (ObjectUtil.isNull(payload)) {
            return true;
        }

        return SUCCESS.equalsIgnoreCase(payload.getStatus());
    }

    private static <E> PageResponseVO<E> doParseResponse(
            String responseBody, Function<String, E> parser) {
        Assert.isTrue(JSONUtil.isJson(responseBody), "hmp response is not json string");

        PageResponseVO<E> responseVO = JSONUtil.toBean(responseBody, PageResponseVO.class);
        JSONObject jsonObject = JSONUtil.parseObj(responseBody);
        Object payload = jsonObject.get(PAYLOAD);
        String payloadStr = String.valueOf(payload);
        if (ObjectUtil.isNull(payload) || !JSONUtil.isJson(payloadStr)) {
            log.warn("unknown payload from hmp: [{}]", payload);
            throw new HttpException("Invalid Payload Data");
        }

        PageVO<E> payloadVO = responseVO.getPayload();
        List<E> collect =
                payloadVO.getResponseData().parallelStream()
                        .map(x -> parser.apply(x.toString()))
                        .collect(Collectors.toList());
        payloadVO.setResponseData(collect);

        return responseVO;
    }

    private static String prepareBody(String interfaceCode, JsonNode payload) {

        ObjectNode root = objectMapper.createObjectNode();

        return root.toString();
    }

    private static String getRealUrl(String urlServerCode) {

        return new StringBuilder(httpProperties.getHost())
                .append(URL_PREFIX)
                .append(urlServerCode)
                .append(URL_SUFFIX)
                .toString();
    }

    private static RequestBody buildBody(Map<String, String> headers, String requestBodyStr) {
        MediaType mediaType =
                parse(Optional.ofNullable(headers.get("Content-Type")).orElse("application/json"));
        return RequestBody.create(mediaType, requestBodyStr);
    }

    private static String buildUrl(String url, Map<String, String> params) {
        StringBuilder paramStr = new StringBuilder(QUESTION_MARK);
        params.keySet()
                .forEach(x -> paramStr.append(x).append(EQUALS).append(params.get(x)).append(AMP));
        paramStr.setLength(paramStr.length() - 1);

        return url + paramStr.toString();
    }

    @Resource
    public void setHttpClient(OkHttpClient httpClient) {
        HttpSupport.httpClient = httpClient;
    }

    @Resource
    public void setHmpProperties(HttpProperties hmpProperties) {
        HttpSupport.httpProperties = hmpProperties;
    }

    @Resource
    public void setObjectMapper(ObjectMapper objectMapper) {
        HttpSupport.objectMapper = objectMapper;
    }
}
