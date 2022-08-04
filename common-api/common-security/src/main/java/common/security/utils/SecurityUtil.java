package common.security.utils;

import cn.hutool.core.util.StrUtil;
import common.core.constant.SecurityConstants;
import common.security.model.CustomUser;
import custom.basic.api.constant.enums.SocialType;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author asd <br>
 * @create 2021-06-30 9:37 AM <br>
 * @project custom-upms-grpc <br>
 */
@UtilityClass
public class SecurityUtil {
    /** 获取Authentication */
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /** 获取用户 */
    public CustomUser getUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUser) {
            return (CustomUser) principal;
        } else if (principal instanceof String) {
            String username = (String) principal;
            if (SecurityConstants.ANONYMOUSUSER.equals(username)) {
                return new CustomUser(username, authentication.getAuthorities());
            }
        }
        return null;
    }

    /** 获取用户 */
    public CustomUser getUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        return getUser(authentication);
    }

    /**
     * 获取用户角色信息
     *
     * @return 角色集合
     */
    public List<Integer> getRoles() {
        Authentication authentication = getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        List<Integer> roleIds = new ArrayList<>();
        authorities.stream()
                .filter(
                        granted ->
                                StrUtil.startWith(granted.getAuthority(), SecurityConstants.ROLE))
                .forEach(
                        granted -> {
                            String id =
                                    StrUtil.removePrefix(
                                            granted.getAuthority(), SecurityConstants.ROLE);
                            roleIds.add(Integer.parseInt(id));
                        });
        return roleIds;
    }

    public String getClientId() {
        String clientId = null;
        if (SecurityUtil.getAuthentication() instanceof OAuth2Authentication) {
            OAuth2Authentication authentication =
                    (OAuth2Authentication) SecurityUtil.getAuthentication();
            clientId = authentication.getOAuth2Request().getClientId();
        }
        return clientId;
    }

    /** 获取登录用户社交账号类型 */
    public SocialType getSocialType() {
        String username = SecurityUtil.getUser().getUsername();
        String[] usernameData = username.split(":", 2);
        String accountType = usernameData[0];
        if (StrUtil.isEmpty(accountType)) {
            return null;
        }
        switch (accountType) {
            case CustomUser.ACCOUNT_TYPE_WX:
                return SocialType.WX;
            case CustomUser.ACCOUNT_TYPE_OFFICIAL_ACCOUNT:
                return SocialType.OFFICIAL_ACCOUNT;
            case CustomUser.ACCOUNT_TYPE_MINIAPP:
                return SocialType.WECHAT_MINI_APP;
            case CustomUser.ACCOUNT_TYPE_WX_WORK:
                return SocialType.WECHAT_WORK;

            default:
                return null;
        }
    }
}
