package custom.test.security.https.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping
@RestController
class HttpsController {


    @GetMapping("/hello/{name}")
    fun hello(@PathVariable name: String): String = "hello ${name}"
}