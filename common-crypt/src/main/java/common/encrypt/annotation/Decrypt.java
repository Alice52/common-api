package common.encrypt.annotation;

import lombok.Data;

import java.lang.annotation.*;

/**
 * @author Zack Zhang
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Decrypt {

    /**
     * 加密的文本字段名
     *
     * @return
     */
    String field() default "origin";

    /**
     * @see com.fasterxml.jackson.databind.JsonNode
     */
    @Data
    @Deprecated
    class RequestData {
        // 加密的文本
        private String origin;
    }
}
