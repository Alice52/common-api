package common.security.service;

import javax.sql.DataSource;

import common.core.constant.SecurityConstants;
import lombok.SneakyThrows;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

/**
 * @author asd <br>
 * @create 2021-06-30 8:52 AM <br>
 * @project custom-upms-grpc <br>
 */
public class CustomJdbcClientDetailsService extends JdbcClientDetailsService {

    public CustomJdbcClientDetailsService(DataSource dataSource) {
        super(dataSource);
    }

    /**
     * 重写原生方法支持redis缓存
     *
     * @param clientId
     * @return
     */
    @Override
    @SneakyThrows
    @Cacheable(
            value = SecurityConstants.CLIENT_DETAILS_KEY,
            key = "#clientId",
            unless = "#result == null")
    public ClientDetails loadClientByClientId(String clientId) {
        return super.loadClientByClientId(clientId);
    }
}
