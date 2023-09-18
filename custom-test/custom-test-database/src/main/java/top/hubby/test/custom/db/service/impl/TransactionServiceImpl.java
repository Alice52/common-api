package top.hubby.test.custom.db.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.hubby.custom.test.converter.PhaseConverter;
import top.hubby.custom.test.model.dto.PhaseDTO;
import top.hubby.custom.test.model.entity.Phase;
import top.hubby.test.custom.db.mapper.PhaseMapper;
import top.hubby.test.custom.db.service.TransactionService;

/**
 * @author zack <br>
 * @create 2022-04-03 19:07 <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
@Service
public class TransactionServiceImpl extends ServiceImpl<PhaseMapper, Phase>
        implements TransactionService {

    @Override
    public boolean savePhase(PhaseDTO dto) {
        try {
            baseMapper.insert(PhaseConverter.CONVERTER.dto2po(dto));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            throw new RuntimeException("finally exception...");
        }
    }
}
