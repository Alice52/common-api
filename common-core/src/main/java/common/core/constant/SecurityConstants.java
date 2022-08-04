package common.core.constant;

/**
 * @author asd <br>
 * @create 2021-06-29 3:12 PM <br>
 * @project custom-upms-grpc <br>
 */
public interface SecurityConstants {
    /** 角色前缀 */
    String ROLE = "ROLE_";

    /** 前缀 */
    String PROJECT_PREFIX = "custom_cloud";

    // oauth
    String OAUTH_PREFIX = PROJECT_PREFIX + ":oauth:";
    String OAUTH_ACCESS_KEY_PREFIX = OAUTH_PREFIX + "access:";

    String FROM = "from";
    String FROM_IN = "Y";

    // oauth 客户端信息
    String CLIENT_DETAILS_KEY = OAUTH_PREFIX + "client:details";

    // cache
    String ADMIN_USER_DETAILS_KEY = PROJECT_PREFIX + ":admin:user_details";

    /** grant_type */
    String REFRESH_TOKEN = "refresh_token";

    String ANONYMOUSUSER = "anonymousUser";

    String GRANT_TYPE_PASSWORD = "password";

    /** {bcrypt} 加密的特征码 */
    String BCRYPT = "{bcrypt}";
    /** sys_oauth_client_details 表的字段，不包括client_id、client_secret */
    String CLIENT_FIELDS =
            "client_id, CONCAT('{noop}',client_secret) as client_secret, resource_ids, scope, "
                    + "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, "
                    + "refresh_token_validity, additional_information, autoapprove";

    /** JdbcClientDetailsService 查询语句 */
    String BASE_FIND_STATEMENT =
            "select " + CLIENT_FIELDS + " from custom_sys_oauth_client_details";

    /** 默认的查询语句 */
    String DEFAULT_FIND_STATEMENT = BASE_FIND_STATEMENT + " order by client_id";

    /** 按条件client_id 查询 */
    String DEFAULT_SELECT_STATEMENT = BASE_FIND_STATEMENT + " where client_id = ?";

    /***
     * 资源服务器默认bean名称
     */
    String RESOURCE_SERVER_CONFIGURER = "resourceServerConfigurerAdapter";
}
