package common.security.service;

import common.core.constant.SecurityConstants;
import common.core.util.R;
import common.security.model.CustomUser;
import common.security.utils.UserDetailsUtil;
import custom.basic.api.dto.UserDTO;
import custom.basic.api.feign.RemoteUserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author asd <br>
 * @create 2021-06-30 10:00 AM <br>
 * @project custom-upms-grpc <br>
 */
public class AuthAdminDetailsService implements UserDetailsService {
    @Autowired private RemoteUserService remoteUserService;

    @Autowired private CacheManager cacheManager;

    /**
     * 用户密码登录
     *
     * @param username 用户名
     * @return
     */
    @Override
    @SneakyThrows
    public UserDetails loadUserByUsername(String username) {
        Cache cache = cacheManager.getCache(SecurityConstants.ADMIN_USER_DETAILS_KEY);
        if (cache != null && cache.get(username) != null) {
            return (CustomUser) cache.get(username).get();
        }

        R<UserDTO> result = remoteUserService.getUser(username, SecurityConstants.FROM_IN);
        UserDetails userDetails = null;
        if (result != null) {
            userDetails = UserDetailsUtil.getAdminUserDetails(result.getData());
        }

        if (userDetails == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        cache.put(username, userDetails);
        return userDetails;
    }
}
