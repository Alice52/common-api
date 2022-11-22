## notice

1. http-service-support

   - data sync
   - notify func
   - recursive call

2. http-support: ok-client
3. interceptor

   - decryptInterceptor: todo
   - loggingInterceptor
   - authInterceptor
   - retryInterceptor

4. decryptInterceptor

   - type: full | data | item | none
   - full|none: global handle
   - data|item: ~~这里可以通过 setter 内内置解密实现, 但是会有极大的破坏性, 以及相关拦截器的实效~~ **{因此应该考虑在全局做【split - decrypt - combine】}**

5. wip

   - `@Http(Method, Host, xxx)`
   - data decrypt
