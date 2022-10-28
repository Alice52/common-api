package common.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import common.core.annotation.discriptor.SensitiveStrategy;
import common.core.jackson.SensitiveJsonSerializer;

/**
 * @author zack <br>
 * @create 2021-06-09 09:38 <br>
 * @project custom-test <br>
 */
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveJsonSerializer.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DeSensitive {
    SensitiveStrategy strategy();
}
