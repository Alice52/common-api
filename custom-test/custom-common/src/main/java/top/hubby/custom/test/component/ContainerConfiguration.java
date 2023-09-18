package top.hubby.custom.test.component;

import io.undertow.server.DefaultByteBufferPool;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

/**
 * @author zack <br>
 * @create 2021-04-20 20:15 <br>
 * @project custom-test <br>
 */
@Component
public class ContainerConfiguration
        implements WebServerFactoryCustomizer<UndertowServletWebServerFactory> {

    /**
     * Fix UT026010 warning.
     *
     * @param factory
     */
    @Override
    public void customize(UndertowServletWebServerFactory factory) {
        factory.addDeploymentInfoCustomizers(
                deploymentInfo -> {
                    WebSocketDeploymentInfo webSocketDeploymentInfo = new WebSocketDeploymentInfo();
                    webSocketDeploymentInfo.setBuffers(new DefaultByteBufferPool(false, 1024));
                    deploymentInfo.addServletContextAttribute(
                            "io.undertow.websockets.jsr.WebSocketDeploymentInfo",
                            webSocketDeploymentInfo);
                });
    }
}
