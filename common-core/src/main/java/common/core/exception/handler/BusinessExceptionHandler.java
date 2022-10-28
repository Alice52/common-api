package common.core.exception.handler;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

import javax.annotation.Resource;

import common.core.constant.enums.CommonResponseEnum;
import common.core.exception.BusinessException;
import common.core.util.R;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author zack <br>
 * @create 2021-06-01<br>
 * @project project-custom <br>
 */
@Order(999)
@RestControllerAdvice
@ConditionalOnProperty(
        prefix = "common.core.global.handler",
        value = {"enabled"},
        havingValue = "true",
        matchIfMissing = true)
@Slf4j
public class BusinessExceptionHandler {

    @Resource private DefaultHandler defaultHandler;

    @ExceptionHandler(value = {BusinessException.class})
    public R<Void> handleBusinessException(BusinessException ex) {
        ExceptionHandlerSupport.printContext();
        log.error(ex.getMessage(), ex);

        return R.error(ex.getResponseEnum());
    }

    @ExceptionHandler(UndeclaredThrowableException.class)
    public R<Void> undeclaredThrowableException(UndeclaredThrowableException e) {
        return tryConvert2ConcurrentException(e);
    }

    @ExceptionHandler(ExecutionException.class)
    public R<Void> executionException(ExecutionException e) {
        return tryConvert2BusinessException(e);
    }

    @ExceptionHandler(CompletionException.class)
    public R<Void> completionException(CompletionException e) {
        return tryConvert2BusinessException(e);
    }

    @ExceptionHandler(InterruptedException.class)
    public R<Void> handleTimeoutException(InterruptedException ex) {
        ExceptionHandlerSupport.printContext();
        log.error(ex.getMessage(), ex);
        return R.<Void>error(CommonResponseEnum.TIMEOUT_ERROR);
    }

    private R<Void> tryConvert2ConcurrentException(UndeclaredThrowableException e) {
        if (e.getUndeclaredThrowable() instanceof ExecutionException) {
            return executionException((ExecutionException) e.getUndeclaredThrowable());
        }

        if (e.getUndeclaredThrowable() instanceof CompletionException) {
            return completionException((CompletionException) e.getUndeclaredThrowable());
        }

        if (e.getUndeclaredThrowable() instanceof InterruptedException) {
            return handleTimeoutException((InterruptedException) e.getUndeclaredThrowable());
        }

        return defaultHandler.handleException(e);
    }

    private R<Void> tryConvert2BusinessException(Exception e) {
        if (e.getCause() instanceof BusinessException) {
            return handleBusinessException((BusinessException) e.getCause());
        }

        return defaultHandler.handleException(e);
    }
}
