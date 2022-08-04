package common.security.grant;

import cn.hutool.core.util.StrUtil;
import common.core.constant.SecurityConstants;
import common.core.util.R;
import common.security.model.CustomUser;
import common.security.model.WebAuthenticationToken;
import custom.basic.api.dto.SmsCodeDTO;
import custom.basic.api.feign.RemoteSmsCodeService;
import custom.basic.api.vo.SmsMemberVO;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author asd <br>
 * @create 2021-06-29 5:30 PM <br>
 * @project custom-upms-grpc <br>
 */
public class MobileTokenGranter extends AbstractTokenGranter {

    public static final String GRANT_TYPE = "mobile";
    private final RemoteSmsCodeService remoteSmsCodeService;

    public MobileTokenGranter(
            AuthorizationServerTokenServices tokenServices,
            ClientDetailsService clientDetailsService,
            OAuth2RequestFactory requestFactory,
            RemoteSmsCodeService remoteSmsCodeService) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.remoteSmsCodeService = remoteSmsCodeService;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(
            ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters =
                new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());
        String mobile = parameters.get("mobile");
        String smsCode = parameters.get("smsCode");
        String appId = parameters.get("appId");
        String signature = parameters.get("signature");

        if (StrUtil.isBlank(mobile)) {
            throw new AuthenticationCredentialsNotFoundException("手机号码不能为空");
        }

        if (StrUtil.isBlank(smsCode)) {
            throw new AuthenticationCredentialsNotFoundException("短信验证码不能为空");
        }

        SmsCodeDTO smsCodeDTO = new SmsCodeDTO();
        smsCodeDTO.setMobile(mobile);
        smsCodeDTO.setSmsCode(smsCode);
        R<SmsMemberVO> result =
                remoteSmsCodeService.validateUser(smsCodeDTO, SecurityConstants.FROM_IN);

        UserDetails userDetails = getUserDetails(result);
        WebAuthenticationToken userAuth = new WebAuthenticationToken(userDetails, null);
        userAuth.setDetails(parameters);
        OAuth2Request storedOAuth2Request =
                getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }

    private UserDetails getUserDetails(R<SmsMemberVO> result) {
        SmsMemberVO info = result.getData();
        if (info == null || info.getMemberId() == null) {
            throw new UsernameNotFoundException("短信登录失败");
        }

        Long memberId = info.getMemberId();
        return new CustomUser(memberId, memberId.toString(), "");
    }
}
