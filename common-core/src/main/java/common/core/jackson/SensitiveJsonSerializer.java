package common.core.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import common.core.annotation.DeSensitive;
import common.core.annotation.discriptor.SensitiveStrategy;

import java.io.IOException;
import java.util.Objects;

/**
 * @author zack <br>
 * @create 2021-06-09 09:42 <br>
 * @project custom-test <br>
 */
public class SensitiveJsonSerializer extends JsonSerializer<String>
        implements ContextualSerializer {
    private SensitiveStrategy strategy;

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeString(strategy.getDeSensitiver().apply(value));
    }

    /**
     * 用来获取实体类上的 @DeSensitive 注解并根据条件初始化对应的 JsonSerializer对象
     *
     * @param prov
     * @param property
     * @return
     * @throws JsonMappingException
     */
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
            throws JsonMappingException {

        DeSensitive annotation = property.getAnnotation(DeSensitive.class);
        if (Objects.nonNull(annotation)
                && Objects.equals(String.class, property.getType().getRawClass())) {
            this.strategy = annotation.strategy();
            return this;
        }
        return prov.findValueSerializer(property.getType(), property);
    }
}
