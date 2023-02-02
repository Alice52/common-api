package common.logging.desensitize.v2.appender;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import common.logging.desensitize.v2.DesensitizationAppender;

/**
 * @author Zack Zhang
 */
public class DeRollingFileAppender<E> extends RollingFileAppender<E> {

    @Override
    protected void subAppend(E event) {

        if (event instanceof LoggingEvent) {
            DesensitizationAppender.doDesensitize((LoggingEvent) event);
        }

        super.subAppend(event);
    }
}
