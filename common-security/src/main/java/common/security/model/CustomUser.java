package common.security.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Collections;

/**
 * @author asd <br>
 * @create 2021-06-29 5:54 PM <br>
 * @project custom-upms-grpc <br>
 */
public class CustomUser extends User {
    public static final String USER_TYPE_MEMBER = "member";
    public static final String USER_TYPE_ADMIN = "admin";
    public static final String ACCOUNT_TYPE_MEMBER = "SYSTEM_MEMBER";
    public static final String ACCOUNT_TYPE_ADMIN = "SYSTEM_ADMIN";
    public static final String ACCOUNT_TYPE_WX = "WX_OPENID";
    public static final String ACCOUNT_TYPE_MINIAPP = "MINI_APP_OPENID";
    public static final String ACCOUNT_TYPE_OFFICIAL_ACCOUNT = "OFFICIAL_OPNEID";
    public static final String ACCOUNT_TYPE_WX_WORK = "WORK_OPENID";
    private static final long serialVersionUID = 100L;
    /** User Type: member, admin */
    @Getter private String userType;

    /** 用户ID */
    @Getter private Long id;

    @Getter private Object basicInfo;

    public CustomUser(String username, Collection<? extends GrantedAuthority> authorities) {
        super(username, "", authorities);
    }

    public CustomUser(Long id, String username, String password) {
        super(username, password, Collections.emptyList());
        this.id = id;
    }

    public CustomUser(
            Long id,
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
    }

    public CustomUser(Long id, String username, String password, Object basicInfo) {
        super(username, password, Collections.emptyList());
        this.id = id;
        this.basicInfo = basicInfo;
    }

    public CustomUser(
            Long id,
            String userType,
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
        this.userType = userType;
    }
}
