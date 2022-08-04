package common.http.exception;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author asd <br>
 * @create 2021-12-07 4:24 PM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
@Data
public class HttpException extends RuntimeException {
    private String errorMsg;

    public HttpException() {
        super();
    }

    public HttpException(String message) {
        super(message);
    }
}
