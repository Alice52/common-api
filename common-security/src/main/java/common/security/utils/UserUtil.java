package common.security.utils;

import common.security.model.CustomUser;
import lombok.extern.slf4j.Slf4j;

/**
 * @author asd <br>
 * @create 2021-06-30 9:47 AM <br>
 * @project custom-upms-grpc <br>
 */
@Slf4j
public final class UserUtil {

    /**
     * Get member id.
     *
     * @return
     */
    public static Long getCurrentMemberId() {
        CustomUser mcUser = SecurityUtil.getUser();
        if (mcUser == null) {
            return null;
        }

        Long memberId = null;
        if (CustomUser.USER_TYPE_MEMBER.equals(mcUser.getUserType())) {
            return mcUser.getId();
        }

        return memberId;
    }

    /**
     * Get adin user id.
     *
     * @return
     */
    public static Long getCurrentUserId() {
        CustomUser mcUser = SecurityUtil.getUser();
        if (mcUser == null) {
            return null;
        }

        Long userId = null;
        if (CustomUser.USER_TYPE_ADMIN.equals(mcUser.getUserType())) {
            return mcUser.getId();
        }

        return userId;
    }

    public static boolean validateMemberId(Long memberId) {
        return memberId != null && memberId > 0;
    }
}
