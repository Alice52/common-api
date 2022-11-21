package common.core.exception;

import common.core.exception.assertion.IBaseErrorResponse;

/**
 * @author zack <br>
 * @create 2021-06-01 18:31 <br>
 * @project custom-test <br>
 */
public class BusinessException extends BaseException {

    private static final long serialVersionUID = 1L;

    public BusinessException(IBaseErrorResponse responseEnum) {
        super(responseEnum);
    }


    public BusinessException(IBaseErrorResponse responseEnum, Object[] args, String message) {
        super(responseEnum, args, message);
    }

    public BusinessException(
            IBaseErrorResponse responseEnum, Object[] args, String message, Throwable cause) {
        super(responseEnum, args, message, cause);
    }
}
