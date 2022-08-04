package top.hubby.mq.sender.configuration.props;

import lombok.Data;
import top.hubby.mq.constants.enums.MQQueueTypeEnums;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zack <br>
 * @create 2022-04-11 21:05 <br>
 * @project project-cloud-custom <br>
 */
@Data
@ConfigurationProperties(prefix = "common.mq")
public class MQProps {
    private String deadLetterQueue;
    private String deadLetterExchange;
    private String deadLetterRoutingKey;

    private MQQueueTypeEnums queueType;
    private String exchange;
    private String queue;
    private String routingKey;
}
