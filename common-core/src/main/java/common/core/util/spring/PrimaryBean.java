package common.core.util.spring;

import common.core.exception.handler.ExceptionHandlerSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import java.time.LocalDateTime;

/**
 * dynamic set primary key.
 *
 * @author asd <br>
 * @create 2022-09-08 03:56 PM <br>
 * @project common-security <br>
 */
@Slf4j
@Order
// @Configuration
public class PrimaryBean {

    @Bean
    @ConditionalOnBean(ExceptionHandlerSupport.class)
    public BeanFactoryPostProcessor beanFactoryPostProcessor() {
        return this::setPrimaryMemberService;
    }

    public void setPrimaryMemberService(ConfigurableListableBeanFactory beanFactory) {

        String[] beanNames = beanFactory.getBeanNamesForType(ExceptionHandlerSupport.class);
        if (beanNames.length <= 0) {
            return;
        }

        for (String name : beanNames) {
            if (name.contains(String.valueOf(LocalDateTime.now().getYear()))) {
                BeanDefinition beanDefinition = beanFactory.getBeanDefinition(name);
                beanDefinition.setPrimary(true);
                break;
            }
        }
    }
}
