package common.logging.desensitize.v2.appender;

import ch.qos.logback.classic.spi.LoggingEvent;
import com.aliyun.openservices.log.logback.LoghubAppender;
import common.logging.desensitize.v2.DesensitizationAppender;

/**
 * @author Zack Zhang
 */
public class DeLoghubAppender<E> extends LoghubAppender<E> {

    @Override
    public void append(E event) {

        if (event instanceof LoggingEvent) {
            DesensitizationAppender.doDesensitize((LoggingEvent) event);
        }

        super.append(event);
    }
}
