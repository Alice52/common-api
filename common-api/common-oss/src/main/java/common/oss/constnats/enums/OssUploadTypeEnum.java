package common.oss.constnats.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zack <br>
 * @create 2021-06-22 15:41 <br>
 * @project swagger-3 <br>
 */
@Getter
@AllArgsConstructor
public enum OssUploadTypeEnum {
    aliyun("aliyun-oss", "阿里云oss"),
    tencent("tencent-cos", "腾讯云cos");

    /** 类型 */
    private final String type;
    /** 描述 */
    private final String description;
}
