package common.security.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import common.security.serialize.Auth2ExceptionSerializer;
import lombok.Getter;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * @author asd <br>
 * @create 2021-06-29 5:07 PM <br>
 * @project custom-upms-grpc <br>
 */
@JsonSerialize(using = Auth2ExceptionSerializer.class)
public class Auth2Exception extends OAuth2Exception {
    @Getter private String errorCode;

    public Auth2Exception(String msg) {
        super(msg);
    }

    public Auth2Exception(String msg, String errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }
}
