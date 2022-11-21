package top.hubby.openapi.util;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.naming.OperationNotSupportedException;
import javax.servlet.http.HttpServletRequest;

import cn.hutool.json.JSONUtil;
import common.core.exception.BaseException;
import common.core.util.ee.WebUtil;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static common.core.constant.RequestConstants.APPID;
import static common.core.constant.RequestConstants.NONCESTR;
import static common.core.constant.RequestConstants.SIGNATURE;
import static common.core.constant.RequestConstants.TIMESTAMP;
import static common.core.constant.enums.CommonResponseEnum.OPENAPI_SIGNATURE_ERROR;
import static top.hubby.openapi.configuration.OpenApiConfiguration.APP_MAP;

/**
 * @author asd <br>
 * @create 2022-04-06 1:48 PM <br>
 * @project mc-middleware-api <br>
 */
@Slf4j
@UtilityClass
public final class OpenApiSecureUtil {

    @SneakyThrows
    public static Map<String, String> buildHeader(
            String appId, String secret, String uri, String body) {
        throw new OperationNotSupportedException();
    }

    public static void validateSignature(HttpServletRequest request) {
        String appId = request.getHeader(APPID);
        String secret = APP_MAP.get(appId);
        validateSignature(request, appId, secret);
    }

    public static void validateSignature(HttpServletRequest request, String appId, String secret) {
        String signature = request.getHeader(SIGNATURE);
        String timestamp = request.getHeader(TIMESTAMP);
        String noncestr = request.getHeader(NONCESTR);
        String uri = WebUtil.getFullUri(request);
        String body = WebUtil.getBody(request);
        validateSignature(signature, uri, body, timestamp, noncestr, appId, secret);
    }

    private static void validateSignature(
            String signature,
            String uri,
            String body,
            String timestamp,
            String noncestr,
            String appId,
            String secret) {

        String newSignature = generateSignature(uri, body, timestamp, noncestr, appId, secret);
        long interval = Instant.now().toEpochMilli() - Long.parseLong(timestamp);
        if (!signature.equals(newSignature) || TimeUnit.MILLISECONDS.toMinutes(interval) > 5) {
            throw new BaseException(OPENAPI_SIGNATURE_ERROR);
        }
    }

    public static String generateSignature(HttpServletRequest request) {
        String timestamp = request.getHeader(TIMESTAMP);
        String noncestr = request.getHeader(NONCESTR);
        String uri = WebUtil.getFullUri(request);
        String body = WebUtil.getBody(request);
        String appId = request.getHeader(APPID);
        String secret = APP_MAP.get(appId);
        return generateSignature(uri, body, String.valueOf(timestamp), noncestr, appId, secret);
    }

    public static String generateSignature(
            String uri,
            String body,
            long timestamp,
            String noncestr,
            String appId,
            String appSecret) {

        return generateSignature(uri, body, String.valueOf(timestamp), noncestr, appId, appSecret);
    }

    public static String generateSignature(
            String uri,
            String body,
            String timestamp,
            String noncestr,
            String appId,
            String appSecret) {

        ArrayList<Object> list = new ArrayList<>();
        list.add(uri);
        list.add(body);
        list.add(String.valueOf(timestamp));
        list.add(noncestr);
        list.add(appId);
        list.add(appSecret);

        return cn.hutool.crypto.SecureUtil.md5(JSONUtil.toJsonStr(list));
    }
}
