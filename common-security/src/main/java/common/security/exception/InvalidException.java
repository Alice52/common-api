package common.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import common.security.serialize.Auth2ExceptionSerializer;

/**
 * @author asd <br>
 * @create 2021-06-29 5:18 PM <br>
 * @project custom-upms-grpc <br>
 */
@JsonSerialize(using = Auth2ExceptionSerializer.class)
public class InvalidException extends Auth2Exception {

    public InvalidException(String msg, Throwable t) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "invalid_exception";
    }

    @Override
    public int getHttpErrorCode() {
        return 426;
    }
}
