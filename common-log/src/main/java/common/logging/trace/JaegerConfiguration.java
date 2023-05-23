package common.logging.trace;

import io.jaegertracing.internal.MDCScopeManager;
import io.jaegertracing.internal.reporters.CompositeReporter;
import io.jaegertracing.internal.reporters.LoggingReporter;
import io.jaegertracing.spi.Reporter;
import io.opentracing.contrib.java.spring.jaeger.starter.JaegerConfigurationProperties;
import io.opentracing.contrib.java.spring.jaeger.starter.ReporterAppender;
import io.opentracing.contrib.java.spring.jaeger.starter.TracerBuilderCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.LinkedList;
import java.util.List;

@Configuration
public class JaegerConfiguration {

    public static final String TRACE_ID_NAME = "X-B3-TraceId";
    public static final String SPAN_ID_NAME = "X-B3-SpanId";
    public static final String SAMPLED_NAME = "X-B3-Sampled";

    @Bean
    public TracerBuilderCustomizer mdcBuilderCustomizer() {
        return builder -> builder.withScopeManager(new MDCScopeManager.Builder().
                withMDCSpanIdKey(SPAN_ID_NAME).
                withMDCTraceIdKey(TRACE_ID_NAME).
                withMDCSampledKey(SAMPLED_NAME).
                build());
    }

//    @Bean
//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    @ConditionalOnProperty(prefix = "opentracing.jaeger.neverReport", value = "enabled", matchIfMissing = true)
//    public Reporter reporter(JaegerConfigurationProperties properties,
//                             @Autowired(required = false) ReporterAppender reporterAppender) {
//        List<Reporter> reporters = new LinkedList<>();
//        if (properties.isLogSpans()) {
//            reporters.add(new LoggingReporter());
//        }
//        if (reporterAppender != null) {
//            reporterAppender.append(reporters);
//        }
//        return new CompositeReporter(reporters.toArray(new Reporter[reporters.size()]));
//    }

}
