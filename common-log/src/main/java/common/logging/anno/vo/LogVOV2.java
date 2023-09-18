package common.logging.anno.vo;

import lombok.Data;

import java.util.Map;

@Data
public class LogVOV2 {

    private String reqId;

    private String remoteAddr;
    private String url;
    private String uri;
    private Object body;
    private String method;
    private Map<String, ?> params;
    private Map<String, Object> headers;
    private Object result = new Object();

    private String beanName;
    private String methodName;

    private Long requestTime;
    private Long requestEndTime;
    private Long requestDuration;

    public void clearUnNecessaryField() {
        this.setBeanName(null);
        this.setMethodName(null);
        this.setRemoteAddr(null);
        this.setUri(null);
        this.setHeaders(null);
        this.setBody(null);
        this.setParams(null);
    }
}
