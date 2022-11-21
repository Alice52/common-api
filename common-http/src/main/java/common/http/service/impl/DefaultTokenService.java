package common.http.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Method;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import common.http.configuration.HttpProperties;
import common.http.constant.enums.RedisHttpEnum;
import common.http.model.TokenVO;
import common.http.service.TokenService;
import common.http.support.HttpSupport;
import common.redis.utils.RedisUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.naming.OperationNotSupportedException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static common.http.constant.Constants.*;

/**
 * @author asd <br>
 * @create 2021-12-07 4:55 PM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
@Component
public class DefaultTokenService extends TokenService {

    @Resource protected HttpProperties p;
    @Resource protected RedisUtil redisUtil;
    @Resource protected ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public void refreshToken() {
        throw new OperationNotSupportedException();
    }

    @Override
    public TokenVO token() {

        TokenVO token =
                token(
                        p.getAccessTokenUrl(),
                        p.getGrantType(),
                        p.getClientId(),
                        p.getClientSecret(),
                        TokenVO.class);

        if (token != null && StrUtil.isNotEmpty(token.getAccessToken())) {
            redisUtil.set(
                    RedisHttpEnum.TOKEN_KEY,
                    token.getAccessToken(),
                    token.getExpiresIn(),
                    TimeUnit.SECONDS);
        }

        return token;
    }

    @Override
    public TokenVO refreshDecryptToken() {
        TokenVO token =
                token(
                        p.getDecryptTokenUrl(),
                        p.getGrantType(),
                        p.getClientId(),
                        p.getClientSecret(),
                        TokenVO.class);

        if (token != null && StrUtil.isNotEmpty(token.getAccessToken())) {
            redisUtil.set(
                    RedisHttpEnum.DECRYPT_TOKEN_KEY,
                    token.getAccessToken(),
                    token.getExpiresIn(),
                    TimeUnit.SECONDS);
        }

        return token;
    }

    @SneakyThrows
    private <T> T token(String url, String type, String id, String secret, Class<T> clazz) {
        Map<String, String> params =
                ImmutableMap.<String, String>builder()
                        .put(GRANT_TYPE, type)
                        .put(CLIENT_ID, id)
                        .put(CLIENT_SECRET, secret)
                        .build();

        Map<String, String> headers = ImmutableMap.of(API_AUTH_FLAG, Boolean.TRUE.toString());
        String response = HttpSupport.doRequest(Method.GET, p.getHost() + url, params, headers);
        return objectMapper.readValue(response, clazz);
    }
}
