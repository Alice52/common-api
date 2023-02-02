package custom.test.logging.controller;

import common.core.util.R;
import common.logging.anno.LogAnno;
import common.logging.anno.LogAnnoV2;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@Api(tags = {"Log Module Api"})
@RestController
@RequestMapping("/log")
public class LogController {

    @Resource private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @LogAnno
    @GetMapping("/anno")
    public R<String> ping() {
        return R.<String>builder().data("pong").build();
    }

    @LogAnnoV2
    @PostMapping("/anno2")
    public R<String> ping2() {

        log.info("idcard: {}", "320324199910111023");
        log.info("email: {}, phone: {}", "123456789@qq.com", "15310763497");
        log.info("username: {}", "15310763497");

        threadPoolTaskExecutor.execute(() -> log.info("sub-thread log"));

        return R.<String>builder().data("pong").build();
    }
}
