package common.event;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

/**
 * @author zack <br>
 * @create 2021-06-02 11:53 <br>
 * @project custom-test <br>
 */
@Slf4j
public class SysLogListener {

    @Async
    @Order
    @EventListener(SysLogEvent.class)
    public void handleSysLogEvent(SysLogEvent event) {
        log.info("handle common.event: {}", event);
    }
}
