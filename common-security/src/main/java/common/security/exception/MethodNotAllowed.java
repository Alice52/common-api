package common.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import common.security.serialize.Auth2ExceptionSerializer;

import org.springframework.http.HttpStatus;

/**
 * @author asd <br>
 * @create 2021-06-29 5:18 PM <br>
 * @project custom-upms-grpc <br>
 */
@JsonSerialize(using = Auth2ExceptionSerializer.class)
public class MethodNotAllowed extends Auth2Exception {

    public MethodNotAllowed(String msg, Throwable t) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "method_not_allowed";
    }

    @Override
    public int getHttpErrorCode() {
        return HttpStatus.METHOD_NOT_ALLOWED.value();
    }
}
