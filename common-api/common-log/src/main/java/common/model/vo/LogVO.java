package common.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author zack <br>
 * @create 2021-06-02 12:04 <br>
 * @project custom-test <br>
 */
@Data
@ToString
@EqualsAndHashCode
@Deprecated
public class LogVO {
    private String reqId;
    private String url;
    private String beanName;
    private String user;
    private String methodName;
    private String params;
    private String sessionId;
    private String uri;
    private long requestTime;
    private String remoteAddr;
    private String result;
}
