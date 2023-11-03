package common.logging.trace;

/**
 * @author alice52
 * @date 2023/11/3
 * @project common-api
 */
public interface TraceIdConstants {

    String TRACE_ID_NAME = "X-B3-TraceId";
    String SPAN_ID_NAME = "X-B3-SpanId";
    String SAMPLED_NAME = "X-B3-Sampled";
}
