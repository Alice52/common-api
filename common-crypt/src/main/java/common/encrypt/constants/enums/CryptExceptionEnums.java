package common.encrypt.constants.enums;

import common.core.exception.assertion.IBaseExceptionAssert;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author zack <br>
 * @create 2021-06-01<br>
 * @project project-custom <br>
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum CryptExceptionEnums implements IBaseExceptionAssert {
    DECRYPT_FAILED(400_010_001, "Decrypt Error"),
    ;

    private Integer errorCode;
    private String errorMsg;
}
