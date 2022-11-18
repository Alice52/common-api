package top.hubby.test.custom.controller;

import common.annotation.LogAnno;
import common.core.util.R;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zack <br>
 * @create 2021-06-01 18:28 <br>
 * @project custom-test <br>
 */
@Api(tags = {"Health Check Api"})
@RestController
@RequestMapping("/health")
@Slf4j
public class PingController {
    @LogAnno
    @GetMapping("/ping")
    public R<String> ping() {
        return R.<String>builder().data("pong").build();
    }
}
