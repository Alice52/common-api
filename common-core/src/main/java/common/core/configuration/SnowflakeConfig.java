package common.core.configuration;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author zack <br>
 * @create 2021-06-02 12:38 <br>
 * @project custom-test <br>
 */
@Component
@Slf4j
@Deprecated
public class SnowflakeConfig {
    /** 为终端ID */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private long workerId = 0;

    /** 数据中心ID */
    private long dataCenterId = 1;

    private Snowflake snowflake = IdUtil.createSnowflake(workerId, dataCenterId);

    @PostConstruct
    public void init() {
        workerId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr());
        log.info("当前机器的workId:{}", workerId);
    }

    public synchronized long snowflakeId() {
        return snowflake.nextId();
    }

    public synchronized long snowflakeId(long workerId, long datacenterId) {
        Snowflake snowflake = IdUtil.createSnowflake(workerId, datacenterId);
        return snowflake.nextId();
    }
}
