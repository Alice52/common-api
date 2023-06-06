package common.feign.trace;

import feign.Client;
import feign.opentracing.FeignSpanDecorator;
import feign.opentracing.TracingClient;
import io.opentracing.Tracer;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.openfeign.loadbalancer.FeignBlockingLoadBalancerClient;
import org.springframework.context.annotation.Lazy;

import java.util.List;

/**
 * @author T04856 <br>
 * @create 2023-06-01 3:39 PM <br>
 * @project common-api <br>
 */
public class TracedFeignBeanFactory {

    private final Tracer tracer;
    private final BeanFactory beanFactory;
    private final List<FeignSpanDecorator> spanDecorators;

    public TracedFeignBeanFactory(
            Tracer tracer, BeanFactory beanFactory, @Lazy List<FeignSpanDecorator> spanDecorators) {
        this.tracer = tracer;
        this.beanFactory = beanFactory;
        this.spanDecorators = spanDecorators;
    }

    public Object from(Object bean) {
        if (bean instanceof TracingClient || bean instanceof FeignBlockingLoadBalancerTraceClient) {
            return bean;
        }

        if (bean instanceof Client) {
            if (bean instanceof FeignBlockingLoadBalancerClient) {
                FeignBlockingLoadBalancerClient feignBlockingLoadBalancerClient =
                        (FeignBlockingLoadBalancerClient) bean;
                TracingClient tracingClient =
                        buildTracingClient(feignBlockingLoadBalancerClient.getDelegate(), tracer);
                return new FeignBlockingLoadBalancerTraceClient(
                        tracingClient,
                        beanFactory.getBean(LoadBalancerClient.class),
                        beanFactory.getBean(LoadBalancerProperties.class),
                        beanFactory.getBean(LoadBalancerClientFactory.class));
            }
            return buildTracingClient((Client) bean, tracer);
        }
        return bean;
    }

    private TracingClient buildTracingClient(Client delegate, Tracer tracer) {
        return new TracingClientBuilder(delegate, tracer)
                .withFeignSpanDecorators(spanDecorators)
                .build();
    }

    static class FeignBlockingLoadBalancerTraceClient extends FeignBlockingLoadBalancerClient {
        public FeignBlockingLoadBalancerTraceClient(
                Client delegate,
                LoadBalancerClient loadBalancerClient,
                LoadBalancerProperties properties,
                LoadBalancerClientFactory loadBalancerClientFactory) {
            super(delegate, loadBalancerClient, properties, loadBalancerClientFactory);
        }
    }
}
