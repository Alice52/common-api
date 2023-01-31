package common.logging.desensitize.v2;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.ConsoleAppender;

/**
 * @author Zack Zhang
 */
public class DesConsoleAppender<E> extends ConsoleAppender<E> {

    @Override
    protected void subAppend(E event) {

        if (event instanceof LoggingEvent) {
            DesensitizationAppender.doDesensitize((LoggingEvent) event);
        }

        super.subAppend(event);
    }
}
