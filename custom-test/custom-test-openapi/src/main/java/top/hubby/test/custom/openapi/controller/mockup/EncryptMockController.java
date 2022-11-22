package top.hubby.test.custom.openapi.controller.mockup;

import com.google.common.collect.Lists;
import common.core.exception.BusinessException;
import common.core.model.Pagination;
import common.core.util.R;
import common.http.component.DecryptComponent;
import io.swagger.annotations.Api;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    Pagination<Object> build1 =
            Pagination.builder().currentPage(1).total(100).pageCount(5).pageSize(20).build();
    Pagination<Object> build2 =
            Pagination.builder().currentPage(1).total(100).pageCount(5).pageSize(20).build();
    List<Pagination> records = Lists.newArrayList(build1, build2);
    Pagination<Pagination> data =
            Pagination.<Pagination>builder()
                    .currentPage(1)
                    .total(100)
                    .pageCount(5)
                    .pageSize(20)
                    .records(records)
                    .build();

    @SneakyThrows
    @GetMapping("/encrypted/full")
    public String full(@RequestParam("clientId") String clientId) {

        if (!APP_MAP.containsKey(clientId)) {
            throw new BusinessException(CLIENT_ID_INVALID);
        }

        String secret = APP_MAP.get(clientId);

        R<Pagination> r = new R<>(data);

        String encrypt = DecryptComponent.encrypt(r, secret);
        log.info("encrypted data: {}", encrypt);
        String decrypt = DecryptComponent.decrypt(encrypt, secret);
        log.info("decrypt data: {}", decrypt);

        return encrypt;
    }

    @SneakyThrows
    @GetMapping("/encrypted/party")
    public R<String> party(@RequestParam("clientId") String clientId) {
        if (!APP_MAP.containsKey(clientId)) {
            throw new BusinessException(CLIENT_ID_INVALID);
        }
        String secret = APP_MAP.get(clientId);

        String encrypt = DecryptComponent.encrypt(data, secret);
        log.info("encrypted data: {}", encrypt);
        String decrypt = DecryptComponent.decrypt(encrypt, secret);
        log.info("decrypt data: {}", decrypt);

        return R.success(encrypt);
    }
}
