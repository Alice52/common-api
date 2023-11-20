package common.core.controller;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @see custom-syntax#HealthCheckControllerTest
 * @create 2023-05-11 1:17 PM <br>
 * @project common-core <br>
 */
@Slf4j
@RequestMapping("/actuator/health")
public class BaseHealthCheckController {

    protected HealthResult healthCheck() {

        HealthResult healthResult = new HealthResult();
        healthResult.setAppStatus(HealthEnum.UP.name());

        try {
            // jdbcTemplate.execute("SELECT 1 FROM pg_database LIMIT 1");
            healthResult.setRdsStatus(HealthEnum.UP.name());
        } catch (Exception e) {
            log.debug("health check error: ", e);
            healthResult.setRdsStatus(HealthEnum.DOWN.name());
        }

        return healthResult;
    }

    @Getter
    public enum HealthEnum {
        UP,
        DOWN;
    }

    @Data
    public static class HealthResult {
        private String appStatus;
        private String rdsStatus;
        private String kvStatus;
    }
}
