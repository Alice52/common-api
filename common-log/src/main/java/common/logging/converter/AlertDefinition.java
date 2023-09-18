package common.logging.converter;

import ch.qos.logback.classic.spi.IThrowableProxy;

/** contain main filed: error_level, error_type. */
public interface AlertDefinition {
    String EMPTY = "";

    default boolean isHighAlertLevel(IThrowableProxy iThrowableProxy) {
        return false;
    }

    default boolean isMediumAlertLevel(IThrowableProxy iThrowableProxy) {
        return false;
    }

    default boolean isLowAlertLevel(IThrowableProxy iThrowableProxy) {
        return false;
    }

    default boolean isSlightlyAlertLevel(IThrowableProxy iThrowableProxy) {
        return false;
    }

    default String translateToLevel(IThrowableProxy iThrowableProxy) {
        return mappedMarker(iThrowableProxy).getName();
    }

    default String translateToErrorType(IThrowableProxy iThrowableProxy) {
        return mappedMarker(iThrowableProxy).getErrorType();
    }

    default ErrorMarker defaultMarker() {
        return ErrorMarker.EMPTY;
    }

    default ErrorMarker mappedMarker(IThrowableProxy throwableProxy) {

        if (isHighAlertLevel(throwableProxy)) {
            return ErrorMarker.HIGH;
        }

        if (isMediumAlertLevel(throwableProxy)) {
            return ErrorMarker.MEDIUM;
        }

        if (isLowAlertLevel(throwableProxy)) {
            return ErrorMarker.LOW;
        }

        if (isSlightlyAlertLevel(throwableProxy)) {
            return ErrorMarker.SLIGHT;
        }

        return defaultMarker();
    }
}
