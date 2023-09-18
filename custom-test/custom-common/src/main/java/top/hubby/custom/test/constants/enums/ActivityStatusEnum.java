package top.hubby.custom.test.constants.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zack <br>
 * @create 2021-04-09 10:19 <br>
 * @project integration <br>
 */
@Getter
@AllArgsConstructor
public enum ActivityStatusEnum {
    DEFAULT(1, ""),
    NOT_STARTED(2, "未开始"),
    STARTED(3, "已开始"),
    ENDED(4, "已结束");

    @EnumValue @JsonValue private final int code;
    private String name;

    /**
     * Notice this method may return null.
     *
     * @param code
     * @return
     */
    public static ActivityStatusEnum getEnumByStatusCode(int code) {
        for (ActivityStatusEnum item : ActivityStatusEnum.values()) {
            if (item.code == code) {
                return item;
            }
        }
        return null;
    }
}
