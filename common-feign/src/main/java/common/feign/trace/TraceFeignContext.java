package common.feign.trace;

import feign.Client;
import feign.opentracing.FeignSpanDecorator;
import io.opentracing.Tracer;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.cloud.openfeign.FeignContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author T04856 <br/>
 * @create 2023-06-01 3:38 PM <br/>
 * @project common-api <br/>
 */
public class TraceFeignContext  extends FeignContext {

    private final FeignContext delegate;
    private final TracedFeignBeanFactory tracedFeignBeanFactory;

    TraceFeignContext(Tracer tracer, FeignContext delegate, BeanFactory beanFactory,
                      List<FeignSpanDecorator> spanDecorators) {
        this.delegate = delegate;
        tracedFeignBeanFactory = new TracedFeignBeanFactory(tracer, beanFactory, spanDecorators);
    }

    @Override
    public <T> T getInstance(String name, Class<T> type) {
        T object = delegate.getInstance(name, type);
        if (object == null && type.isAssignableFrom(Client.class)) {
            object = (T) new Client.Default(null, null);
        }

        return (T) addTracingClient(object);
    }

    @Override
    public <T> Map<String, T> getInstances(String name, Class<T> type) {
        Map<String, T> instances = delegate.getInstances(name, type);
        if (instances == null) {
            return null;
        }
        Map<String, T> tracedInstances = new HashMap<>();
        for (Map.Entry<String, T> instanceEntry : instances.entrySet()) {
            tracedInstances
                    .put(instanceEntry.getKey(), (T) addTracingClient(instanceEntry.getValue()));
        }
        return tracedInstances;
    }

    private Object addTracingClient(Object bean) {
        return tracedFeignBeanFactory.from(bean);
    }

}
