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
public enum ServletResponseEnum implements IBaseExceptionAssert {
    MethodArgumentNotValidException(400_400_000, "BAD_REQUEST"),
    MethodArgumentTypeMismatchException(400_401_000, "BAD_REQUEST"),
    MissingServletRequestPartException(400_402_000, "BAD_REQUEST"),
    MissingPathVariableException(400_403_000, "BAD_REQUEST"),
    BindException(400_404_000, "BAD_REQUEST"),
    MissingServletRequestParameterException(400_405_000, "BAD_REQUEST"),
    TypeMismatchException(400_406_000, "BAD_REQUEST"),
    ServletRequestBindingException(400_407_000, "BAD_REQUEST"),
    HttpMessageNotReadableException(400_408_000, "BAD_REQUEST"),
    NoHandlerFoundException(400_409_000, "NOT_FOUND"),
    NoSuchRequestHandlingMethodException(400_410_000, "NOT_FOUND"),
    HttpRequestMethodNotSupportedException(400_411_000, "METHOD_NOT_ALLOWED"),
    HttpMediaTypeNotAcceptableException(400_412_000, "NOT_ACCEPTABLE"),
    HttpMediaTypeNotSupportedException(400_413_000, "UNSUPPORTED_MEDIA_TYPE"),
    ConversionNotSupportedException(400_414_000, "INTERNAL_SERVER_ERROR"),
    HttpMessageNotWritableException(400_415_000, "INTERNAL_SERVER_ERROR"),
    AsyncRequestTimeoutException(400_416_000, "SERVICE_UNAVAILABLE");

    private Integer errorCode;
    private String errorMsg;
}
