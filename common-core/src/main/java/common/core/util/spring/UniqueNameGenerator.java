package common.core.util.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

/**
 * 解决Bean 不唯一问题
 *
 * <pre>
 *     1.  设置bean的默认name为[全类名], 结合 @ComponentScan, @MapperScan 使用
 *     2. Example: @ComponentScan(basePackages="xxx.xxx.xxx", nameGenerator = UniqueNameGenerator.class)
 * </pre>
 *
 * <p>@author asd <br>
 *
 * @create 2022-09-08 03:58 PM <br>
 * @project common-security <br>
 */
public class UniqueNameGenerator extends AnnotationBeanNameGenerator {
    @Override
    protected String buildDefaultBeanName(BeanDefinition definition) {
        return definition.getBeanClassName();
    }
}
