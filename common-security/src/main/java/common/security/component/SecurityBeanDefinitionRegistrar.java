package common.security.component;

import common.core.constant.SecurityConstants;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author asd <br>
 * @create 2021-06-29 2:53 PM <br>
 * @project custom-upms-grpc <br>
 */
@Slf4j
@Import(CustomResourceServerConfigurerAdapter.class)
public class SecurityBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
    /**
     * 根据注解值动态注入资源服务器的相关属性
     *
     * @param metadata 注解信息
     * @param registry 注册器
     */
    @Override
    public void registerBeanDefinitions(
            AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        if (registry.isBeanNameInUse(SecurityConstants.RESOURCE_SERVER_CONFIGURER)) {
            log.warn("本地存在资源服务器配置，覆盖默认配置:" + SecurityConstants.RESOURCE_SERVER_CONFIGURER);
            return;
        }

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(CustomResourceServerConfigurerAdapter.class);
        registry.registerBeanDefinition(
                SecurityConstants.RESOURCE_SERVER_CONFIGURER, beanDefinition);
    }
}
