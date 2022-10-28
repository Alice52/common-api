package common.swagger.configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import common.swagger.configuration.properties.SwaggerProperties;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.configuration.OpenApiDocumentationConfiguration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.OAuth;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zack <br>
 * @create 2021-06-21 13:12 <br>
 * @project swagger-3 <br>
 */
@Slf4j
@Configuration
@EnableAutoConfiguration
@ConditionalOnProperty(name = "swagger.enabled", matchIfMissing = true)
@EnableConfigurationProperties(SwaggerProperties.class)
@AutoConfigureAfter(ValidationAutoConfiguration.class)
@Import(OpenApiDocumentationConfiguration.class)
@Deprecated
public class SwaggerAutoConfiguration implements WebMvcConfigurer {

    /** 默认的排除路径，排除Spring Boot默认的错误处理路径和端点 */
    private static final List<String> DEFAULT_EXCLUDE_PATH =
            Arrays.asList("/error", "/actuator/**");

    private static final String BASE_PATH = "/**";
    private static final String LOCALHOST = "localhost";
    @Resource private SwaggerProperties swaggerProperties;

    /**
     * Predicates.and( Predicates.not(Predicates.or(excludePath)), Predicates.or(basePath))
     *
     * @param excludePath
     * @param basePath
     * @return
     */
    @Deprecated
    private static Predicate<String> getPredicate(
            List<Predicate<String>> excludePath, List<Predicate<String>> basePath) {
        Predicate<String> p = x -> false;
        for (Predicate<String> pre : excludePath) {
            p.or(pre);
        }
        p = p.negate();

        Predicate<String> q = x -> false;
        for (Predicate<String> pre : basePath) {
            q.or(pre);
        }

        return p.and(q);
    }

    /**
     * throw-exception-if-no-handler-found: true <br>
     * will lead to no handler exception for static resources. <br>
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
                .resourceChain(false);
        registry.addResourceHandler("/v3/**")
                .addResourceLocations("classpath:/META-INF/resources/**");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/swagger-ui", "/swagger-ui/index.html");
        registry.addRedirectViewController("/api", "/swagger-ui/index.html");
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

        return new Docket(DocumentationType.OAS_30)
                .host(Optional.ofNullable(swaggerProperties.getHost()).orElse(LOCALHOST))
                .apiInfo(apiInfo(swaggerProperties))
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                // .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(getPredicate(excludePath, basePath))
                .build()
                .protocols(new LinkedHashSet<>(Arrays.asList("https", "http")))
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
