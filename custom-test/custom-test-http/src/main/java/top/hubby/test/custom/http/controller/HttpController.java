package top.hubby.test.custom.http.controller;

import common.core.model.Pagination;
import common.core.util.R;
import common.http.model.TokenVO;
import common.http.service.TokenService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hubby.test.custom.http.service.DecryptService;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * todo: @Http(Method, Host, xxx)
 *
 * @author asd <br>
 * @create 2021-12-07 5:22 PM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
@Api(tags = {"Http Module Api"})
@RestController
@RequestMapping("/custom/http")
public class HttpController {
    @Resource private TokenService tokenService;
    @Resource private DecryptService decryptService;

    @GetMapping("/token")
    public @NotNull R<TokenVO> token() {
        return R.success(tokenService.token());
    }

    @GetMapping("/full-encrypt")
    public R<Pagination> fullEncrypt() {
        return decryptService.fullEncrypt();
    }

    @GetMapping("/full-encrypt-v2")
    public R<Pagination> fullEncryptV2() {
        return decryptService.fullEncryptV2();
    }

    @Deprecated
    @GetMapping("/party-encrypt")
    public top.hubby.test.custom.http.model.R<Pagination> partyEncrypt() {
        return decryptService.partyEncrypt();
    }
}
