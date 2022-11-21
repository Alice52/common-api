package top.hubby.test.custom.openapi.controller.mockup;

import common.core.exception.BusinessException;
import common.core.model.Pagination;
import common.core.util.R;
import common.http.component.DecryptComponent;
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
@RequestMapping("/openapi/mockup")
@Api(value = "Mockup For Http Test", tags = "Mockup Service")
public class EncryptMockController {

    // this is origin data.
    R<Pagination> data =
            new R<>(
                    Pagination.builder()
                            .currentPage(1)
                            .total(100)
                            .pageCount(5)
                            .pageSize(20)
                            .build());

    @GetMapping("/encrypted/full")
    public String token(@RequestParam("clientId") String clientId) {

        if (!APP_MAP.containsKey(clientId)) {
            throw new BusinessException(CLIENT_ID_INVALID);
        }

        String secret = APP_MAP.get(clientId);
        String encrypt = DecryptComponent.encrypt(data, secret);
        log.info("encrypted data: {}", encrypt);
        String decrypt = DecryptComponent.decrypt(encrypt, secret);
        log.info("decrypt data: {}", decrypt);

        return encrypt;
    }
}
