package common.core.exception.handler;

import java.util.concurrent.TimeoutException;

import common.core.constant.enums.CommonResponseEnum;
import common.core.exception.BaseException;
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
@Order
@RestControllerAdvice
@ConditionalOnProperty(
        prefix = "common.core.global.handler",
        value = {"enabled"},
        havingValue = "true",
        matchIfMissing = true)
@Slf4j
public class DefaultHandler {

    @ExceptionHandler({BaseException.class})
    public R<Void> handleException(BaseException ex) {
        ExceptionHandlerSupport.printContext();
        log.error(ex.getMessage(), ex);

        return R.<Void>error(ex.getResponseEnum());
    }

    @ExceptionHandler({Exception.class, TimeoutException.class})
    public R<Void> handleException(Exception ex) {
        ExceptionHandlerSupport.printContext();
        log.error(ex.getMessage(), ex);

        return R.<Void>error(CommonResponseEnum.INTERNAL_ERROR);
    }
}
