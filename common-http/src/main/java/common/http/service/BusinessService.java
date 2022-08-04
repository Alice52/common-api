package common.http.service;

import common.http.model.TokenVO;
import lombok.extern.slf4j.Slf4j;

/**
 * @author asd <br>
 * @create 2021-12-07 4:22 PM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
public abstract class BusinessService {
    public abstract void refreshToken();

    public abstract TokenVO token();

    public abstract void refreshDecryptToken();
}
