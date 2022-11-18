package top.hubby.custom.test.constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zack <br>
 * @create 2021-04-09 10:16 <br>
 * @project integration <br>
 */
@Getter
@AllArgsConstructor
public enum PhaseStatusEnum {
    DEFAULT("", ""),
    NOT_STARTED("NOT_STARTED", "未开始"),
    STARTED("STARTED", "已开始"),
    ENDED("ENDED", "已结束");

    private String statusCode;
    private String statusName;

    /**
     * Notice this method may return null.
     *
     * @param statusCode
     * @return
     */
    public static PhaseStatusEnum getEnumByStatusCode(String statusCode) {
        for (PhaseStatusEnum item : PhaseStatusEnum.values()) {
            if (item.statusCode.equals(statusCode)) {
                return item;
            }
        }
        return null;
    }
}
