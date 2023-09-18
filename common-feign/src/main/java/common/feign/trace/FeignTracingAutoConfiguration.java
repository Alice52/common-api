package common.feign.trace;

import feign.Client;
import feign.Request;
import feign.opentracing.FeignSpanDecorator;
import feign.opentracing.TracingClient;
import io.opentracing.Tracer;
import io.opentracing.contrib.spring.tracer.configuration.TracerAutoConfiguration;
import lombok.NoArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.List;

/**
 * @author T04856 <br>
 * @create 2023-06-01 3:32 PM <br>
 * @project common-api <br>
 */
@Configuration
@ConditionalOnClass(Client.class)
@AutoConfigureAfter(TracerAutoConfiguration.class)
@AutoConfigureBefore(name = "org.springframework.cloud.openfeign.FeignAutoConfiguration")
@ConditionalOnProperty(
        name = "opentracing.spring.cloud.feign.enabled",
        havingValue = "true",
        matchIfMissing = true)
@NoArgsConstructor
public class FeignTracingAutoConfiguration {

    @Autowired @Lazy private Tracer tracer;

    @Lazy
    @Autowired(required = false)
    private List<FeignSpanDecorator> spanDecorators;

    @Bean
    @ConditionalOnClass(name = "org.springframework.cloud.openfeign.FeignContext")
    FeignContextBeanPostProcessor feignContextBeanPostProcessor(BeanFactory beanFactory) {
        return new FeignContextBeanPostProcessor(tracer, beanFactory, spanDecorators);
    }

    @Bean
    public TracingAspect tracingAspect() {
        return new TracingAspect();
    }

    /** Trace feign clients created manually */
    @Aspect
    class TracingAspect {
        @Around("execution (* feign.Client.*(..)) && !within(is(FinalType))")
        public Object feignClientWasCalled(ProceedingJoinPoint pjp) throws Throwable {
            Object bean = pjp.getTarget();

            if (bean instanceof TracingClient) {
                return pjp.proceed();
            }

            Object[] args = pjp.getArgs();
            return new TracingClientBuilder((Client) bean, tracer)
                    .withFeignSpanDecorators(spanDecorators)
                    .build()
                    .execute((Request) args[0], (Request.Options) args[1]);
        }
    }
}
