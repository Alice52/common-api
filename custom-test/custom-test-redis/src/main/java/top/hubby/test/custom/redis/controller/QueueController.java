package top.hubby.test.custom.redis.controller;

import cn.hutool.core.map.MapUtil;
import common.redis.component.RedisListQueueService;
import common.redis.component.RedisStreamQueueService;
import common.redis.queue.RedisProducer;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import static top.hubby.test.custom.redis.constants.RedisStreamConstants.TESTING_STREAM_KEY;

/**
 * @author zack <br>
 * @create 2022-05-22 20:38 <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
@Api(tags = {"Redis Queue Api"})
@RestController
@RequestMapping("/custom/redis/queue")
public class QueueController {

    @Resource private RedisListQueueService listQueueService;
    @Resource private RedisStreamQueueService streamQueueService;
    @Resource private RedisProducer producer;

    @PostConstruct
    public void consume() {
        new Thread(() -> listQueueService.onMessage());
        new Thread(() -> streamQueueService.onMessage());
    }

    @GetMapping("/list")
    public void testListQueue(@RequestParam("msg") String msg) {
        listQueueService.sendMessage(msg);
    }

    @GetMapping("/stream/redission")
    public void testStreamRedissionQueue(@RequestParam("msg") String msg) {
        streamQueueService.sendMessage("msg-key", msg);
        streamQueueService.onMessage();
    }

    @GetMapping("/stream")
    public void testStreamQueue(@RequestParam("msg") String msg) {
        producer.sendMessage(TESTING_STREAM_KEY, MapUtil.of("zack", "18"));
    }
}
