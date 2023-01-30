package top.hubby.mq.listerner;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.rabbitmq.client.Channel;
import common.core.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;

@Slf4j
@Component
public class OrderConsumerListener
        implements ChannelAwareMessageListener, ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(
            @Nonnull ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /** 保存用户订单 */
    @RabbitListener(queues = "ORDER", containerFactory = "orderListenerFactory")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        String orderMessage = new String(message.getBody());
        log.info("receive order message from rabbit queue : {}", orderMessage);
        if (StringUtils.isBlank(orderMessage)) {
            // 消费成功
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), Boolean.FALSE);
            return;
        }

        // 消息预取: 消息转发到队列后，分配是提前一次性完成的，
        // 即 RabbitMQ 尽可能快速地将消息推送至客户端，由客户端缓存本地，而并非在消息消费时才逐一确定
        channel.basicQos(26);

        String orderNo = null;
        try {
            // 1. 解析: 解析不了则直接确认消费(活丢入异常队列)
            OrderMessageDTO orderMessageDTO = JSONUtil.toBean(orderMessage, OrderMessageDTO.class);
            if (null == orderMessageDTO) {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), Boolean.FALSE);
                return;
            }

            // 2. 直接确认, 不消费的场景

            // 3. 去重: 查询该订单是否存在
            Order existOrder = getFromDb(orderNo);
            if (null != existOrder) {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), Boolean.FALSE);
                return;
            }

            // 4. 消费消息: 落库 + 发布订单事件
            boolean consumeSuccess = true;
            // 失败则抛出异常: throw new BusinessException()
            if (!consumeSuccess) {
                throw new BusinessException(null);
            }
            //      成功则确认 ack: basicAck
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), Boolean.FALSE);

        } catch (DuplicateKeyException e) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), Boolean.FALSE);
        } catch (Exception e) {
            log.error("consume order message from rabbit queue error, orderNo : {}", orderNo, e);
            // 消费失败的消息重新入队列
            channel.basicNack(
                    message.getMessageProperties().getDeliveryTag(), Boolean.FALSE, Boolean.TRUE);
            // 抛异常回滚数据
            throw new BusinessException(null);
        }
    }

    private Order getFromDb(String orderNo) {
        return null;
    }
}

class OrderMessageDTO {}

class Order {}
