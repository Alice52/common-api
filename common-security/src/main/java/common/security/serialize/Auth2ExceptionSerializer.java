package common.security.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import common.core.constant.CommonConstants;
import common.security.exception.Auth2Exception;
import lombok.SneakyThrows;

/**
 * @author asd <br>
 * @create 2021-06-29 5:07 PM <br>
 * @project common.security <br>
 */
public class Auth2ExceptionSerializer extends StdSerializer<Auth2Exception> {
    public Auth2ExceptionSerializer() {
        super(Auth2Exception.class);
    }

    @Override
    @SneakyThrows
    public void serialize(Auth2Exception value, JsonGenerator gen, SerializerProvider provider) {
        gen.writeStartObject();
        gen.writeObjectField("code", CommonConstants.FAIL);
        gen.writeStringField("msg", value.getMessage());
        gen.writeStringField("data", value.getErrorCode());
        gen.writeEndObject();
    }
}
