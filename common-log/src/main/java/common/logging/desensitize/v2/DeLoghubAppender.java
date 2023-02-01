package common.logging.desensitize.v2;

import ch.qos.logback.classic.spi.LoggingEvent;
import com.aliyun.openservices.log.logback.LoghubAppender;

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
