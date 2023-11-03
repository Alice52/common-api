package common.logging.trace;

import io.jaegertracing.internal.MDCScopeManager;
import io.opentracing.contrib.java.spring.jaeger.starter.TracerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static common.logging.trace.TraceIdConstants.*;

/**
 * this will configure traceid generator and broadcast.
 *
 * @see io.opentracing.contrib.java.spring.jaeger.starter.JaegerAutoConfiguration
 * @see io.opentracing.contrib.spring.tracer.configuration.TracerRegisterAutoConfiguration
 * @see io.opentracing.contrib.spring.web.starter.ServerTracingAutoConfiguration#TracingHandlerInterceptor
 * @see io.opentracing.contrib.spring.web.starter.SkipPatternAutoConfiguration
 * @see io.opentracing.contrib.spring.web.starter.RestTemplateTracingAutoConfiguration
 * @see io.opentracing.contrib.spring.web.starter.WebClientTracingAutoConfiguration
 */
@Configuration
public class JaegerConfiguration {


    @Bean
    public TracerBuilderCustomizer mdcBuilderCustomizer() {
        return builder ->
                builder.withScopeManager(
                        new MDCScopeManager.Builder()
                                .withMDCSpanIdKey(SPAN_ID_NAME)
                                .withMDCTraceIdKey(TRACE_ID_NAME)
                                .withMDCSampledKey(SAMPLED_NAME)
                                .build());
    }

    //    @Bean
    //    @Order(Ordered.HIGHEST_PRECEDENCE)
    //    @ConditionalOnProperty(prefix = "opentracing.jaeger.neverReport", value = "enabled",
    // matchIfMissing = true)
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
