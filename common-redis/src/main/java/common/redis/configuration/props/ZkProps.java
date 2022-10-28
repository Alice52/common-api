package common.redis.configuration.props;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zack <br>
 * @create 2022-03-20 18:11 <br>
 * @project project-cloud-custom <br>
 */
@Data
@ConfigurationProperties(prefix = "zk")
public class ZkProps {
    /** 连接地址 */
    private String url;

    /** 超时时间(毫秒)，默认1000 */
    private int timeout = 1000;

    /** 重试次数，默认3 */
    private int retry = 3;
}
