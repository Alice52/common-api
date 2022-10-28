package common.database.config;

import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import common.core.jackson.JavaTimeModule;
import common.database.config.handler.MybatisMetaHandler;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;

/**
 * @author zack <br>
 * @create 2021-04-20 17:28 <br>
 * @project database-mybatis-plus <br>
 */
public abstract class BaseMybatisConfig {
    @Bean
    public GlobalConfig globalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setMetaObjectHandler(new MybatisMetaHandler());
        return globalConfig;
    }

    /**
     * This will help to java8 time standard.
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(Jackson2ObjectMapperBuilderCustomizer.class)
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            builder.locale(Locale.CHINA);
            builder.timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
            builder.simpleDateFormat(DatePattern.NORM_DATETIME_PATTERN);
            builder.modules(new JavaTimeModule());
        };
    }
}
