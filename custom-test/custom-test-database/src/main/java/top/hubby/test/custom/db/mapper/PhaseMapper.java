package top.hubby.test.custom.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.hubby.custom.test.model.entity.Phase;

/**
 * @author zack <br>
 * @create 2021-04-09 10:23 <br>
 * @project integration <br>
 */
@Mapper
public interface PhaseMapper extends BaseMapper<Phase> {}
