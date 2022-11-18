package top.hubby.test.custom.openapi.controller;

import common.core.util.R;
import common.redis.annotation.RedisLimitRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.hubby.openapi.annotation.OpenApiLog;
import top.hubby.openapi.annotation.OpenApiSignature;

/**
 * @author zack <br>
 * @create 2022-04-08 20:39 <br>
 * @project project-cloud-custom <br>
 */
@RestController
@Slf4j
@RequestMapping("/openapi")
@Api(value = "第三方接口模块", tags = "第三方接口模块")
public class OpenApiTestController {

    @OpenApiLog
    @OpenApiSignature
    @RedisLimitRequest(count = 200)
    @ApiOperation(value = "新增帖子/文章")
    @PostMapping("/test")
    public R<String> save(@RequestBody String body) {

        return new R<>("success");
    }
}
