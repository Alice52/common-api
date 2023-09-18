package top.hubby.mq.constants.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;

/**
 * @author zack <br>
 * @create 2022-04-12 11:51 <br>
 * @project project-cloud-custom <br>
 */
@AllArgsConstructor
public enum EventStatus {
    NEW("new", 1),
    PULISHED("pulished", 2),
    RECEIVED("received", 3),
    CONSUMED("consumed", 4),
    ;

    private String status;

    @EnumValue private int code;
}
