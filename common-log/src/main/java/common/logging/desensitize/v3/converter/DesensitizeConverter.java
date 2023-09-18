package common.logging.desensitize.v3.converter;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import common.logging.desensitize.v3.DESENSITIZE_RULE;

import java.util.Objects;

/**
 * This class is used in logback.xml to log Object property.<br>
 * <code>
 *      <conversionRule conversionWord="msg" converterClass="common.converter.DesensitizeConverter"/>
 *      <property name="file.pattern" value="${[%t] %-40.40logger{39} : %msg%n}"/>
 *  </code>
 */
public class DesensitizeConverter extends MessageConverter {

    @Override
    public String convert(ILoggingEvent event) {
        DESENSITIZE_RULE rule = DESENSITIZE_RULE.matchedMarker(event.getMarker());
        if (Objects.isNull(rule)) {
            return super.convert(event);
        }

        return rule.transformLoggerEvent(event);
    }
}
