package common.security.service;

import common.core.constant.SecurityConstants;
import common.core.util.R;
import common.security.grant.MobileTokenGranter;
import common.security.model.CustomUser;
import common.security.utils.UserDetailsUtil;
import custom.basic.api.dto.MemberSocialDTO;
import custom.basic.api.dto.UserDTO;
import custom.basic.api.feign.RemoteUserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

/**
 * @author asd <br>
 * @create 2021-06-30 8:54 AM <br>
 * @project custom-upms-grpc <br>
 * @see PreAuthenticatedAuthenticationProvider
 */
public class AuthUserDetailsService implements UserDetailsService {
    @Autowired private RemoteUserService remoteUserService;

    /**
     * 用户密码登录
     *
     * @param username 用户名
     * @return
     */
    @Override
    @SneakyThrows
    public UserDetails loadUserByUsername(String username) {
        String[] usernameData = username.split(":", 2);
        String name = username;
        String accountType = MobileTokenGranter.GRANT_TYPE;
        if (usernameData.length == 2) {
            accountType = usernameData[0];
            name = usernameData[1];
        }

        UserDetails userDetails = null;
        // refresh token时查询member信息
        R<MemberSocialDTO> memberR = null;
        switch (accountType) {
            case CustomUser.USER_TYPE_ADMIN:
                R<UserDTO> result = remoteUserService.getUser(username, SecurityConstants.FROM_IN);
                if (result != null) {
                    userDetails = UserDetailsUtil.getAdminUserDetails(result.getData());
                }
                break;
            default:
                break;
        }

        if (userDetails == null) {
            throw new UsernameNotFoundException("Not found the user");
        }

        return userDetails;
    }
}
