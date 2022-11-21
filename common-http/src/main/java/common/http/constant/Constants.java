package common.http.constant;

import common.core.constant.SecurityConstants;

/**
 * @author asd <br>
 * @create 2021-12-07 4:23 PM <br>
 * @project project-cloud-custom <br>
 */
public interface Constants {
    String GRANT_TYPE = "grant_type";
    String CLIENT_ID = "client_id";
    String CLIENT_SECRET = "client_secret";
    String AUTHORIZATION = "Authorization";

    // value is string of boolean
    String API_AUTH_FLAG = "no-auth";

    // cache
    String HTTP_PREFIX = SecurityConstants.PROJECT_PREFIX + ":http";




    /* ==================== http relative ======= */

    String URL_ACCESS_TOKEN = "/hr/bearerToken/query";
    String URL_PREFIX = "/hitf/v1/rest/invoke?namespace=HZERO&serverCode=";
    String URL_SUFFIX = "&interfaceCode=hcbb-mdm.homdm-interface-execute.query";
    String PAYLOAD = "payload";
    String RESPONSE_DATA = "responseData";
    String SUCCESS = "S";

}
