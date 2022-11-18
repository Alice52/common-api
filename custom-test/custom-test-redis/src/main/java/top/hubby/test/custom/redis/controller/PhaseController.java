package top.hubby.test.custom.redis.controller;

import common.core.util.R;
import common.core.util.valid.Add;
import common.redis.constants.CommonCacheConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.hubby.custom.test.model.dto.PhaseDTO;
import top.hubby.custom.test.model.vo.PhaseVO;
import top.hubby.test.custom.redis.service.PhaseService;

import javax.annotation.Resource;
import javax.validation.groups.Default;
import java.util.List;

/**
 * @author zack <br>
 * @create 2021-04-09 10:10 <br>
 * @project integration <br>
 */
@Slf4j
@Api(tags = {"Phase Manage Api"})
@RestController
@RequestMapping("/custom")
public class PhaseController {
    @Resource private PhaseService phaseService;
    // @Resource private PhaseServiceV2Impl phaseService;
    // @Resource private PhaseServiceV3Impl phaseService;

    @GetMapping("/phases")
    @Cacheable(
            value = CommonCacheConstants.MODULE_PHASE_KEY,
            key = "'list'",
            unless = "#result.data.size() == 0")
    public R<List<PhaseVO>> list(
            @RequestParam(value = "type", required = false) @ApiParam(value = "活动标识") String type) {

        return R.success(phaseService.listPhases(type));
    }

    @ApiOperation("data de-sensitive")
    @GetMapping("/phase/{id}")
    @Cacheable(
            value = CommonCacheConstants.MODULE_PHASE_KEY,
            key = "#id",
            unless = "#result.data.id == null")
    public R<PhaseVO> get(
            @PathVariable("id") Long id,
            @RequestParam(value = "type", required = false) @ApiParam(value = "活动标识") String type) {

        return R.success(phaseService.getPhase(id, type));
    }

    @PutMapping("/phase/{id}")
    @Caching(
            evict = {
                @CacheEvict(value = CommonCacheConstants.MODULE_PHASE_KEY, key = "#id"),
                @CacheEvict(value = CommonCacheConstants.MODULE_PHASE_KEY, key = "'list'")
            })
    public R<Boolean> update(@PathVariable("id") Long id, @RequestBody PhaseDTO phase) {
        phase.setId(id);
        return R.success(phaseService.updatePhase(phase));
    }

    @DeleteMapping("/phase/{id}")
    @Caching(
            evict = {
                @CacheEvict(value = CommonCacheConstants.MODULE_PHASE_KEY, key = "#id"),
                @CacheEvict(value = CommonCacheConstants.MODULE_PHASE_KEY, key = "'list'")
            })
    public R<Boolean> delete(@PathVariable("id") Long id) {
        return R.success(phaseService.deletePhase(id));
    }

    @PostMapping("/phase")
    @CacheEvict(value = CommonCacheConstants.MODULE_PHASE_KEY, key = "'list'")
    public R<Boolean> create(
            @RequestBody @Validated({Add.class, Default.class}) PhaseDTO phase) {
        return R.success(phaseService.createPhase(phase));
    }
}
