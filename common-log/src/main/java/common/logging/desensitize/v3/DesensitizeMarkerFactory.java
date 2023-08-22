package common.logging.desensitize.v3;

import ch.qos.logback.classic.spi.ILoggingEvent;

import com.alibaba.fastjson.JSON;

import org.slf4j.Marker;

import java.util.function.Function;

public class DesensitizeMarkerFactory {
    public static final Marker NAME_SENSITIVE_MARKER =
            org.slf4j.MarkerFactory.getMarker("NAME_SENSITIVE_MARKER");
    public static final Function<ILoggingEvent, String> NAME_TRANSFORM =
            event -> {
                String message = event.getMessage();
                Object[] objects = event.getArgumentArray();

                try {
                    // do mask logic
                    return message.replace("{}", JSON.toJSONString(objects[0]));
                } catch (Exception e) {
                    return "logback json converter failure, e:" + e.getMessage();
                }
            };
}
