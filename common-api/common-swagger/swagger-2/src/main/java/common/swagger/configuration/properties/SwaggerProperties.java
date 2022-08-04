package common.swagger.configuration.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zack <br>
 * @create 2021-06-01 16:48 <br>
 * @project common-swagger <br>
 */
@Data
@ConfigurationProperties("swagger")
public class SwaggerProperties {
    /** 是否开启swagger */
    private Boolean enabled;
    /** swagger会解析的包路径 */
    private String basePackage = "";
    /** swagger会解析的url规则 */
    private List<String> basePath = new ArrayList<>();
    /** 在basePath基础上需要排除的url规则 */
    private List<String> excludePath = new ArrayList<>();
    /** 标题 */
    private String title = "";
    /** 描述 */
    private String description = "";
    /** 版本 */
    private String version = "";

    /** host信息 */
    private String host = "";

    /** host信息 */
    private String pathMapping = "";

    /** 全局统一鉴权配置 */
    private Authorization authorization = new Authorization();

    @Data
    @NoArgsConstructor
    public static class Authorization {

        /** 鉴权策略ID，需要和SecurityReferences ID保持一致 */
        private String name = "";

        /** 需要开启鉴权URL的正则 */
        private String authRegex = "^.*$";

        /** 鉴权作用域列表 */
        private List<AuthorizationScope> authorizationScopeList = new ArrayList<>();

        private List<String> tokenUrlList = new ArrayList<>();
    }

    @Data
    @NoArgsConstructor
    public static class AuthorizationScope {

        /** 作用域名称 */
        private String scope = "";

        /** 作用域描述 */
        private String description = "";
    }
}
