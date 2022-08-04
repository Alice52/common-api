package common.http.constant;

import common.core.constant.SecurityConstants;

/**
 * @author asd <br>
 * @create 2021-12-07 4:23 PM <br>
 * @project project-cloud-custom <br>
 */
public interface Constants {

    /* ============== cache ====================*/
    String SMT_PREFIX = SecurityConstants.PROJECT_PREFIX + ":http";

    String BIZ_TOKEN = SMT_PREFIX + ":biz_token";
    String BIZ_DECRYPT_TOKEN = SMT_PREFIX + ":biz_decrypt_token";

    /* ==================== http relative ======= */
    // value is string of boolean
    String API_AUTH_FLAG = "no-auth";
    String URL_ACCESS_TOKEN = "/hr/bearerToken/query";
    String URL_PREFIX = "/hitf/v1/rest/invoke?namespace=HZERO&serverCode=";
    String URL_SUFFIX = "&interfaceCode=hcbb-mdm.homdm-interface-execute.query";
    String PAYLOAD = "payload";
    String RESPONSE_DATA = "responseData";
    String SUCCESS = "S";

    /* ==================== token relative ======= */
    String GRANT_TYPE = "grant_type";
    String CLIENT_CREDENTIALS = "client_credentials";
    String CLIENT_ID = "client_id";
    String CLIENT_SECRET = "client_secret";
    String AUTHORIZATION = "Authorization";
}
