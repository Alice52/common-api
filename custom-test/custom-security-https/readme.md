## cert generate

1. 首先使用 jdk 自带的 keytool 命令生成证书复制到项目的 resources 目录下

   ```shell
    # genkey：表示要创建一个新的密钥。
    # alias：表示 keystore 的别名。
    # keyalg：表示使用的加密算法是 RSA ，一种非对称加密算法。
    # keysize：表示密钥的长度。
    # keystore：表示生成的密钥存放位置。
    # validity：表示密钥的有效时间，单位为天。
    keytool -genkey -alias undertowhttps -keyalg RSA -keysize 2048 -keystore E:/httpsKey.p12 -validity 365
   ```

2. config in yml

   ```yml
   server:
     ssl:
       enabled: true
       key-store: classpath:httpsKey.p12
       key-alias: undertowhttps
       key-store-type: JKS
       key-store-password: 123456
     port: 443
   ```

3. https

   - http redirect to https
   - https configuarion: 配置是容器的 Context 使用 SecurityConstraint(url-pattern & Constraint)

4. tomcat https

   - http redirect to https

     ```kotlin
     @Bean
     fun connector(): Connector {
         val connector = Connector("org.apache.coyote.http11.Http11NioProtocol")
         connector.scheme = "http"
         connector.port = 80
         connector.secure = false
         connector.redirectPort = 443
         return connector
     }
     ```

   - enable https config

     ```kotlin
     @Bean
     fun tomcatServletWebServerFactory(connector: Connector): TomcatServletWebServerFactory {
         val tomcat = object : TomcatServletWebServerFactory() {
            override fun postProcessContext(context: Context) {
               val securityConstraint = SecurityConstraint()
               securityConstraint.userConstraint = "CONFIDENTIAL"
               val collection = SecurityCollection()
               collection.addPattern("/*")
               securityConstraint.addCollection(collection)
               context.addConstraint(securityConstraint)
            }
         }
         tomcat.addAdditionalTomcatConnectors(connector)
         return tomcat
     }
     ```

5. undertow https

   - http redirect to https

     ```kotlin
     @Bean
     open fun servletWebServerFactory(): ServletWebServerFactory {
       val factory = UndertowServletWebServerFactory()

       factory.addBuilderCustomizers({
           // it.setServerOption(UndertowOptions.ENABLE_HTTP2, true);
           it.addHttpListener(80, "0.0.0.0")
       })

       factory.addDeploymentInfoCustomizers({
           it.setConfidentialPortManager { 443 }
       })

       return factory
     }
     ```

   - enable https config

     ```kotlin
     @Bean
     open fun servletWebServerFactory(): ServletWebServerFactory {
        val factory = UndertowServletWebServerFactory()
        val constraint = SecurityConstraint()
           .addWebResourceCollection(WebResourceCollection().addUrlPattern("/*"))
           .setTransportGuaranteeType(TransportGuaranteeType.CONFIDENTIAL)
           .setEmptyRoleSemantic(SecurityInfo.EmptyRoleSemantic.PERMIT);
        factory.addDeploymentInfoCustomizers({
           it.addSecurityConstraint(constraint)
        })

        return factory
     }
     ```
