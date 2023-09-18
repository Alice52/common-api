package common.core.constant.enums;

import common.core.exception.assertion.IBusinessExceptionAssert;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author zack <br>
 * @create 2021-06-03 17:28 <br>
 * @project custom-test <br>
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum BusinessResponseEnum implements IBusinessExceptionAssert {
    LOCK_ERROR(600_000_000, "Get Lock Error"),

    VERIFY_CODE_ERROR(400_000_001, "验证码错误"),
    PARAMTER_ERROR(400_000_001, "參數错误"),
    ;

    private Integer errorCode;
    private String errorMsg;
}
