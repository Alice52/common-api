package top.hubby.mq.sender;

import cn.hutool.json.JSONUtil;
import common.uid.generator.CachedUidGenerator;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import top.hubby.mq.constants.enums.EventStatus;
import top.hubby.mq.sender.configuration.props.MQProps;
import top.hubby.mq.service.DtxEventService;

import javax.annotation.Resource;

import static top.hubby.mq.sender.configuration.RabbitMqAutoConfiguration.mqSender;

/**
 * https://blog.csdn.net/xhf852963/article/details/107884528
 *
 * @see org.springframework.amqp.rabbit.connection.PublisherCallbackChannel
 * @author zack <br>
 * @create 2022-04-11 20:51 <br>
 * @project project-cloud-custom <br>
 */
@Service
@EnableConfigurationProperties(MQProps.class)
public class SenderService {

    @Resource private MQProps props;
    @Resource private CachedUidGenerator uidGenerator;
    @Resource private DtxEventService dtxService;

    public void convertAndSend(final Object message) throws AmqpException {
        long uid = uidGenerator.getUID();
        dtxService.createEvent(uid, "default", JSONUtil.toJsonStr(message), EventStatus.NEW);
        convertAndSend(message, uid);
    }

    public void convertAndSend(final Object message, Long uid) throws AmqpException {

        mqSender.convertAndSend(
                props.getExchange(),
                props.getRoutingKey(),
                message,
                m -> {
                    m.getMessageProperties().setCorrelationId(uid.toString());
                    return m;
                },
                new CorrelationData(uid.toString()));
    }
}
