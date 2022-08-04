package top.hubby.mq.sender.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import top.hubby.mq.sender.configuration.RabbitMQAutoConfiguration;

import org.springframework.context.annotation.Import;

/**
 * @author zack <br>
 * @create 2022-04-11 20:09 <br>
 * @project project-cloud-custom <br>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({RabbitMQAutoConfiguration.class})
public @interface EnableMQSender {}
