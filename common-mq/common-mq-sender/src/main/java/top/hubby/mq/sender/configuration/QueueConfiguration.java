package top.hubby.mq.sender.configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.google.common.collect.ImmutableMap;
import top.hubby.mq.sender.configuration.props.MQProps;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * spel: https://roytuts.com/spring-conditionalonexpression-example/
 *
 * <pre>
 * 1. 默认都是延迟创建的<br>
 * 2. 重启无损: 不会报错{存在就是要, 不存在会创建}<br>
 * 3. 运行期间: 删除是不会创建的<br>
 *   - 删除 exchange: 会直接触发ConfirmCallback报错{ack为false}<br>
 *   - 删除 queue: 会触发ReturnCallback, 消息没有到 queue<br>
 * </pre>
 *
 * @author zack <br>
 * @create 2022-04-11 20:58 <br>
 * @project project-cloud-custom <br>
 */
@Configuration
public class QueueConfiguration {
    private static ImmutableMap<String, Object> deadLetterMap;
    @Resource public MQProps props;

    @PostConstruct
    protected void init() {

        /**
         * message come into dlq
         *
         * <pre>
         *   1. queue 达到最大的长度
         *   2. consumer 拒绝收消息并将设置 re-queue 为 false
         *   3. 消息过期
         * </pre>
         *
         * DL usage flow:
         *
         * <pre>
         *     1. publisher 消息先到 direct-exchange
         *     2. 根据 direct-routing-key 加入 direct-direct-queue
         *     3. consumer 消费失败后带上 dl-routing-key 作为 routingkey 发送到 dl-exchange
         *     4. 在根据 dl-routing-key 发送到 dead-dl-queue
         * </pre>
         */
        deadLetterMap =
                ImmutableMap.<String, Object>builder()
                        .put("x-dead-letter-exchange", props.getDeadLetterExchange())
                        .put("x-dead-letter-routing-key", props.getDeadLetterRoutingKey())
                        .build();
    }

    // region: dead letter
    @Bean
    public Queue deadQueue() {
        return new Queue(props.getDeadLetterQueue(), true, false, false);
    }

    @Bean
    public Exchange deadExchange() {
        return new DirectExchange(props.getDeadLetterExchange(), true, false);
    }

    @Bean
    public Binding directDeadBinding() {

        return new Binding(
                props.getDeadLetterQueue(),
                Binding.DestinationType.QUEUE,
                props.getDeadLetterExchange(),
                props.getDeadLetterRoutingKey(),
                null);
    }
    // endregion
    /**
     * can config as delay queue: arguments.put("x-message-ttl", 60_000);
     *
     * @return
     */
    @Bean
    public Queue queue() {

        return new Queue(props.getQueue(), true, false, false, deadLetterMap);
    }

    // region: direct queue
    @Bean
    @ConditionalOnExpression(value = "'${common.mq.queue-type}'.equals('direct')")
    public Exchange directExchange() {

        return new DirectExchange(props.getExchange(), true, false);
    }

    @Bean
    @ConditionalOnExpression(value = "'${common.mq.queue-type}'.equals('direct')")
    public Binding directBinding() {
        /*
         * String destination, 目的地（队列名或者交换机名字）
         * DestinationType destinationType, 目的地类型（Queue/Exhcange）
         * String exchange,
         * String routingKey,
         * Map<String, Object> arguments
         * */
        return new Binding(
                props.getQueue(),
                Binding.DestinationType.QUEUE,
                props.getExchange(),
                props.getRoutingKey(),
                null);
    }

    // endregion

    // region: topic
    @Bean
    @ConditionalOnExpression(value = "'${common.mq.queue-type}'.equals('topic')")
    public TopicExchange topicExchange() {

        return new TopicExchange(props.getExchange());
    }

    @Bean
    @ConditionalOnExpression(value = "'${common.mq.queue-type}'.equals('topic')")
    public Binding topicBinding(TopicExchange topicExchange) {
        return BindingBuilder.bind(queue()).to(topicExchange).with(props.getRoutingKey());
    }
    // endregion
}
