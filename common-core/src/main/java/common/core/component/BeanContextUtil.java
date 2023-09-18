package common.core.component;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Component;

/**
 * @author zack <br>
 * @create 2020-12-28<br>
 * @project common-coding <br>
 */
@Component
public class BeanContextUtil implements BeanFactoryAware {
    private static BeanFactory beanFactory;

    public static <T> T getBean(String beanName) {
        if (beanFactory.containsBean(beanName)) {
            return (T) beanFactory.getBean(beanName);
        } else {
            return null;
        }
    }

    /**
     * This method will throw {@link
     * org.springframework.beans.factory.NoSuchBeanDefinitionException}
     *
     * @param clazz
     * @param <T>
     * @return
     */
    @Deprecated
    public static <T> T getBean(Class<T> clazz) {

        return beanFactory.getBean(clazz);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        BeanContextUtil.beanFactory = beanFactory;
    }
}
