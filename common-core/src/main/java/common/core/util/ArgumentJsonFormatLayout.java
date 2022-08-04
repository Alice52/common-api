package common.core.util;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.slf4j.helpers.MessageFormatter;

import java.util.Arrays;

/**
 * This class is used in logback-spring.xml to log Object property.<br>
 *
 * @author zack <br>
 * @create 2021-06-02 14:44 <br>
 * @project custom-test <br>
 */
public class ArgumentJsonFormatLayout extends MessageConverter {

    @Override
    public String convert(ILoggingEvent event) {
        String formattedMessage = event.getFormattedMessage();
        try {
            Object[] argumentArray = event.getArgumentArray();
            if (ObjectUtil.isNull(argumentArray)) {
                return formattedMessage;
            }

            if (StrUtil.contains(formattedMessage, "@")) {
                Object[] objects = Arrays.stream(argumentArray).map(JSONUtil::toJsonStr).toArray();
                return MessageFormatter.arrayFormat(event.getMessage(), objects).getMessage();
            }

            return formattedMessage;
        } catch (Exception e) {
            return formattedMessage;
        }
    }
}
