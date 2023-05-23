package common.token.manage;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.token.configuration.TokenProviderProperties;
import common.token.exception.ValidTokenException;
import common.token.model.Token;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.Objects;

/**
 * @author T04856 <br>
 * @create 2023-05-22 2:39 PM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
public class TokenHttpRequests {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final TokenProviderProperties tokenProperties;
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    public TokenHttpRequests(TokenProviderProperties tokenProperties, ObjectMapper objectMapper) {
        this.tokenProperties = tokenProperties;
        this.client = TokenOkClient.getInstance();
        this.objectMapper = objectMapper;
    }

    public Token getToken() throws ValidTokenException {
        Request request = createHttpRequest();
        log.debug("[Token Manager] get token with request [{}].", request);
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && Objects.nonNull(response.body())) {
                return objectMapper.readValue(response.body().string(), Token.class);
            }
        } catch (Exception e) {
            throw new ValidTokenException(
                    "The response from the identity provider was not correct.", e);
        }

        throw new ValidTokenException("The response from the identity provider was not correct.");
    }

    private Request createHttpRequest() {

        String format =
                StrUtil.format(
                        "{ \"{}\": \"{}\", \"{}\": \"{}\"}",
                        tokenProperties.getKeyName(),
                        tokenProperties.getKeyValue(),
                        tokenProperties.getSecretName(),
                        tokenProperties.getSecretValue());
        RequestBody requestBody = RequestBody.create(JSON, format);
        return new Request.Builder()
                .url(tokenProperties.getAuthenticationUrl())
                .post(requestBody)
                .build();
    }

    static class TokenOkClient {

        private static final OkHttpClient instance =
                new OkHttpClient.Builder()
                        .connectTimeout(Duration.ofMillis(3000))
                        .readTimeout(Duration.ofMillis(3000 * 3))
                        .sslSocketFactory(sslSocketFactory(), x509TrustManager())
                        .hostnameVerifier(hostnameVerifier())
                        .build();

        private TokenOkClient() {}

        public static OkHttpClient getInstance() {
            return instance;
        }

        private static SSLSocketFactory sslSocketFactory() {
            SSLContext ctx;
            try {
                ctx = SSLContext.getInstance("TLSv1.2");
            } catch (NoSuchAlgorithmException e) {
                throw new ValidTokenException(e.getMessage(), e);
            }

            try {
                ctx.init(null, new TrustManager[] {x509TrustManager()}, new SecureRandom());
            } catch (KeyManagementException e) {
                throw new ValidTokenException(e.getMessage(), e);
            }
            return ctx.getSocketFactory();
        }

        private static X509TrustManager x509TrustManager() {
            return new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s)
                        throws CertificateException {}

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s)
                        throws CertificateException {}

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
        }

        private static HostnameVerifier hostnameVerifier() {
            return (hostname, sslSession) -> true;
        }
    }
}
