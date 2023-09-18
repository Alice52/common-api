package top.hubby.test.custom.redis.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.hubby.custom.test.converter.PhaseConverter;
import top.hubby.custom.test.model.dto.PhaseDTO;
import top.hubby.custom.test.model.entity.Phase;
import top.hubby.custom.test.model.vo.PhaseVO;
import top.hubby.test.custom.redis.mapper.PhaseMapper;
import top.hubby.test.custom.redis.service.PhaseService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * use list to cache phase.
 *
 * @author zack <br>
 * @create 2021-04-09 10:22 <br>
 * @project integration <br>
 */
@Service
@Slf4j
@Deprecated
public class PhaseServiceV2Impl extends ServiceImpl<PhaseMapper, Phase> implements PhaseService {
    private static final String CACHE_KEY = "phase-v2-list";

    @Resource private RedisTemplate redisTemplate;

    @Override
    public Boolean createPhase(PhaseDTO dto) {

        Phase phase = PhaseConverter.CONVERTER.dto2po(dto);
        int insert = baseMapper.insert(phase);

        redisTemplate.opsForList().rightPush(CACHE_KEY, phase);

        return retBool(insert);
    }

    @Override
    public PhaseVO getPhase(Long id, String type) {

        return PhaseConverter.CONVERTER.po2vo(getEntity(id));
    }

    @Override
    public Boolean updatePhase(PhaseDTO dto) {

        Phase phase = PhaseConverter.CONVERTER.dto2po(dto);
        Long mappedIndex = getMappedIndex(dto.getId());
        if (mappedIndex != -1) {
            redisTemplate.opsForList().set(CACHE_KEY, mappedIndex, phase);
        } else {
            redisTemplate.opsForList().rightPush(CACHE_KEY, phase);
        }

        return retBool(baseMapper.updateById(phase));
    }

    @Override
    public Boolean deletePhase(Long id) {

        Phase phase = getEntity(id);
        redisTemplate.opsForList().remove(CACHE_KEY, 1, phase);

        phase.setIsDeleted(true);
        return retBool(baseMapper.updateById(phase));
    }

    private Phase getMapped(Long id) {
        List<Phase> range = redisTemplate.opsForList().range(CACHE_KEY, 0, -1);
        for (int i = 0; i < range.size(); i++) {
            if (range.get(i).getId().equals(id)) {
                return range.get(i);
            }
        }
        return null;
    }

    private Long getMappedIndex(Long id) {

        List<Phase> range = redisTemplate.opsForList().range(CACHE_KEY, 0, -1);
        for (long i = 0; i < range.size(); i++) {
            if (range.get((int) i).getId().equals(id)) {
                return i;
            }
        }

        return -1L;
    }

    @Override
    public List<PhaseVO> listPhases(String type) {

        return getPhases(type);
    }

    private List<PhaseVO> getPhases(String type) {

        Set range = redisTemplate.opsForSet().members(CACHE_KEY);
        if (CollUtil.isNotEmpty(range)) {
            List<Phase> phases = new ArrayList<>(range);
            return PhaseConverter.CONVERTER.pos2vos(phases);
        }

        LambdaQueryWrapper<Phase> queryWrapper = buildQueryWrapper(type);
        List<Phase> phases = this.list(queryWrapper);

        phases.forEach(x -> redisTemplate.opsForSet().add(CACHE_KEY, x));

        return phases.stream().map(PhaseConverter.CONVERTER::po2vo).collect(Collectors.toList());
    }

    private Phase getEntity(Long id) {
        Object object = getMapped(id);
        Phase phase;
        if (object instanceof Phase) {
            phase = (Phase) object;
        } else {
            val entity = Phase.builder().id(id).build();
            phase = getByCondition(entity);
            Optional.ofNullable(phase)
                    .ifPresent(x -> redisTemplate.opsForList().set(CACHE_KEY, x.getId(), x));
        }

        return phase;
    }
}
