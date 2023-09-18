package common.core.exception;

import common.core.exception.assertion.IBaseAssert;

/**
 * 只包装了 错误信息 的 {@link RuntimeException}. 用于 {@link IBaseAssert} 中用于包装自定义异常信息
 *
 * @author zack <br>
 * @create 2021-06-01<br>
 * @project project-custom <br>
 */
public class WrapMessageException extends RuntimeException {

    public WrapMessageException(String message) {
        super(message);
    }

    public WrapMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
