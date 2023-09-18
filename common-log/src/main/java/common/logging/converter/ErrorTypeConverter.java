package common.logging.converter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import cn.hutool.core.text.CharSequenceUtil;
import org.slf4j.Marker;

/**
 * @author T04856 <br>
 * @create 2023-05-12 9:45 AM <br>
 * @project project-cloud-custom <br>
 */
public abstract class ErrorTypeConverter extends MessageConverter implements AlertDefinition {

    /**
     *
     *
     * <pre>
     *     1. get from ErrorMarker
     *     2. or else get from translateToErrorType()
     *     3. or else judge by level
     * </pre>
     *
     * @param event
     * @return
     */
    @Override
    public String convert(ILoggingEvent event) {
        if (event.getLevel().isGreaterOrEqual(Level.ERROR)) {
            Marker marker = event.getMarker();
            if (marker instanceof ErrorMarker) {
                ErrorMarker errorMarker = (ErrorMarker) marker;
                return errorMarker.getErrorType();
            }

            IThrowableProxy throwableProxy = event.getThrowableProxy();
            if (null == throwableProxy) {
                return this.defaultMarker().getErrorType();
            }

            String errorType = translateToErrorType(throwableProxy);
            if (CharSequenceUtil.isNotBlank(errorType)) {
                return errorType;
            }

            return mappedMarker(throwableProxy).getErrorType();
        }

        return AlertDefinition.EMPTY;
    }
}
