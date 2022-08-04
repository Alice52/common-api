package top.hubby.mq.constants.enums;

import lombok.AllArgsConstructor;

/**
 * @author zack <br>
 * @create 2022-04-11 21:09 <br>
 * @project project-cloud-custom <br>
 */
@AllArgsConstructor
public enum MQQueueTypeEnums {
    DIRECT("direct", 1),
    FANOUT("fanout", 2),
    TOPIC("topic", 3),
    ;

    private String type;
    private int code;
}
