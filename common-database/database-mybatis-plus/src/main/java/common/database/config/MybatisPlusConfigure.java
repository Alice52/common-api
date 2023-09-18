package common.database.config;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import common.database.config.handler.MybatisMetaHandler;
import common.database.interceptor.DeSensitiveFieldInterceptor;
import common.database.interceptor.QuerySensitiveInterceptor;
import common.database.interceptor.SensitiveFieldInterceptor;
import common.database.plugin.SensitivePlugin;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zack <br>
 * @create 2021-04-09 12:29 <br>
 * @project database-mybatis-plus <br>
 */
@Configuration
public class MybatisPlusConfigure extends BaseMybatisConfig {

    @Bean
    @Override
    public GlobalConfig globalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setMetaObjectHandler(new MybatisMetaHandler());
        return globalConfig;
    }

    //    /**
    //     * 分页插件
    //     *
    //     * @return PaginationInterceptor
    //     */
    //    @Bean
    //    public PaginationInterceptor paginationInterceptor() {
    //        return new PaginationInterceptor();
    //    }

    /**
     * 数据权限插件
     *
     * @return DataScopeInterceptor
     */
    //    @Bean
    //    public DataScopeInterceptor dataScopeInterceptor() {
    //        return new DataScopeInterceptor();
    //    }

    /**
     * 默认逻辑删除
     *
     * @return
     */
    // @Bean
    public ISqlInjector sqlInjector() {
        return new DefaultSqlInjector();
    }

    /**
     * SQL执行效率插件 <br>
     * 且只有在 HikariDataSource 下才会生效参数<br>
     *
     * @return
     */
    //    @Bean
    //    @Profile({"dev", "cloud"})
    //    public PerformanceInterceptor performanceInterceptor() {
    //        return new PerformanceInterceptor();
    //    }

    @Bean
    @Deprecated
    @ConditionalOnProperty(
            prefix = "common.global.database",
            value = {"deprecated-sensitive"},
            havingValue = "true",
            matchIfMissing = false)
    public SensitivePlugin sensitivePlugin() {
        return new SensitivePlugin();
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "common.global.database",
            value = {"sensitive"},
            havingValue = "true",
            matchIfMissing = true)
    public SensitiveFieldInterceptor sensitiveFieldInterceptor() {
        return new SensitiveFieldInterceptor();
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "common.global.database",
            value = {"sensitive"},
            havingValue = "true",
            matchIfMissing = true)
    public DeSensitiveFieldInterceptor deSensitiveFieldInterceptor() {
        return new DeSensitiveFieldInterceptor();
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "common.global.database",
            value = {"sensitive"},
            havingValue = "true",
            matchIfMissing = true)
    public QuerySensitiveInterceptor querySensitiveInterceptor() {
        return new QuerySensitiveInterceptor();
    }
}
