package common.core.exception.handler;

import common.core.constant.enums.CommonResponseEnum;
import common.core.constant.enums.ServletResponseEnum;
import common.core.exception.assertion.IBaseErrorResponse;
import common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zack <br>
 * @create 2021-06-01<br>
 * @project project-custom <br>
 */
@Order(100)
@RestControllerAdvice
@ConditionalOnProperty(
        prefix = "common.core.global.handler",
        value = {"enabled"},
        havingValue = "true",
        matchIfMissing = true)
@Slf4j
public class ServletExceptionHandler {

    protected static R<Void> handleServletException(Exception e) {
        ExceptionHandlerSupport.printContext();
        log.error(e.getMessage(), e);

        IBaseErrorResponse response = CommonResponseEnum.INTERNAL_ERROR;
        try {
            response = ServletResponseEnum.valueOf(e.getClass().getSimpleName());
        } catch (IllegalArgumentException e1) {
            log.error(
                    "class [{}] not defined in enum {}",
                    e.getClass().getName(),
                    ServletResponseEnum.class.getName());
        }

        // if (ENV_PROD.equals(profile)) {
        //     // 当为生产环境, 不适合把具体的异常信息展示给用户, 比如404.
        //     code = CommonResponseEnum.SERVER_ERROR.getCode();
        //     BaseException baseException = new BaseException(CommonResponseEnum.SERVER_ERROR);
        //     String message = getMessage(baseException);
        //     return new ErrorResponse(code, message);
        // }
        return R.<Void>error(response);
    }

    @ExceptionHandler({
        NoHandlerFoundException.class,
        HttpRequestMethodNotSupportedException.class,
        HttpMediaTypeNotSupportedException.class,
        HttpMediaTypeNotAcceptableException.class,
        MissingPathVariableException.class,
        MissingServletRequestParameterException.class,
        TypeMismatchException.class,
        HttpMessageNotReadableException.class,
        HttpMessageNotWritableException.class,
        // BindException.class,
        // MethodArgumentNotValidException.class
        ServletRequestBindingException.class,
        ConversionNotSupportedException.class,
        MissingServletRequestPartException.class,
        AsyncRequestTimeoutException.class
    })
    public R<Void> handleException(HttpServletRequest request, Exception e) {
        return handleServletException(e);
    }
}
