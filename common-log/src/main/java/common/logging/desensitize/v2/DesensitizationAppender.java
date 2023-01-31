package common.logging.desensitize.v2;

import ch.qos.logback.classic.spi.LoggingEvent;
import cn.hutool.core.text.CharSequenceUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Objects;

@Slf4j
public class DesensitizationAppender {

    /**
     * LoggingEvent's Property <br>
     * - message: raw message before formatted<br>
     * - such as log.info("name: {}", "zack"), and the message is "name: {}"<br>
     */
    private static final String MESSAGE = "message";

    /**
     * LoggingEvent's Property <br>
     * - formattedMessage: formatted message<br>
     * - such as log.info("name: {}", "zack"), and the formattedMessage is "name: zack"<br>
     */
    private static final String FORMATTED_MESSAGE = "formattedMessage";

    public static void doDesensitize(LoggingEvent event) {
        // log message args is empty
        if (Objects.isNull(event.getArgumentArray())) {
            return;
        }

        String eventFormattedMessage = event.getFormattedMessage();

        // obtain desensitize message
        String changeMessage = DesensitizationUtil.customChange(eventFormattedMessage);
        if (CharSequenceUtil.isNotBlank(changeMessage)) {
            try {
                Class<? extends LoggingEvent> eventClazz = event.getClass();
                // replace raw message of log-event by reflect
                 Field message = eventClazz.getDeclaredField(MESSAGE);
                 message.setAccessible(true);
                 message.set(event, changeMessage);

                // replace raw formatted-message of log-event by reflect
                Field formattedMessage = eventClazz.getDeclaredField(FORMATTED_MESSAGE);
                formattedMessage.setAccessible(true);
                formattedMessage.set(event, changeMessage);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                // log symbol, then ignored
                log.warn("[common-log] desensitization appender error due to reflect error");
            }
        }
    }
}
