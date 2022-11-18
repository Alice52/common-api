package top.hubby.test.custom.uid.controller;

import common.core.util.R;
import common.uid.generator.CachedUidGenerator;
import common.uid.generator.DefaultUidGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.HashMap;

/**
 * @author zack <br>
 * @create 2021-06-25<br>
 * @project project-custom <br>
 */
@Api(tags = {"Sample Api"})
@RestController
@RequestMapping("/uid")
@Slf4j
public class SampleUidController {

    @Resource private CachedUidGenerator cachedUidGenerator;

    @Resource private DefaultUidGenerator defaultUidGenerator;

    @GetMapping("/cached-generator")
    @ApiOperation("Generate UID By Cached Generator Sample")
    public @NotNull R<HashMap<String, Object>> cachedGenerate() {

        return getHashMapR(cachedUidGenerator);
    }

    @GetMapping("/default-generator")
    @ApiOperation("Generate UID By Default Generator Sample")
    public @NotNull R<HashMap<String, Object>> defaultGenerate() {

        return getHashMapR(defaultUidGenerator);
    }

    @NotNull
    private R<HashMap<String, Object>> getHashMapR(DefaultUidGenerator defaultUidGenerator) {
        long uid = defaultUidGenerator.getUID();
        String detail = defaultUidGenerator.parseUID(uid);

        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        map.put("detail", detail);

        return R.<HashMap<String, Object>>builder().data(map).build();
    }
}
