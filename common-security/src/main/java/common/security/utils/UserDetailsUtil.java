package common.security.utils;

import cn.hutool.core.util.ArrayUtil;
import common.core.constant.SecurityConstants;
import common.security.model.CustomUser;
import custom.basic.api.dto.MemberSocialDTO;
import custom.basic.api.dto.UserDTO;
import custom.basic.api.entity.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author asd <br>
 * @create 2021-06-30 9:28 AM <br>
 * @project custom-upms-grpc <br>
 */
public final class UserDetailsUtil {

    public static CustomUser getAdminUserDetails(UserDTO userInfo) {
        if (userInfo == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        Set<String> dbAuthsSet = new HashSet<>();
        if (ArrayUtil.isNotEmpty(userInfo.getRoles())) {
            // 获取角色
            Arrays.stream(userInfo.getRoles())
                    .forEach(role -> dbAuthsSet.add(SecurityConstants.ROLE + role));
            // 获取资源
            dbAuthsSet.addAll(Arrays.asList(userInfo.getPermissions()));
        }
        Collection<? extends GrantedAuthority> authorities =
                AuthorityUtils.createAuthorityList(dbAuthsSet.toArray(new String[0]));
        SysUser user = userInfo.getSysUser();

        String username = CustomUser.USER_TYPE_ADMIN + ":" + user.getUsername();

        return new CustomUser(
                user.getUserId(),
                CustomUser.USER_TYPE_ADMIN,
                username,
                SecurityConstants.BCRYPT + user.getPassword(),
                authorities);
    }

    public static CustomUser getMemberUserDetails(MemberSocialDTO memberSocialVO, String username) {
        if (memberSocialVO == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        Long memberId = memberSocialVO.getMemberId();
        if (memberId != null) {
            username = CustomUser.USER_TYPE_MEMBER + ":" + memberId;
        }

        return new CustomUser(memberId, CustomUser.USER_TYPE_MEMBER, username, "");
    }
}
