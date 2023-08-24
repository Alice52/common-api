package custom.test.security.https

import common.swagger.annotation.EnableSwagger
import io.github.alice52.common.inject.annotation.SimpleBootApplication
import org.springframework.boot.runApplication


@EnableSwagger
@SimpleBootApplication
open class HttpsApplication

fun main(vararg args: String) {
    runApplication<HttpsApplication>(*args)
}
