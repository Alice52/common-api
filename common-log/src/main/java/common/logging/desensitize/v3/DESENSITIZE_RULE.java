package common.logging.desensitize.v3;

import static common.logging.desensitize.v3.DesensitizeMarkerFactory.NAME_SENSITIVE_MARKER;
import static common.logging.desensitize.v3.DesensitizeMarkerFactory.NAME_TRANSFORM;

import ch.qos.logback.classic.spi.ILoggingEvent;

import cn.hutool.core.text.CharSequenceUtil;

import lombok.AllArgsConstructor;

import org.slf4j.Marker;

import java.util.function.Function;
import java.util.stream.Stream;

@AllArgsConstructor
public enum DESENSITIZE_RULE {
    JSON_POS_LOG_SENSITIVE_RULE(NAME_SENSITIVE_MARKER, NAME_TRANSFORM),
    ;
    private final Marker marker;
    private final Function<ILoggingEvent, String> transform;

    public static DESENSITIZE_RULE matchedMarker(Marker marker) {
        if (null == marker || CharSequenceUtil.isBlank(marker.getName())) {
            return null;
        }
        return Stream.of(DESENSITIZE_RULE.values())
                .filter(rule -> rule.marker.getName().equalsIgnoreCase(marker.getName()))
                .findAny()
                .orElseGet(null);
    }

    public String transformLoggerEvent(ILoggingEvent event) {
        return transform.apply(event);
    }
}
