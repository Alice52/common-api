package common.logging.reqid.async;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author asd <br>
 * @create 2021-12-07 4:01 PM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
@EnableAsync
public class SampleThreadPoolConfig {
    private int corePoolSize = 50;
    private int maxPoolSize = 200;
    private int queueCapacity = 1000;
    private int keepAliveSeconds = 300;

    @Bean(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(maxPoolSize);
        executor.setCorePoolSize(corePoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setTaskDecorator(new MdcTaskDecorator());
        // 线程池对拒绝任务(无线程可用)的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    @Async("threadPoolTaskExecutor")
    public void testSyncMethod() {
        // usage
    }
}

@Slf4j
class MdcTaskDecorator implements TaskDecorator {
    /**
     * 使异步线程池获得主线程的上下文
     *
     * @param runnable
     * @return
     */
    @Override
    public Runnable decorate(Runnable runnable) {

        Map<String, String> context = MDC.getCopyOfContextMap();
        return () -> {
            Map<String, String> previous = MDC.getCopyOfContextMap();
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            try {
                runnable.run();
            } finally {
                if (previous == null) {
                    MDC.clear();
                } else {
                    MDC.setContextMap(previous);
                }
            }
        };
    }
}
