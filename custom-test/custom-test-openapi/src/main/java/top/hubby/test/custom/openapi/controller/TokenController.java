package top.hubby.test.custom.openapi.controller;

import common.core.exception.BusinessException;
import common.core.util.R;
import common.http.model.TokenVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static top.hubby.openapi.configuration.OpenApiConfiguration.APP_MAP;
import static top.hubby.test.custom.openapi.constants.OpenApiResponseEnum.CLIENT_ID_INVALID;

/**
 * @author zack <br>
 * @create 2022-04-08 20:38 <br>
 * @project custom-test-openapi <br>
 */
@Slf4j
@RestController
@RequestMapping("/openapi")
@Api(value = "提供token", tags = "Token")
public class TokenController {

    @GetMapping("/access-token")
    public R<TokenVO> token(@RequestParam("clientId") String clientId) {

        if (!APP_MAP.containsKey(clientId)) {
            throw new BusinessException(CLIENT_ID_INVALID);
        }

        TokenVO token =
                TokenVO.builder()
                        .accessToken(APP_MAP.get(clientId))
                        .tokenType("grant_type")
                        .expiresIn((long) (30 * 24 * 60 * 60))
                        .build();

        return new R<>(token);
    }
}
