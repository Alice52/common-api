package common.core.constant.enums;

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
public enum CommonResponseEnum implements IBaseExceptionAssert {
    OPENAPI_SIGNATURE_ERROR(400_000_001, "Signature Invalid Error"),
    INTERNAL_ERROR(500_000_000, "Internal Error"),
    SERVER_BUSY(500_000_001, "Network Error"),
    BEAN_VALIDATION(400_000_000, "Invalid Parameter"),
    REQUEST_LIMIT_ERROR(400_000_000, "Too Many Request"),
    TIMEOUT_ERROR(600_000_000, "Timeout"),
    OSS_CONFIGURE_ERROR(700_000_001, "Please Check OSS Type!"),
    OSS_COS_CREDENTIAL_ERROR(700_000_002, "Obtain COS Credential Error"),
    OSS_DOWNLOAD_ERROR(700_000_003, "OSS Download Error"),
    OSS_UPLOAD_ERROR(700_000_004, "OSS Upload Error"),
    OSS_CUSTOM_ERROR(700_000_005, "OSS Error"),

    UID_GENERATE_ERROR(800_000_001, "Uid Generate Error"),

    ENCODING_ERROR(500_001_000, "Data Encoding Error"),
    DECODING_ERROR(500_001_000, "Data Decoding Error"),
    ;

    private Integer errorCode;
    private String errorMsg;
}
