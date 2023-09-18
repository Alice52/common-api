package common.oss.annotation;

import common.oss.constnats.enums.OssUploadTypeEnum;

import java.lang.annotation.*;

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
