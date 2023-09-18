package top.hubby.mq.sender.configuration;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static top.hubby.mq.constants.RabbitMQConstants.*;

/**
 * RabbitMQ配置，主要是配置队列，如果提前存在该队列，可以省略本配置类<br>
 * 默认都是延迟创建的
 *
 * @author zack <br>
 * @create 2022-04-11 19:55 <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
@Configuration
public class DefaultQueueConfig {

    /** 直接模式队列1 */
    @Bean
    public Queue directOneQueue() {
        return new Queue(DIRECT_MODE_QUEUE_ONE);
    }

    /** 队列2 */
    @Bean
    public Queue queueTwo() {
        return new Queue(QUEUE_TWO);
    }

    /** 队列3 */
    @Bean
    public Queue queueThree() {
        return new Queue(QUEUE_THREE);
    }

    // region: 广播
    /** 分列模式队列 */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_MODE_QUEUE);
    }

    /**
     * 分列模式绑定队列1
     *
     * @param directOneQueue 绑定队列1
     * @param fanoutExchange 分列模式交换器
     */
    @Bean
    public Binding fanoutBinding1(Queue directOneQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(directOneQueue).to(fanoutExchange);
    }

    /**
     * 分列模式绑定队列2
     *
     * @param queueTwo 绑定队列2
     * @param fanoutExchange 分列模式交换器
     */
    @Bean
    public Binding fanoutBinding2(Queue queueTwo, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queueTwo).to(fanoutExchange);
    }

    // endregion

    // region: topic
    /**
     * 主题模式队列
     * <li>路由格式必须以 . 分隔，比如 user.email 或者 user.aaa.email
     * <li>通配符 * ，代表一个占位符，或者说一个单词，比如路由为 user.*，那么 user.email 可以匹配，但是 user.aaa.email 就匹配不了
     * <li>通配符 # ，代表一个或多个占位符，或者说一个或多个单词，比如路由为 user.#，那么 user.email 可以匹配，user.aaa.email 也可以匹配
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_MODE_QUEUE);
    }

    /**
     * 主题模式绑定分列模式
     *
     * @param fanoutExchange 分列模式交换器
     * @param topicExchange 主题模式交换器
     */
    @Bean
    public Binding topicBinding1(FanoutExchange fanoutExchange, TopicExchange topicExchange) {
        return BindingBuilder.bind(fanoutExchange).to(topicExchange).with(TOPIC_ROUTING_KEY_ONE);
    }

    /**
     * 主题模式绑定队列2
     *
     * @param queueTwo 队列2
     * @param topicExchange 主题模式交换器
     */
    @Bean
    public Binding topicBinding2(Queue queueTwo, TopicExchange topicExchange) {
        return BindingBuilder.bind(queueTwo).to(topicExchange).with(TOPIC_ROUTING_KEY_TWO);
    }

    /**
     * 主题模式绑定队列3
     *
     * @param queueThree 队列3
     * @param topicExchange 主题模式交换器
     */
    @Bean
    public Binding topicBinding3(Queue queueThree, TopicExchange topicExchange) {
        return BindingBuilder.bind(queueThree).to(topicExchange).with(TOPIC_ROUTING_KEY_THREE);
    }
    // endregion

    // region: 延迟队列
    /** 延迟队列 */
    @Bean
    public Queue delayQueue() {
        return new Queue(DELAY_QUEUE, true);
    }

    /** 延迟队列交换器, x-delayed-type 和 x-delayed-message 固定 */
    @Bean
    public CustomExchange delayExchange() {
        Map<String, Object> args = Maps.newHashMap();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DELAY_MODE_QUEUE, "x-delayed-message", true, false, args);
    }

    /**
     * 延迟队列绑定自定义交换器
     *
     * @param delayQueue 队列
     * @param delayExchange 延迟交换器
     */
    @Bean
    public Binding delayBinding(Queue delayQueue, CustomExchange delayExchange) {
        return BindingBuilder.bind(delayQueue).to(delayExchange).with(DELAY_QUEUE).noargs();
    }
    // endregion
}
