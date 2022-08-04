package top.hubby.mq.exception;

import common.core.exception.assertion.IBaseExceptionAssert;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author zack <br>
 * @create 2022-04-12 10:00 <br>
 * @project project-cloud-custom <br>
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum MQResponseEnum implements IBaseExceptionAssert {
    RETURN_CALLBACK_ERROR(300_000_001, "Return Callback Error"),
    CONFIRM_CALLBACK_ERROR(300_000_002, "Confirm Callback Error"),
    ;

    private Integer errorCode;
    private String errorMsg;
}
