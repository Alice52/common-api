package top.hubby.test.custom.controller;

import common.core.controller.BaseHealthCheckController;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author alice52
 * @date 2023/11/20
 * @project common-api
 */
@Slf4j
@RestController
public class HealthCheckController extends BaseHealthCheckController {

    @Resource private RedisTemplate stringRedisTemplate;

    @GetMapping
    public HealthResult healthCheck() {

        HealthResult healthResult = super.healthCheck();
        healthResult.setKvStatus(rdsStatus().name());

        return healthResult;
    }

    protected HealthEnum rdsStatus() {
        try {
            stringRedisTemplate.execute(
                    (RedisCallback<String>) connection -> new String(connection.ping()));
            return HealthEnum.UP;
        } catch (Exception e) {
            log.debug("health check error: ", e);
            return HealthEnum.DOWN;
        }
    }
}
