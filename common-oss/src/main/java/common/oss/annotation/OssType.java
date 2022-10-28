package common.oss.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import common.oss.constnats.enums.OssUploadTypeEnum;

/**
 * @author zack <br>
 * @create 2021-06-22 17:36 <br>
 * @project swagger-3 <br>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface OssType {
    OssUploadTypeEnum type();
}
