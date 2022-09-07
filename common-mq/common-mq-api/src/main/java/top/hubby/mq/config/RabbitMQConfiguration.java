package top.hubby.mq.config;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.springframework.amqp.support.converter.Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID;

/**
 * @author zack <br>
 * @create 2022-04-11 19:46 <br>
 * @project project-cloud-custom <br>
 */
@Getter
public abstract class RabbitMQConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQConfiguration.class);
    private RabbitTemplate rabbitTemplate;

    @Bean
    @ConditionalOnMissingBean
    public Jackson2JsonMessageConverter converter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setTypePrecedence(TYPE_ID);
        return converter;
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public RabbitTemplate rabbitTemplate(
            CachingConnectionFactory connectionFactory, Jackson2JsonMessageConverter converter) {
        connectionFactory.setPublisherConfirms(true);
        connectionFactory.setPublisherReturns(true);

        connectionFactory.setPublisherChannelFactory(MQPublisherCallbackChannelImpl.factory());

        rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter);
        // important
        rabbitTemplate.setMandatory(true);
        initRabbitTemplate();

        return rabbitTemplate;
    }

    protected abstract void initRabbitTemplate();
}
