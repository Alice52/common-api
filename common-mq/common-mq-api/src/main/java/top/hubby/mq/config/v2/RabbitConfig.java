package top.hubby.mq.config.v2;

import lombok.Data;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.hubby.mq.listerner.OrderConsumerListener;

/**
 * @see OrderConsumerListener
 */
@Configuration
public class RabbitConfig {

    @Autowired private RabbitProperties properties;

    @Autowired private RabbitConfigProperties configProperties;

    @Bean(name = "orderConnectionFactory")
    public ConnectionFactory orderConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(properties.getHost());
        connectionFactory.setPort(properties.getPort());
        connectionFactory.setUsername(properties.getUsername());
        connectionFactory.setPassword(properties.getPassword());
        connectionFactory.setVirtualHost(configProperties.getOrderVirtualHost());
        return connectionFactory;
    }

    /** 订单消费者配置 */
    @Bean(name = "orderListenerFactory")
    public SimpleRabbitListenerContainerFactory orderListenerFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            @Qualifier("orderConnectionFactory") ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory listenerContainerFactory =
                new SimpleRabbitListenerContainerFactory();
        // 设置手动应答
        listenerContainerFactory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        configurer.configure(listenerContainerFactory, connectionFactory);
        return listenerContainerFactory;
    }
}

@Data
class RabbitConfigProperties {

    public static final String RABBIT_CONFIG_PREFIX = "rabbit.properties";

    private String orderVirtualHost;
    private String virtualHost2;
}
