package common.http.interceptor;

import cn.hutool.core.util.NumberUtil;
import common.http.exception.DecryptException;
import common.http.exception.RetryException;
import common.http.model.HttpProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author asd <br>
 * @create 2021-11-24 9:47 AM <br>
 * @project mc-platform <br>
 */
@Slf4j
@Component
public class RetryInterceptor implements Interceptor {

    private static final String EXECUTE_TIMES = "retryTimes";
    private static String errMsg;
    @Resource private HttpProperties hmpProperties;

    @Override
    public Response intercept(Chain chain) {
        return process(chain, chain.request());
    }

    @SneakyThrows
    private Response process(Chain chain, Request request) {

        int times = NumberUtil.parseInt(request.headers().get(EXECUTE_TIMES));
        if (times++ > hmpProperties.getMaxRetryTimes()) {
            log.warn(
                    "Retry Limit[{}] reached for hmp api[{}]",
                    hmpProperties.getMaxRetryTimes(),
                    request.url());
            throw new RetryException(hmpProperties.getMaxRetryTimes(), errMsg);
        }

        Response response = null;
        try {
            response = chain.proceed(request);
        } catch (DecryptException ex) {
            // there are no benefit to retry this error, so throw it directly.
            errMsg = ex.getMessage();
            throw ex;
        } catch (Exception ex) {
            errMsg = ex.getMessage();
            request =
                    request.newBuilder()
                            .removeHeader(EXECUTE_TIMES)
                            .addHeader(EXECUTE_TIMES, String.valueOf(times))
                            .build();
            log.warn("call mchmp failed for times[{}], detail: {}", times, ex);
            int sleepMillis = hmpProperties.getRetrySleep() * times;
            try {
                TimeUnit.MILLISECONDS.sleep(sleepMillis);
            } finally {
                process(chain, request);
            }
        }

        return response;
    }
}
