package common.security.listener;

import cn.hutool.core.collection.CollUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;

/**
 * @author asd <br>
 * @create 2021-06-30 8:51 AM <br>
 * @project custom-upms-grpc <br>
 */
public abstract class AbstractAuthSuccessEventHandler
        implements ApplicationListener<AuthenticationSuccessEvent> {
    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Authentication authentication = (Authentication) event.getSource();
        if (CollUtil.isNotEmpty(authentication.getAuthorities())) {
            handle(authentication);
        }
    }

    /**
     * 处理登录成功方法
     *
     * <p>获取到登录的authentication 对象
     *
     * @param authentication 登录对象
     */
    public abstract void handle(Authentication authentication);
}
