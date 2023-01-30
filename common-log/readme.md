## feature

1. anno
    - @LogAnnoV2
2. request-id
    - trace-id
3. sls
    - enable: false
4. desensitize
    - 真实情况下, 需要脱敏的数据不是很多: 可以直接 log 手动写(**不推荐**)
    - XxxConverter: 不推荐(可以注解方式)
    - [配置脱敏](https://github.com/liuchengyin01/LogbackDesensitization): **推荐**