package common.http.service.impl;

import common.http.model.TokenVO;
import common.http.service.BusinessService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.naming.OperationNotSupportedException;

/**
 * @author asd <br>
 * @create 2021-12-07 4:55 PM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
@Component
public class DefaultBusinessService extends BusinessService {
    @SneakyThrows
    @Override
    public void refreshToken() {
        throw new OperationNotSupportedException();
    }

    @SneakyThrows
    @Override
    public TokenVO token() {
        throw new OperationNotSupportedException();
    }

    @SneakyThrows
    @Override
    public void refreshDecryptToken() {
        throw new OperationNotSupportedException();
    }
}
