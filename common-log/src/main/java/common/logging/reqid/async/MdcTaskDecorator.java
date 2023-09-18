package common.logging.reqid.async;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

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
