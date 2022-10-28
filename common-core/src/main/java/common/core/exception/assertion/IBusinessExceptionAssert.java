package common.core.exception.assertion;

import java.text.MessageFormat;

import common.core.exception.BaseException;
import common.core.exception.BusinessException;

/**
 * @author zack <br>
 * @create 2021-06-01<br>
 * @project project-custom <br>
 */
public interface IBusinessExceptionAssert extends IBaseErrorResponse, IBaseAssert {

    @Override
    default BaseException newException(Object... args) {
        String msg = this.getErrorMsg();
        if (args != null && args.length > 0) {
            msg = MessageFormat.format(this.getErrorMsg(), args);
        }

        return new BusinessException(this, args, msg);
    }

    @Override
    default BaseException newException(Throwable t, Object... args) {
        String msg = this.getErrorMsg();
        if (args != null && args.length > 0) {
            msg = MessageFormat.format(this.getErrorMsg(), args);
        }

        return new BusinessException(this, args, msg, t);
    }
}
