package top.hubby.test.custom.db.controller;

import common.core.util.R;
import common.core.util.valid.Add;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.hubby.custom.test.model.dto.PhaseDTO;
import top.hubby.custom.test.model.vo.PhaseVO;
import top.hubby.test.custom.db.service.PhaseService;

import javax.annotation.Resource;
import javax.validation.groups.Default;
import java.util.List;

/**
 * @author zack <br>
 * @create 2022-04-08<br>
 * @project project-cloud-custom <br>
 */
@Slf4j
@Api(tags = {"Phase Manage Api"})
@RestController
@RequestMapping("/custom")
public class PhaseController {
    @Resource private PhaseService phaseService;

    @GetMapping("/phases")
    public R<List<PhaseVO>> list(
            @RequestParam(value = "type", required = false) @ApiParam(value = "活动标识") String type) {

        return R.success(phaseService.listPhases(type));
    }

    @ApiOperation("data de-sensitive")
    @GetMapping("/phase/{id}")
    public R<PhaseVO> get(
            @PathVariable("id") Long id,
            @RequestParam(value = "type", required = false) @ApiParam(value = "活动标识") String type) {

        return R.success(phaseService.getPhase(id, type));
    }

    @PutMapping("/phase/{id}")
    public R<Boolean> update(@PathVariable("id") Long id, @RequestBody PhaseDTO phase) {
        phase.setId(id);
        return R.success(phaseService.updatePhase(phase));
    }

    @DeleteMapping("/phase/{id}")
    public R<Boolean> delete(@PathVariable("id") Long id) {
        return R.success(phaseService.deletePhase(id));
    }

    @PostMapping("/phase")
    public R<Boolean> create(
            @RequestBody @Validated({Add.class, Default.class}) PhaseDTO phase) {
        return R.success(phaseService.createPhase(phase));
    }
}
