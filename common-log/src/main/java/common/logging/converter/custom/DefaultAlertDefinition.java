package common.logging.converter.custom;

import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxy;
import common.core.exception.BusinessException;
import common.logging.converter.AlertDefinition;

/**
 * @author T04856 <br>
 * @create 2023-05-12 9:33 AM <br>
 * @project project-cloud-custom <br>
 */
public interface DefaultAlertDefinition extends AlertDefinition {

    @Override
    default boolean isSlightlyAlertLevel(IThrowableProxy iThrowableProxy) {
        return ((ThrowableProxy) iThrowableProxy).getThrowable() instanceof Exception;
    }

    @Override
    default boolean isLowAlertLevel(IThrowableProxy iThrowableProxy) {
        return ((ThrowableProxy) iThrowableProxy).getThrowable() instanceof BusinessException;
    }
}
