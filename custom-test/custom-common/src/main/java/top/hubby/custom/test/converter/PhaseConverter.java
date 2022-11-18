package top.hubby.custom.test.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import top.hubby.custom.test.model.dto.PhaseDTO;
import top.hubby.custom.test.model.entity.Phase;
import top.hubby.custom.test.model.vo.PhaseVO;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author zack <br>
 * @create 2021-06-03 10:39 <br>
 * @project custom-test <br>
 */
@Mapper
public interface PhaseConverter {
    PhaseConverter CONVERTER = Mappers.getMapper(PhaseConverter.class);

    /**
     * Convert dto to po.
     *
     * @param dto
     * @return
     */
    @Mappings({
        @Mapping(target = "status", ignore = true),
    })
    @Nullable
    Phase dto2po(@Nullable PhaseDTO dto);

    /**
     * Convert dto to vo.
     *
     * @param dto
     * @return
     */
    @Nullable
    PhaseVO dto2vo(@Nullable PhaseDTO dto);

    /**
     * Convert dto to vo.
     *
     * @param po
     * @return
     */
    @Mappings({
        @Mapping(target = "status", ignore = true),
        @Mapping(target = "isDeleted", ignore = true)
    })
    @Nullable
    PhaseVO po2vo(@Nullable Phase po);

    /**
     * Convert pos to vos.
     *
     * @param pos
     * @return
     */
    List<PhaseVO> pos2vos(List<Phase> pos);
}
