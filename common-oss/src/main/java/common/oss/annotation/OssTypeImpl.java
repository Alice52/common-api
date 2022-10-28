package common.oss.annotation;

import java.lang.annotation.Annotation;

import common.oss.constnats.enums.OssUploadTypeEnum;

/**
 * @author zack <br>
 * @create 2021-06-22 17:37 <br>
 * @project swagger-3 <br>
 */
public class OssTypeImpl implements OssType {

    private OssUploadTypeEnum type;

    public OssTypeImpl(OssUploadTypeEnum type) {
        this.type = type;
    }

    @Override
    public OssUploadTypeEnum type() {
        return type;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return OssType.class;
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        hashCode += (127 * "type".hashCode()) ^ type.hashCode();
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OssType)) {
            return false;
        }
        OssType other = (OssType) obj;
        return type.name().equals(other.type().name());
    }
}
