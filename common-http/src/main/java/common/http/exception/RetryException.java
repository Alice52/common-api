package common.http.exception;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author asd <br>
 * @create 2021-12-07 4:44 PM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
public class RetryException extends RuntimeException {
    public RetryException() {
        super();
    }

    public RetryException(int retry) {
        super("Retry Reach Max Limit: " + retry);
    }

    public RetryException(int retry, String msg) {
        super(StrUtil.format("Retry Reach Max Limit: {} due to: {}", retry, msg));
    }
}
