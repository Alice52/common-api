package common.token.manage;

import common.token.model.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author T04856 <br>
 * @create 2023-05-22 2:39 PM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
@RequiredArgsConstructor
public class TokenManager implements InitializingBean {

    // todo: this should be extra interface, impl by memory or redis cache
    private static final AtomicReference<Token> tokenStorage = new AtomicReference<>();
    private static final Semaphore SEMAPHORE = new Semaphore(1);
    private static volatile boolean isStopNow = false;
    private static ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private final TokenHttpRequests tokenHttpRequests;

    private static boolean respondIfTokenValid(Token token) {
        return null != token
                && null != token.getAccessToken()
                && token.getExpireTime() > System.currentTimeMillis();
    }

    /**
     * 1. call identity-server to get token.<br>
     * 2. calculate token expire time for schedule job.<br>
     *
     * @param tokenHttpRequests
     * @return
     */
    private static Token refreshToken(TokenHttpRequests tokenHttpRequests) {
        long delayTime = 1;
        Token token = new Token(null, delayTime, 0);
        try {
            token = tokenHttpRequests.getToken(); // http call
            token.setExpireTime(System.currentTimeMillis() + (token.getTtl() - 1) * 1000);
            TokenManager.tokenStorage.getAndSet(token);

            delayTime = token.getTtl() / 2;

            // less than the connection timeout.
            if (delayTime <= 6) {
                delayTime = 0;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        executor.schedule(
                () -> {
                    if (!isStopNow) {
                        log.debug("[Token Manager] begin to refresh.");
                        refreshToken(tokenHttpRequests);
                    }
                },
                delayTime * 1000L,
                TimeUnit.MILLISECONDS);

        return token;
    }

    @Override
    public void afterPropertiesSet() {
        refreshToken(tokenHttpRequests);
    }

    public String getAccessToken() {

        Token token = TokenManager.tokenStorage.get();
        if (respondIfTokenValid(token)) {
            return token.getAccessToken();
        }

        try {
            SEMAPHORE.acquire();
            if (respondIfTokenValid(token)) {
                return token.getAccessToken();
            }

            return refreshToken(tokenHttpRequests).getAccessToken();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            SEMAPHORE.release();
        }

        return null;
    }

    public void clear() {
        TokenManager.tokenStorage.getAndSet(null);
    }
}
