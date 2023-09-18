package common.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import common.security.serialize.Auth2ExceptionSerializer;

import org.springframework.http.HttpStatus;

/**
 * @author asd <br>
 * @create 2021-06-29 5:19 PM <br>
 * @project custom-upms-grpc <br>
 */
@JsonSerialize(using = Auth2ExceptionSerializer.class)
public class ServerErrorException extends Auth2Exception {

    public ServerErrorException(String msg, Throwable t) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "server_error";
    }

    @Override
    public int getHttpErrorCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }
}
