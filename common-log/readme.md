## feature

1. anno
   - @LogAnnoV2
2. request-id
   - trace-id
   - [子线程的 trace-id 关联](#async-with-main-thread-traceid)
3. sls
   - enable: false
4. desensitize
   - 真实情况下, 需要脱敏的数据不是很多: 可以直接 log 手动写(**不推荐**)
   - XxxConverter: 不推荐(可以注解方式)
   - [配置脱敏](https://github.com/liuchengyin01/LogbackDesensitization): **推荐**

## trace request

### MCD: Mapped Diagnostic Context

1. 映射诊断环境
2. MDC 是 log4j 和 logback 提供的一种方便在多线程条件下记录日志的功能
3. MDC 可以看成是一个与当前线程绑定的哈希表, 可以往其中添加键值对
4. MDC 的使用方法
   - 向 MDC 中设置值：MDC.put(key, value);
   - 从 MDC 中取值：MDC.get(key);
   - 将 MDC 中内容打印到日志中：%X{key}

### usage

1. Filter + @WebFilter
2. OncePerRequestFilter + @Component
3. HandlerInterceptorAdapter + @Component
4. log xml

   ```xml
   <!-- 之前 -->
   <property name="log.pattern" value="%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{20} - [%method,%line] - %msg%n" />
   <!-- 增加req-id后 -->
   <property name="log.pattern" value="%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{20} - [%method,%line] - [%X{req-id}] - %msg%n" />
   ```

### async with main thread traceid

1. Custom TaskDecorator
2. @EnableAsync + ThreadPoolTaskExecutor + TaskDecorator
3. @Async("threadPoolTaskExecutor")
4. https://mp.weixin.qq.com/s/1FxpPy2jwnOouQDTNJAxeA

### traceid

1. request
2. async thread
3. job
4. rpc
