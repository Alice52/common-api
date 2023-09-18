package common.logging.desensitize.v2.appender;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import common.logging.desensitize.v2.DesensitizationAppender;

/**
 * @author Zack Zhang
 */
public class DeConsoleJsonAppender<E> extends ConsoleAppender<E> {

    @Override
    protected void subAppend(E event) {

        if (event instanceof LoggingEvent) {
            DesensitizationAppender.doDesensitize((LoggingEvent) event);
        }

        super.subAppend(event);
    }
}
