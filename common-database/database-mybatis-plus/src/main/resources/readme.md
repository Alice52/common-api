## flyway

0. 数据库不存在则直接创建数据库配置: jdbc 配置
    - createDatabaseIfNotExist=true
    - rewriteBatchedStatements=true
    - allowMultiQueries=true
    - autoReconnect=true
    - serverTimezone=Asia/Shanghai
    - zeroDateTimeBehavior=convertToNull
    - useLegacyDatetimeCode=false
    - useUnicode=true
    - characterEncoding=utf8
    - useSSL=false
    - allowPublicKeyRetrieval=true
    - useJDBCCompliantTimezoneShift=true

1. config

   ```yml
   spring:
   flyway:
     enabled: false
     user: ENC(3+5Pg+H9L0s1ATmg/xTWLbNt9OeMFguochuRyTl9a4BGbD4lL2SLM8351rdHYhC2)
     password: ENC(MkGdNTchUL+OnF+nvm5YSSjWc7XeHLNab+KV9Wr3fqF1fAbEVLB2urZotLn8nAk1)
     url: ENC(ODg2HaThw6347pLMI7UzSCRU/FzNUPuITeCgAGQMpIzvnDc6pN7X4wc8G3K6i9c4iFy6LvNTfnZgnZGYa3bA3za4tc3nbvj/dIf3yofN/5mMQPYAwRv8bp92Y0c8MGW1rOsxQyUv9YwGc4tNcWWfylJFj3GxOXtp1LTNpMmw2VkVBnBlsk47zvk3qqtHPJJJ5JnFD/Pf32XRdTzXc3aOgDkqrQ9e+UALplaXoV4x+PQ8g3vusKOfbayCuaerFL/orG6MG22UWf1MlFUu6vM1NhY81n5RMK6P7c7VWN+SECxWnhqDtpu+74/gEfs1D9EeMh9t19L5bULFKcKstdKdJRUus6Rvk3KA+LFTwAz08K1iCQXpRzvv6poZhy2UCFb9eVZqMpFJ3NjfrpjA7HmI69WyEoR4Uj7HG4JgeX9WsxjE8G0CHHMVPQJR3TvQF17x)
     baseline-version: 1.0.1
     baseline-on-migrate: true
     table: flyway_schema_history
     locations: classpath*:db/migration
   ```

    - flyway.baseline-description 对执行迁移时基准版本的描述.
    - flyway.baseline-on-migrate 当迁移时发现目标 schema 非空, 而且带有没有元数据的表时, 是否自动执行基准迁移, 默认
      false.
    - flyway.baseline-version 开始执行基准迁移时对现有的 schema 的版本打标签, 默认值为 1.
    - flyway.check-location 检查迁移脚本的位置是否存在, 默认 false.
    - flyway.clean-on-validation-error 当发现校验错误时是否自动调用 clean, 默认 false.
    - flyway.enabled 是否开启 flyway, 默认 true.
    - flyway.encoding 设置迁移时的编码, 默认 UTF-8.
    - flyway.ignore-failed-future-migration 当读取元数据表时是否忽略错误的迁移, 默认 false.
    - flyway.init-sqls 当初始化好连接时要执行的 SQL.
    - flyway.locations 迁移脚本的位置, 默认 db/migration.
    - flyway.out-of-order 是否允许无序的迁移, 默认 false.
    - flyway.password 目标数据库的密码.
    - flyway.placeholder-prefix 设置每个 placeholder 的前缀, 默认${.
    - flyway.placeholder-replacementplaceholders 是否要被替换, 默认 true.
    - flyway.placeholder-suffix 设置每个 placeholder 的后缀, 默认}.
    - flyway.placeholders.[placeholder name]设置 placeholder 的 value
    - flyway.schemas 设定需要 flyway 迁移的 schema, 大小写敏感, 默认为连接默认的 schema.
    - flyway.sql-migration-prefix 迁移文件的前缀, 默认为 V.
    - flyway.sql-migration-separator 迁移脚本的文件名分隔符, 默认\_\_
    - flyway.sql-migration-suffix 迁移脚本的后缀, 默认为.sql
    - flyway.tableflyway 使用的元数据表名, 默认为 schema_version
    - flyway.target 迁移时使用的目标版本, 默认为 latest version
    - flyway.url 迁移时使用的 JDBC URL, 如果没有指定的话, 将使用配置的主数据源
    - flyway.user 迁移数据库的用户名
    - flyway.validate-on-migrate 迁移时是否校验, 默认为 true.

2. version control: V 开头的 SQL 执行优先级要比 R 开头的 SQL 优先级高
   ![image](https://github.com/fork-repoes/COLA/assets/42330329/aee2b01c-6711-4b50-bf9c-34ba42848b19)

    - 版本号: `数字间以.或_分隔开`

    - V(仅执行一次的 SQL): V+版本号+双下划线+文件描述+后缀名
        1. `V20201100__create_user.sql`
        2. `V2.1.5__create_user_ddl.sql`
        3. `V4.1_2__add_user_dml.sql`
    - R(可重复运行的 SQL): R+版本号+双下划线+文件描述+后缀名
        1. `R__2.0_Init_column.sql`
        2. `R__2.1_Init_data.sql`
        3. `R__2.2_Init_Index.sql`

3. directory explain
    - init_db_script: this is full db script
    - db.migration: this folder will be executable script(V|U|R)

---

## reference

1. https://blog.csdn.net/qianzhitu/article/details/110629847
2. https://blog.csdn.net/MyronCham/article/details/119184345
