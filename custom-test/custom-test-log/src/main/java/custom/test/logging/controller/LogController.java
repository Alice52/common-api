package custom.test.logging.controller;

import static common.logging.desensitize.v3.DesensitizeMarkerFactory.NAME_SENSITIVE_MARKER;

import common.core.constant.enums.BusinessResponseEnum;
import common.core.exception.BusinessException;
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

    @GetMapping("/marker")
    public void markerDesensitize() {
        log.info(NAME_SENSITIVE_MARKER, "name: {}", "zack");
    }

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

    @LogAnnoV2
    @PostMapping("/runtime-exception")
    public R<String> exception() {
        throw new RuntimeException("runtime-exception");
    }

    @LogAnnoV2
    @PostMapping("/biz-exception")
    public R<String> bizException() {
        throw new BusinessException(BusinessResponseEnum.PARAMTER_ERROR);
    }
}
