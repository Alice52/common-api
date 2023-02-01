package common.core.util.web;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import common.core.exception.CheckedException;
import common.core.util.se.ClassUtil;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;

import javax.annotation.Nullable;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

import static cn.hutool.json.JSONUtil.isJson;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.StringUtils.startsWithIgnoreCase;

/**
 * @author zack <br>
 * @create 2021-06-01<br>
 * @project project-custom <br>
 */
@Slf4j
@UtilityClass
public class WebUtil extends org.springframework.web.util.WebUtils {
    private static final String BASIC_ = "Basic ";
    private static final String UNKNOWN = "unknown";
    private static final String BEARER_ = "Bearer ";
    private static final int MAX_LENGTH = 15;

    public HttpServletRequest getCurrentRequest() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        return sra.getRequest();
    }

    public static HttpServletResponse getCurrentResponse() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        return sra.getResponse();
    }

    // region: header
    public Map<String, Object> getHeaders(HttpServletRequest request) {
        Map<String, Object> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (ObjectUtil.isNotNull(headerNames) && headerNames.hasMoreElements()) {
            String hv = headerNames.nextElement();
            headers.put(hv, request.getHeader(hv));
        }

        return headers;
    }

    @Nullable
    public static String getCurrentToken() {
        HttpServletRequest request = getCurrentRequest();
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith(BEARER_)) {
            return null;
        }

        return header.replace(BEARER_, "");
    }

    @Nullable
    public String getIpAddr() {
        return getIpAddr(getCurrentRequest());
    }

    /**
     * Gets the IP address of the login user's remote host
     *
     * @param request
     * @return
     */
    @Nullable
    public String getIpAddr(HttpServletRequest request) {

        String ip = null;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (StrUtil.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StrUtil.isEmpty(ip) || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StrUtil.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StrUtil.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StrUtil.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } catch (Exception e) {
            log.error("getIpAddr ERROR ", e);
        }

        // 使用代理，则获取第一个IP地址
        if (!StrUtil.isEmpty(ip) && ip.length() > MAX_LENGTH && ip.indexOf(StrUtil.COMMA) > 0) {
            ip = ip.substring(0, ip.indexOf(StrUtil.COMMA));
        }

        return ip;
    }

    /**
     * Get client-id from request
     *
     * @return
     */
    @SneakyThrows
    public static String[] getClientId(ServerHttpRequest request) {
        String header = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith(BASIC_)) {
            throw new CheckedException("请求头中client信息为空");
        }

        byte[] base64Token = header.substring(6).getBytes("UTF-8");
        byte[] decoded;
        try {
            decoded = Base64.decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new CheckedException("Failed to decode basic authentication token");
        }

        String token = new String(decoded, StandardCharsets.UTF_8);

        int delim = token.indexOf(":");

        if (delim == -1) {
            throw new CheckedException("Invalid basic authentication token");
        }
        return new String[] {token.substring(0, delim), token.substring(delim + 1)};
    }
    // endregion

    /**
     * 判断是否ajax请求 spring ajax 返回含有 ResponseBody 或者 RestController注解
     *
     * @param handlerMethod HandlerMethod
     * @return 是否ajax请求
     */
    public static boolean hasBody(HandlerMethod handlerMethod) {
        ResponseBody responseBody = ClassUtil.getAnnotation(handlerMethod, ResponseBody.class);
        return responseBody != null;
    }

    // region: cookie
    public static String getCookieVal(String name) {
        HttpServletRequest request = WebUtil.getCurrentRequest();
        Assert.notNull(request, "request from RequestContextHolder is null");
        return getCookieVal(request, name);
    }

    public static String getCookieVal(HttpServletRequest request, String name) {
        Cookie cookie = getCookie(request, name);
        return cookie != null ? cookie.getValue() : null;
    }

    public static void removeCookie(HttpServletResponse response, String key) {
        setCookie(response, key, null, 0);
    }

    public static void setCookie(
            HttpServletResponse response, String name, String value, int maxAgeInSeconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAgeInSeconds);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
    // endregion

    // region: req-id
    public String getRequestId(HttpServletRequest request, String requestIdKey) {
        String requestId;
        String parameterRequestId = request.getParameter(requestIdKey);
        String headerRequestId = request.getHeader(requestIdKey);
        String mdcRequestId = MDC.get(requestIdKey);
        if (parameterRequestId == null && headerRequestId == null && mdcRequestId == null) {
            requestId = IdUtil.fastUUID();
            // request.setAttribute(requestIdKey, requestId);
        } else {
            requestId = StrUtil.firstNonBlank(parameterRequestId, headerRequestId, mdcRequestId);
        }
        return requestId;
    }
    // endregion

    // region:params & body
    public Map<String, ?> getParams(HttpServletRequest request) {

        return request.getParameterMap();
    }

    public String getBody(HttpServletRequest request) {
        try {
            return readInternal(request);
        } catch (Exception ex) {
            log.error("get request body failed: ", ex);
        }

        return StrUtil.EMPTY;
    }

    protected String readInternal(HttpServletRequest request) throws IOException {
        ServletServerHttpRequest inputMessage = new ServletServerHttpRequest(request);
        Charset charset = getContentTypeCharset(inputMessage.getHeaders().getContentType());
        return StreamUtils.copyToString(inputMessage.getBody(), charset);
    }

    private Charset getContentTypeCharset(
            @org.springframework.lang.Nullable MediaType contentType) {

        if (contentType != null && contentType.getCharset() != null) {
            return contentType.getCharset();
        }

        return StandardCharsets.UTF_8;
    }

    /**
     * 计算请求参数的 MD5.
     *
     * <pre>
     *     1. MD5 理论上可能会重复, 但是去重通常是短时间窗口内的去重[例如一秒]
     *     2. 一个短时间内同一个用户同样的接口能拼出不同的参数导致一样的 MD5 几乎是不可能的
     * </pre>
     *
     * @param request
     * @param excludeKeys
     * @return
     */
    public String deDupParamMD5(HttpServletRequest request, String... excludeKeys) {

        Map<String, String[]> paramMap = request.getParameterMap();
        if (excludeKeys != null) {
            List<String> dedupExcludeKeys = Arrays.asList(excludeKeys);
            if (!dedupExcludeKeys.isEmpty()) {
                for (String dedupExcludeKey : dedupExcludeKeys) {
                    paramMap.remove(dedupExcludeKey);
                }
            }
        }

        paramMap.put("username", new String[] {WebUtil.getCurrentToken()});
        paramMap.put("uri", new String[] {request.getRequestURI()});
        String md5deDupParam = jdkMD5(JSONUtil.toJsonStr(paramMap));

        log.debug("md5deDupParam = {}, excludeKeys = {} {}", md5deDupParam, excludeKeys, paramMap);

        return md5deDupParam;
    }

    /**
     * todo: this method is complexed
     *
     * <pre>
     *     1. WebUtil.getBody(request): this is need repeatable request.
     *     2. excludeKeys need build method by self.
     * </pre>
     *
     * @param request
     * @param excludeKeys
     * @return
     */
    public String deDupParamMD5_V2(HttpServletRequest request, String... excludeKeys) {
        Map<String, String[]> paramMap = new HashMap<>();
        paramMap.put("username", new String[] {WebUtil.getCurrentToken()});
        paramMap.put("uri", new String[] {request.getRequestURI()});
        if (request.getMethod().equals(Method.GET.name())) {
            paramMap.putAll(request.getParameterMap());
        } else {
            String body = WebUtil.getBody(request);
            if (isJson(body)
                    && startsWithIgnoreCase(request.getContentType(), APPLICATION_JSON_VALUE)) {
                JSONObject jsonObject = JSONUtil.parseObj(body);
                if (excludeKeys != null) {
                    List<String> dedupExcludeKeys = Arrays.asList(excludeKeys);
                    if (!dedupExcludeKeys.isEmpty()) {
                        for (String dedupExcludeKey : dedupExcludeKeys) {
                            jsonObject.remove(dedupExcludeKey);
                        }
                    }
                }
            } else {
                paramMap.put("body", new String[] {body});
            }
        }

        if (excludeKeys != null) {
            List<String> dedupExcludeKeys = Arrays.asList(excludeKeys);
            if (!dedupExcludeKeys.isEmpty()) {
                for (String dedupExcludeKey : dedupExcludeKeys) {
                    paramMap.remove(dedupExcludeKey);
                }
            }
        }

        String md5deDupParam = jdkMD5(JSONUtil.toJsonStr(paramMap));

        log.debug("md5deDupParam = {}, excludeKeys = {} {}", md5deDupParam, excludeKeys, paramMap);

        return md5deDupParam;
    }

    private String jdkMD5(String src) {
        String res = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] mdBytes = messageDigest.digest(src.getBytes());
            res = DatatypeConverter.printHexBinary(mdBytes);
        } catch (Exception e) {
            log.error("", e);
        }
        return res;
    }
    // endregion

    public String getFullUri(HttpServletRequest request) {

        String prefix = request.getHeader("X-Forwarded-Prefix");
        String uri = request.getRequestURI();
        if (StrUtil.isNotBlank(prefix)) {
            uri = prefix + uri;
        }

        return uri;
    }
}
