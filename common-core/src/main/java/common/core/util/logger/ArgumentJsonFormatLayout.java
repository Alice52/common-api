package common.core.util.logger;

import java.util.Arrays;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.slf4j.helpers.MessageFormatter;

/**
 * This class is used in logback.xml to log Object property.<br>
 *
 * @author zack <br>
 * @create 2020-03-06 22:31 <br>
 */
public class ArgumentJsonFormatLayout extends MessageConverter {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @SneakyThrows
    private static String writeAsString(Object obj) {
        return MAPPER.writeValueAsString(obj);
    }

    @Override
    public String convert(ILoggingEvent event) {
        String formattedMessage = event.getFormattedMessage();
        try {
            Object[] argumentArray = event.getArgumentArray();
            if (ObjectUtil.isNull(argumentArray)) {
                return formattedMessage;
            }

            if (CharSequenceUtil.contains(formattedMessage, "@")) {
                Object[] objects = Arrays.stream(argumentArray).map(JSONUtil::toJsonStr).toArray();
                return MessageFormatter.arrayFormat(event.getMessage(), objects).getMessage();
            }

            return formattedMessage;
        } catch (Exception e) {
            return formattedMessage;
        }
    }
}
