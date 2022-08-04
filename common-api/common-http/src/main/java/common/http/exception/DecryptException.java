package common.http.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * @author asd <br>
 * @create 2021-12-07 4:37 PM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
public class DecryptException extends RuntimeException {
    public DecryptException() {
        super();
    }

    public DecryptException(String message) {
        super(message);
    }
}
