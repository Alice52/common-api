package top.hubby.test.custom.controller;

/**
 * @author zack <br>
 * @create 2021-06-22 12:58 <br>
 * @project swagger-3 <br>
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/tool")
public class ToolController {
    /** please remove manually if no use */
    public static final ThreadLocal<String> tl = new ThreadLocal<>();

    @RequestMapping(
            value = "/jie8583",
            method = RequestMethod.GET,
            produces = "application/json;charset=UTF-8")
    public String jie8583(String msg) {
        tl.set("83的线程");
        return "";
    }

    @RequestMapping(
            value = "/jie8581",
            method = RequestMethod.GET,
            produces = "application/json;charset=UTF-8")
    public String jie8581(String msg) {
        log.info(tl.get());
        return "";
    }
}
