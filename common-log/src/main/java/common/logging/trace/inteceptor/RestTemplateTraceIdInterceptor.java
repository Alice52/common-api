package common.logging.trace.inteceptor;

import common.logging.constants.Constants;
import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * JaegerConfiguration will considering this scenario.
 *
 * <pre>
 *     restTemplate.setInterceptors(Arrays.asList(new RestTemplateTraceIdInterceptor()));
 * </pre>
 *
 * @author T04856 <br>
 * @create 2023-06-07 5:13 PM <br>
 * @project common-api <br>
 */
@Deprecated
public class RestTemplateTraceIdInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(
            HttpRequest httpRequest,
            byte[] bytes,
            ClientHttpRequestExecution clientHttpRequestExecution)
            throws IOException {
        String traceId = MDC.get(Constants.TRACE_ID);
        if (traceId != null) {
            httpRequest.getHeaders().add(Constants.TRACE_ID, traceId);
        }

        return clientHttpRequestExecution.execute(httpRequest, bytes);
    }
}
