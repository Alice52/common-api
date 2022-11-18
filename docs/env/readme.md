## default env

1. mysql
 
    - host: 1.15.129.214
    - port: 3306
    - password: MkGdNTchUL+OnF+nvm5YSSjWc7XeHLNab+KV9Wr3fqF1fAbEVLB2urZotLn8nAk1
    - database: liter-starter
    - version: 8.0.27

2. redis
    
   - host: 1.15.129.214
   - port: 6379
   - password: JIczcGuavcI1nX9NbM0YcDyHiQ834QTPhqmczAw+Qr8Nm6wPeq5p5M076HzPSbFF
   - database: 15

3. sls: default no sls
   
   - endpoint: cn-shanghai.log.aliyuncs.com 
   - access-key-id: ${SLS_ACCESS_ID} 
   - access-key-secret: ${SLS_ACCESS_SECRET} 
   - project: tims-cms-service-local 
   - log-store: tims-cms-service-local-logstore 
   - topic: ${spring.application.name} 
   - level: INFO

4. rabbitmq
   
   - host: 127.0.0.1
   - port: 5672
   - username: guest
   - password: guest

5. oss-aliyun

   - endpoint: oss-cn-hangzhou.aliyuncs.com
   - accessKey: Kf42+pHi8/IU/QWZlOm8UpTTLIMxHWRBWkYENv7a1hO93AkB/rb0G9juNLPXSAfpzYVkX4DGFvUKC1qLooR9Yw==
   - accessKeySecret: zu07F3mfQ5SeGQR2YG3q7OWoRMoJPE73C5DWwqxV8LWyz8MOGpz0viKcdadOBeS/Gp54+V0LWWC8jjWIoVTbnA==
   - bucket: project-ec

6. oss-tencent
   
   - endpoint: ap-shanghai
   - accessKey: FMtjulnXplHmQK9VEtk8Xmged1FJXFLsK4VEX+vVF9hZnoPBdx2pgj6IcOWGoYAocPNYCjaCt5Oo+VIhPnn2lahNDj9Z5+ZNC7AHD6QUEaE=
   - accessKeySecret: rhz4PjHjCedUTtKpwKohsgZXMhNXxBJk+w5XKppx/9PjGzyYmxegLVUzyk4UwrAULYPZjnbeWkBTJFnM+zIdiwGvW9yUKMzjaGpkc4iYsGo=
   - bucket: project-ec-1300043990

7. others
   
   - common_service_ip: 1.15.129.214
   - JASYPT_ENCRYPTOR_PASSWORD: ${JASYPT_ENCRYPTOR_PASSWORD}