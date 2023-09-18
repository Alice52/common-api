package top.hubby.test.custom.schedule;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author zack <br>
 * @create 2021-06-21 10:39 <br>
 * @project custom-test <br>
 */
@Component
@EnableScheduling
@Slf4j
public class HelloJob {

    /** 按照标准时间来算，每隔 10s 执行一次 */
    @Scheduled(cron = "0/10 * 2 * * ?")
    public void job1() {
        log.info("【job1】开始执行：{}", DateUtil.formatDateTime(new Date()));
    }
}
