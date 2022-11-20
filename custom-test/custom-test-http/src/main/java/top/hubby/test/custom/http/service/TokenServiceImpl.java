package top.hubby.test.custom.http.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.http.configuration.HttpProperties;
import common.http.model.TokenVO;
import common.http.service.TokenService;
import common.http.service.impl.DefaultTokenService;
import common.redis.utils.RedisUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.naming.OperationNotSupportedException;

/**
 * @author asd <br>
 * @create 2021-12-07 4:55 PM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
@Component
@Primary
public class TokenServiceImpl extends TokenService {

    @Resource protected HttpProperties httpProperties;
    @Resource protected RedisUtil redisUtil;
    @Resource protected ObjectMapper objectMapper;

    @Resource protected DefaultTokenService tokenService;

    @SneakyThrows
    @Override
    public void refreshToken() {
        throw new OperationNotSupportedException();
    }

    @SneakyThrows
    @Override
    public TokenVO token() {

        return tokenService.token();
    }

    @SneakyThrows
    @Override
    public TokenVO refreshDecryptToken() {
        return tokenService.refreshDecryptToken();
    }
}
