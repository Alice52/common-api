package top.hubby.test.custom.http.controller;

import common.core.util.R;
import common.http.model.TokenVO;
import common.http.service.BusinessService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * @author asd <br>
 * @create 2021-12-07 5:22 PM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
@Api(tags = {"Http Module Api"})
@RestController
@RequestMapping("/custom/http")
public class HttpController {
    @Resource private BusinessService businessService;

    @GetMapping("/token")
    public @NotNull R<TokenVO> token() {
        return R.success(businessService.token());
    }
}
