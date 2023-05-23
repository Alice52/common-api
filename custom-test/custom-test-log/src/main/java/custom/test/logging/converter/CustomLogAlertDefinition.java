package custom.test.logging.converter;

import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxy;
import common.core.exception.BusinessException;
import common.logging.converter.ErrorMarker;
import common.logging.converter.custom.DefaultAlertDefinition;

/**
 * @author T04856 <br>
 * @create 2023-05-12 9:33 AM <br>
 * @project project-cloud-custom <br>
 */
public interface CustomLogAlertDefinition extends DefaultAlertDefinition {

    @Override
    default boolean isLowAlertLevel(IThrowableProxy iThrowableProxy) {
        return ((ThrowableProxy) iThrowableProxy).getThrowable() instanceof BusinessException;
    }

    @Override
    default String translateToErrorType(IThrowableProxy iThrowableProxy) {
        if (((ThrowableProxy) iThrowableProxy).getThrowable() instanceof BusinessException) {
            return ErrorMarker.INFRASTRUCTURE_ERROR;
        }

        return DefaultAlertDefinition.super.translateToErrorType(iThrowableProxy);
    }
}
