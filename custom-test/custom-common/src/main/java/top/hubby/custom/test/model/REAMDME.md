## 关于枚举的使用说明

1. 不建议在 model 中使用枚举作为字段: 可以作为逻辑限制
2. tinyint(1) 在某些框架中会会强制转换为 Boolean, 否则就会为 Null
3. 使用枚举作为字段时
    - 为了查询的方便, 需要存 name 而不是枚举值对象名
    - `@EnumValue` + `mybatis-plus.typeEnumsPackage`
    - `@EnumValue` 在可以接受 name 或者 枚举值对象名
