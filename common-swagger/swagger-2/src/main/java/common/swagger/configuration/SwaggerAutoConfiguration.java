package common.swagger.configuration;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import common.swagger.configuration.properties.SwaggerProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author zack <br>
 * @create 2021-06-01 16:32 <br>
 * @project common-swagger <br>
 */
@Slf4j
@Configuration
@EnableSwagger2
@EnableAutoConfiguration
//@Profile({"dev", "cloud"})
@ConditionalOnProperty(name = "swagger.enabled", matchIfMissing = true)
@EnableConfigurationProperties(SwaggerProperties.class)
public class SwaggerAutoConfiguration implements WebMvcConfigurer {

    /** 默认的排除路径，排除Spring Boot默认的错误处理路径和端点 */
    private static final List<String> DEFAULT_EXCLUDE_PATH =
            Arrays.asList("/error", "/actuator/**");

    private static final String BASE_PATH = "/**";
    private static final String LOCALHOST = "localhost";
    @Resource private SwaggerProperties swaggerProperties;

    /**
     * throw-exception-if-no-handler-found: true <br>
     * will lead to no handler exception for static resources. <br>
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // release swagger
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        // release relevant js
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/swagger-ui", "/swagger-ui.html");
        registry.addRedirectViewController("/api", "/swagger-ui.html");
    }

    @Bean
    public Docket api(SwaggerProperties swaggerProperties) {
        if (swaggerProperties.getBasePath().isEmpty()) {
            swaggerProperties.getBasePath().add(BASE_PATH);
        }
        swaggerProperties.getExcludePath().removeAll(DEFAULT_EXCLUDE_PATH);
        swaggerProperties.getExcludePath().addAll(DEFAULT_EXCLUDE_PATH);

        List<Predicate<String>> basePath =
                swaggerProperties.getBasePath().stream()
                        .map(PathSelectors::ant)
                        .collect(Collectors.toList());

        List<Predicate<String>> excludePath =
                swaggerProperties.getExcludePath().stream()
                        .map(PathSelectors::ant)
                        .collect(Collectors.toList());

        return new Docket(DocumentationType.SWAGGER_2)
                .host(Optional.ofNullable(swaggerProperties.getHost()).orElse(LOCALHOST))
                .apiInfo(apiInfo(swaggerProperties))
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                // .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(
                        Predicates.and(
                                Predicates.not(Predicates.or(excludePath)),
                                Predicates.or(basePath)))
                .build()
                .securitySchemes(Collections.singletonList(securitySchema()))
                .securityContexts(Collections.singletonList(securityContext()))
                .pathMapping(swaggerProperties.getPathMapping());
    }

    /**
     * 配置默认的全局鉴权策略的开关，通过正则表达式进行匹配；默认匹配所有URL
     *
     * @return
     */
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex(swaggerProperties.getAuthorization().getAuthRegex()))
                .build();
    }

    /**
     * 默认的全局鉴权策略
     *
     * @return
     */
    private List<SecurityReference> defaultAuth() {
        List<AuthorizationScope> authorizationScopeList =
                swaggerProperties.getAuthorization().getAuthorizationScopeList().stream()
                        .map(x -> new AuthorizationScope(x.getScope(), x.getDescription()))
                        .collect(Collectors.toList());

        return Collections.singletonList(
                SecurityReference.builder()
                        .reference(swaggerProperties.getAuthorization().getName())
                        .scopes(authorizationScopeList.toArray(new AuthorizationScope[0]))
                        .build());
    }

    private OAuth securitySchema() {
        List<AuthorizationScope> authorizationScopeList =
                swaggerProperties.getAuthorization().getAuthorizationScopeList().stream()
                        .map(x -> new AuthorizationScope(x.getScope(), x.getDescription()))
                        .collect(Collectors.toList());
        List<GrantType> grantTypes =
                swaggerProperties.getAuthorization().getTokenUrlList().stream()
                        .map(ResourceOwnerPasswordCredentialsGrant::new)
                        .collect(Collectors.toList());

        return new OAuth(
                swaggerProperties.getAuthorization().getName(), authorizationScopeList, grantTypes);
    }

    private ApiInfo apiInfo(SwaggerProperties swaggerProperties) {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .version(swaggerProperties.getVersion())
                .build();
    }
}
