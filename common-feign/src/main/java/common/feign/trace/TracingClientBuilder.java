package common.feign.trace;

import feign.Client;
import feign.opentracing.FeignSpanDecorator;
import feign.opentracing.TracingClient;
import io.opentracing.Tracer;

import java.util.List;

/**
 * @author T04856 <br/>
 * @create 2023-06-01 3:35 PM <br/>
 * @project common-api <br/>
 */
public class TracingClientBuilder {

    private final Client delegate;
    private final Tracer tracer;
    private List<FeignSpanDecorator> decorators;

    TracingClientBuilder(Client delegate, Tracer tracer) {
        this.delegate = delegate;
        this.tracer = tracer;
    }

    TracingClientBuilder withFeignSpanDecorators(List<FeignSpanDecorator> decorators) {
        this.decorators = decorators;
        return this;
    }

    TracingClient build() {
        if (decorators == null || decorators.isEmpty()) {
            return new TracingClient(delegate, tracer);
        }
        return new TracingClient(delegate, tracer, decorators);
    }
}
