package common.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author zack <br>
 * @create 2021-06-02 11:52 <br>
 * @project custom-test <br>
 */
public class SysLogEvent extends ApplicationEvent {

    public SysLogEvent(Object source) {
        super(source);
    }
}
