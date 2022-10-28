package common.interceptor.async;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import org.springframework.core.task.TaskDecorator;

/**
 * @author asd <br>
 * @create 2021-12-07 4:00 PM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
public class MdcTaskDecorator implements TaskDecorator {
    /**
     * 使异步线程池获得主线程的上下文
     *
     * @param runnable
     * @return
     */
    @Override
    public Runnable decorate(Runnable runnable) {
        Map<String, String> map = MDC.getCopyOfContextMap();
        return () -> {
            try {
                MDC.setContextMap(map);
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
