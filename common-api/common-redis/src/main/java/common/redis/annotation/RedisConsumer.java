package common.redis.annotation;

import java.lang.annotation.*;

/**
 * @author zack <br>
 * @create 2022-05-23 09:25 <br>
 * @project project-cloud-custom <br>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface RedisConsumer {
    /** Redis Stream key */
    String streamKey();

    /* Redis Stream 消费者分组名字 */
    String consumerGroup();

    /* Redis Stream 消费者名称*/
    String consumerName();

    /* Redis Stream Group 消费者数量，如果一个分组需要创建多个消费者，会自动在消费者名称后面拼接1，2，3等数字*/
    int consumerNum() default 1;
}
