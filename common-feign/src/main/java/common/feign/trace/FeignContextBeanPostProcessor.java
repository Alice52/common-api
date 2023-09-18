package common.feign.trace;

import feign.opentracing.FeignSpanDecorator;
import io.opentracing.Tracer;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.openfeign.FeignContext;

import java.util.List;

/**
 * @author T04856 <br/>
 * @create 2023-06-01 3:37 PM <br/>
 * @project common-api <br/>
 */
@AllArgsConstructor
public class FeignContextBeanPostProcessor implements BeanPostProcessor {

    private final Tracer tracer;
    private final BeanFactory beanFactory;
    private final List<FeignSpanDecorator> spanDecorators;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String name) throws BeansException {
        if (bean instanceof FeignContext && !(bean instanceof TraceFeignContext)) {
            return new TraceFeignContext(tracer, (FeignContext) bean, beanFactory, spanDecorators);
        }
        return bean;
    }
}
