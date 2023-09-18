package top.hubby.test.custom.redis.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.hubby.custom.test.converter.PhaseConverter;
import top.hubby.custom.test.model.dto.PhaseDTO;
import top.hubby.custom.test.model.entity.Phase;
import top.hubby.custom.test.model.vo.PhaseVO;
import top.hubby.test.custom.redis.mapper.PhaseMapper;
import top.hubby.test.custom.redis.service.PhaseService;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * use list to cache phase.<br>
 * 缓存失效模式: 失效和双写
 *
 * @author zack <br>
 * @create 2021-04-09 10:22 <br>
 * @project integration <br>
 */
@Primary
@Service
@Slf4j
public class PhaseServiceV3Impl extends ServiceImpl<PhaseMapper, Phase> implements PhaseService {
    private static final String CACHE_KEY = "phase-v3-hash";

    @Resource private RedisTemplate redisTemplate;

    @Override
    public Boolean createPhase(PhaseDTO dto) {

        Phase phase = PhaseConverter.CONVERTER.dto2po(dto);
        int insert = baseMapper.insert(phase);

        redisTemplate.opsForHash().put(CACHE_KEY, phase.getId().toString(), phase);

        return retBool(insert);
    }

    @Deprecated
    private void MapKeyIsIntIssue() {
        HashMap<Integer, String> map = new HashMap<>();

        map.put(1, "1-value");
        map.put(2, "2-value");

        redisTemplate.opsForValue().set("map", map);
        // 此时的 map key 会变成 string
        map = (HashMap<Integer, String>) redisTemplate.opsForValue().get("map");
        log.info("map: {}", map);
    }

    @Override
    public PhaseVO getPhase(Long id, String type) {

        Phase entity = Phase.builder().id(id).type(type).build();

        return PhaseConverter.CONVERTER.po2vo(getEntity(entity));
    }

    @Override
    public Boolean updatePhase(PhaseDTO dto) {

        Phase phase = PhaseConverter.CONVERTER.dto2po(dto);

        redisTemplate.opsForHash().put(CACHE_KEY, phase.getId().toString(), phase);

        return retBool(baseMapper.updateById(phase));
    }

    @Override
    public Boolean deletePhase(Long id) {
        Phase entity = Phase.builder().id(id).build();
        Phase phase = getEntity(entity);
        redisTemplate.opsForHash().delete(CACHE_KEY, id.toString());

        phase.setIsDeleted(true);
        return retBool(baseMapper.updateById(phase));
    }

    @Override
    public List<PhaseVO> listPhases(String type) {

        return getPhases(type);
    }

    private List<PhaseVO> getPhases(String type) {

        Map<Long, Phase> entries = redisTemplate.opsForHash().entries(CACHE_KEY);
        if (CollUtil.isNotEmpty(entries)) {
            return PhaseConverter.CONVERTER.pos2vos(new ArrayList(entries.values()));
        }

        LambdaQueryWrapper<Phase> queryWrapper = buildQueryWrapper(type);
        List<Phase> phases = this.list(queryWrapper);

        Map<String, Phase> collect =
                phases.parallelStream()
                        .collect(
                                Collectors.toMap(
                                        x -> x.getId().toString(),
                                        Function.identity(),
                                        (v1, v2) -> v2));
        redisTemplate.opsForHash().putAll(CACHE_KEY, collect);

        return phases.stream().map(PhaseConverter.CONVERTER::po2vo).collect(Collectors.toList());
    }

    private Phase getEntity(Phase dto) {

        Object o = redisTemplate.opsForHash().get(CACHE_KEY, dto.getId().toString());
        if (ObjectUtil.isNotNull(o) && o instanceof Phase) {
            return (Phase) o;
        }

        Phase phase = getByCondition(dto);
        Optional.ofNullable(phase)
                .ifPresent(x -> redisTemplate.opsForHash().put(CACHE_KEY, x.getId().toString(), x));
        return phase;
    }
}
