package custom.test.security.https.config

import io.undertow.UndertowOptions
import io.undertow.servlet.api.SecurityConstraint
import io.undertow.servlet.api.SecurityInfo
import io.undertow.servlet.api.TransportGuaranteeType
import io.undertow.servlet.api.WebResourceCollection
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory
import org.springframework.boot.web.servlet.server.ServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(
    name = arrayOf("server.ssl.enabled"), havingValue = "true", matchIfMissing = false
)
open class HttpsConfigUndertow {

    @Bean
    open fun servletWebServerFactory(): ServletWebServerFactory {
        val factory = UndertowServletWebServerFactory()

        factory.addBuilderCustomizers({
            // it.setServerOption(UndertowOptions.ENABLE_HTTP2, true);
            it.addHttpListener(80, "0.0.0.0")
        })

        val constraint = SecurityConstraint().addWebResourceCollection(WebResourceCollection().addUrlPattern("/*"))
            .setTransportGuaranteeType(TransportGuaranteeType.CONFIDENTIAL)
            .setEmptyRoleSemantic(SecurityInfo.EmptyRoleSemantic.PERMIT);
        factory.addDeploymentInfoCustomizers({
            it.addSecurityConstraint(constraint).setConfidentialPortManager { 443 }
        })

        return factory
    }
}