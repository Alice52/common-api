package top.hubby.test.custom.db.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.val;
import org.springframework.stereotype.Service;
import top.hubby.custom.test.constants.enums.ActivityPhaseEnum;
import top.hubby.custom.test.converter.PhaseConverter;
import top.hubby.custom.test.model.dto.PhaseDTO;
import top.hubby.custom.test.model.entity.Phase;
import top.hubby.custom.test.model.vo.PhaseVO;
import top.hubby.test.custom.db.mapper.PhaseMapper;
import top.hubby.test.custom.db.service.PhaseService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author zack <br>
 * @create 2022-04-08<br>
 * @project project-cloud-custom <br>
 */
@Service
public class PhaseServiceImpl extends ServiceImpl<PhaseMapper, Phase> implements PhaseService {

    @Override
    public PhaseVO getPhase(Long id, String type) {

        val entity = Phase.builder().id(id).type(type).build();
        Phase phase = getByCondition(entity);

        return PhaseConverter.CONVERTER.po2vo(phase);
    }

    @Override
    public Boolean updatePhase(PhaseDTO dto) {
        validateDuplicateByCodeOrName(dto);

        return retBool(baseMapper.updateById(PhaseConverter.CONVERTER.dto2po(dto)));
    }

    @Override
    public Boolean deletePhase(Long id) {

        Phase phase = validateThenGet(id);
        phase.setIsDeleted(true);

        return retBool(baseMapper.updateById(phase));
    }

    @Override
    public Boolean createPhase(PhaseDTO dto) {

        validateDuplicateByCodeOrName(dto);

        Phase phase = new Phase();

        return retBool(baseMapper.insert(PhaseConverter.CONVERTER.dto2po(dto)));
    }

    @Override
    public List<PhaseVO> listPhases(String type) {

        return getPhases(type);
    }

    private List<PhaseVO> getPhases(String type) {
        LambdaQueryWrapper<Phase> queryWrapper = buildQueryWrapper(type);
        List<Phase> phases = this.list(queryWrapper);

        return phases.stream().map(PhaseConverter.CONVERTER::po2vo).collect(Collectors.toList());
    }

    private void validateDuplicateByCodeOrName(PhaseDTO dto) {

        ensureValidPhaseCode(dto.getPhaseCode());

        LambdaQueryWrapper<Phase> queryWrapper = buildQueryWrapper();
        queryWrapper.and(
                obj ->
                        obj.eq(Phase::getPhaseCode, dto.getPhaseCode())
                                .or()
                                .eq(Phase::getPhaseName, dto.getPhaseName()));

        if (ObjectUtil.isNotNull(dto.getId())) {
            queryWrapper.ne(Phase::getId, dto.getId());
        }

        List<Phase> phases = this.list(queryWrapper);

        if (!CollUtil.isEmpty(phases)) {
            throw new RuntimeException("阶段Code或者阶段名称重复");
        }
    }

    private void ensureValidPhaseCode(String phaseCode) {
        if (StrUtil.isNotEmpty(phaseCode)
                && Boolean.FALSE.equals(ActivityPhaseEnum.contains(phaseCode))) {
            throw new RuntimeException("不是有效的阶段 Code");
        }
    }

    private Phase validateThenGet(Long id) {
        return validateThenGet(id, null);
    }

    /**
     * 根据条件判断记录是否存在, 不存在则抛出 {@link RuntimeException}, 存在则获取该对象
     *
     * @param id
     * @param type
     * @return
     */
    private Phase validateThenGet(Long id, String type) {
        val entity = Phase.builder().id(id).type(type).build();
        Phase phase = getByCondition(entity);
        Optional.ofNullable(phase)
                .orElseThrow(() -> new RuntimeException(StrUtil.format("Id 为 {} 的记录不存在", id)));

        return phase;
    }
}
