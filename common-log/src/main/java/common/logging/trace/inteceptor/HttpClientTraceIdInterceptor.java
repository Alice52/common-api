package common.logging.trace.inteceptor;

import common.logging.constants.Constants;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;
import org.slf4j.MDC;

import java.io.IOException;

/**
 * JaegerConfiguration will considering this scenario.
 *
 * <pre>
 *   private static CloseableHttpClient httpClient = HttpClientBuilder.create()
 *           .addInterceptorFirst(new HttpClientTraceIdInterceptor())
 *           .build();
 * </pre>
 *
 * @author T04856 <br>
 * @create 2023-06-07 5:10 PM <br>
 * @project common-api <br>
 */
@Deprecated
public class HttpClientTraceIdInterceptor implements HttpRequestInterceptor {
    @Override
    public void process(HttpRequest httpRequest, HttpContext httpContext)
            throws HttpException, IOException {
        String traceId = MDC.get(Constants.TRACE_ID);
        // 当前线程调用中有traceId，则将该traceId进行透传
        if (traceId != null) {
            // 添加请求体
            httpRequest.addHeader(Constants.TRACE_ID, traceId);
        }
    }
}
