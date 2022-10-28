package common.http.component;

import java.time.Duration;

import javax.annotation.Resource;

import common.http.interceptor.BasicAuthInterceptor;
import common.http.interceptor.DecryptInterceptor;
import common.http.interceptor.LoggingRequestInterceptor;
import common.http.interceptor.RetryInterceptor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * @see okhttp3.logging.HttpLoggingInterceptor
 * @author asd <br>
 * @create 2021-12-07 4:18 PM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
@Component
public class OkClient {

    @Resource private BasicAuthInterceptor authInterceptor;
    @Resource private LoggingRequestInterceptor loggingInterceptor;
    @Resource private RetryInterceptor retryInterceptor;
    @Resource private DecryptInterceptor decryptInterceptor;

    private OkHttpClient.Builder builder =
            new OkHttpClient.Builder()
                    .connectTimeout(Duration.ofSeconds(3))
                    .readTimeout(Duration.ofSeconds(15));

    @Primary
    @Bean
    public OkHttpClient okHttpClient() {

        return builder.addInterceptor(retryInterceptor)
                .addInterceptor(authInterceptor)
                .addInterceptor(loggingInterceptor)
                // .addInterceptor(decryptInterceptor)
                .build();
    }
}
