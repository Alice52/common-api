package top.hubby.test.custom.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author asd <br>
 * @create 2022-01-11 10:01 AM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
@RestController
@RequestMapping("/array")
public class ArrayArgController {

    /**
     * curl: curl -X POST "http://localhost:8080/array/body" -H "Content-Type: application/json" -d
     * "[ \"1\",\"2\",3]" <br>
     *
     * @param array
     * @return
     */
    @PostMapping("/body")
    public String[] arrayArgAsBody(@RequestBody String[] array) {

        return array;
    }

    @PostMapping("/body/list")
    public List<String> listArgAsBody(@RequestBody List<String> list) {

        return list;
    }

    /**
     * uri: http://localhost:8080/array/body?array=1&array=6
     *
     * @param array
     * @return
     */
    @GetMapping("/body")
    public String[] arrayArgAsBody4Get(@RequestParam String[] array) {

        return array;
    }

    @GetMapping("/body/list")
    public List<String> listArgAsBody4Get(@RequestParam List<String> list) {

        return list;
    }
}
