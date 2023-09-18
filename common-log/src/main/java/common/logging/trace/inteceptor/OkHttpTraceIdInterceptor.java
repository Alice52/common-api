package common.logging.trace.inteceptor;

import common.logging.constants.Constants;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.MDC;

import java.io.IOException;

/**
 * JaegerConfiguration will considering this scenario.
 *
 * <pre>
 *     OkHttpClient client = new OkHttpClient.Builder()
 *                  .addNetworkInterceptor(new OkHttpTraceIdInterceptor())
 *                  .build();
 * </pre>
 *
 * @author T04856 <br>
 * @create 2023-06-07 5:11 PM <br>
 * @project common-api <br>
 */
@Deprecated
public class OkHttpTraceIdInterceptor implements Interceptor {

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        String traceId = MDC.get(Constants.TRACE_ID);
        Request request = null;
        if (traceId != null) {
            // 添加请求体
            request = chain.request().newBuilder().addHeader(Constants.TRACE_ID, traceId).build();
        }

        return chain.proceed(request);
    }
}
