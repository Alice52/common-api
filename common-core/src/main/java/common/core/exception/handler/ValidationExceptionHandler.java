package common.core.exception.handler;

import cn.hutool.core.util.StrUtil;
import common.core.constant.enums.CommonResponseEnum;
import common.core.exception.ListValidException;
import common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This is bean validation exception handler.
 *
 * @author zack <br>
 * @create 2021-06-01<br>
 * @project project-custom <br>
 */
@RestControllerAdvice
@Order(101)
@ConditionalOnProperty(
        prefix = "common.core.global.handler",
        value = {"enabled"},
        havingValue = "true",
        matchIfMissing = true)
@Slf4j
public class ValidationExceptionHandler {

    private static String subAfter(
            CharSequence string, CharSequence separator, boolean isLastSeparator) {
        if (StrUtil.isEmpty(string)) {
            return null == string ? null : string.toString();
        }
        if (separator == null) {
            return "";
        }
        final String str = string.toString();
        final String sep = separator.toString();
        final int pos = isLastSeparator ? str.lastIndexOf(sep) : str.indexOf(sep);
        if (-1 == pos || (string.length() - 1) == pos) {
            return "";
        }
        return str.substring(pos + separator.length());
    }

    @ExceptionHandler(ValidationException.class)
    public R handleValidationException(ValidationException ex) throws Exception {
        ExceptionHandlerSupport.printContext();
        log.error(
                "validation bean error: type {}, params {}, message {}, detail {}",
                ex.getClass().getTypeName(),
                ex.getClass().getTypeParameters(),
                ex.getMessage(),
                ex);

        Throwable cause = ex.getCause();
        Map<String, Object> collect = new HashMap<>(16);

        if (cause instanceof ListValidException) {
            Map<Integer, Set<ConstraintViolation<Object>>> errors =
                    ((ListValidException) cause).getErrors();

            errors.forEach(
                    (i, error) -> {
                        error.stream()
                                .parallel()
                                .forEach(
                                        x ->
                                                collect.put(
                                                        "["
                                                                + i
                                                                + "]."
                                                                + x.getPropertyPath().toString(),
                                                        new HashMap<String, Object>(2) {
                                                            {
                                                                put(
                                                                        "rejectValue",
                                                                        x.getInvalidValue());
                                                                put("message", x.getMessage());
                                                            }
                                                        }));
                    });
        } else {
            collect.put("message", ex.getMessage());
            collect.put("exception type", ex.getClass().getTypeName());
        }

        return R.error(CommonResponseEnum.BEAN_VALIDATION, collect);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public R handleConstraintViolationException(ConstraintViolationException ex) {
        ExceptionHandlerSupport.printContext();
        Map<String, Object> collect =
                ex.getConstraintViolations().stream()
                        .parallel()
                        .collect(
                                Collectors.toMap(
                                        x -> subAfter(x.getPropertyPath().toString(), ".", false),
                                        x ->
                                                new HashMap<String, Object>(2) {
                                                    {
                                                        put("rejectValue", x.getInvalidValue());
                                                        put("message", x.getMessage());
                                                    }
                                                },
                                        (s, a) -> Arrays.asList(s, a)));

        return R.error(CommonResponseEnum.BEAN_VALIDATION, collect);
    }

    @ExceptionHandler(BindException.class)
    public R handleBindException(BindException ex) {

        return getErrorResults(ex.getBindingResult(), ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        return getErrorResults(ex.getBindingResult(), ex);
    }

    private R getErrorResults(BindingResult bindingResult, Exception ex) {
        ExceptionHandlerSupport.printContext();
        log.error(
                "validation bean error: type {}, params {}, message {}, detail {}",
                ex.getClass().getTypeName(),
                ex.getClass().getTypeParameters(),
                ex.getMessage(),
                ex);

        Map<String, Object> collect =
                bindingResult.getFieldErrors().stream()
                        .parallel()
                        .collect(
                                Collectors.toMap(
                                        FieldError::getField,
                                        x ->
                                                new HashMap<String, Object>(2) {
                                                    {
                                                        put("rejectValue", x.getRejectedValue());
                                                        put("message", x.getDefaultMessage());
                                                    }
                                                },
                                        (s, a) -> Arrays.asList(s, a)));

        return R.error(CommonResponseEnum.BEAN_VALIDATION, collect);
    }
}
