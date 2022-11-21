package top.hubby.test.custom.openapi.constants;

import common.core.exception.assertion.IBusinessExceptionAssert;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum OpenApiResponseEnum implements IBusinessExceptionAssert {
    CLIENT_ID_INVALID(700_000_000, "Client Id Is Not Exist"),
    ;

    private Integer errorCode;
    private String errorMsg;
}
