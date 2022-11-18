package top.hubby.custom.test.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import common.database.model.entity.BaseEntity;
import common.database.sensitive.annotation.SensitiveField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author zack <br>
 * @create 2021-04-09 10:12 <br>
 * @project integration <br>
 */
@Data
@Builder
@TableName("boot_cache_all_star_phase")
@AllArgsConstructor
@NoArgsConstructor
public class Phase extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @SensitiveField private String phaseCode;

    private String phaseName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @SensitiveField @TableField private String type;

    private String status;
}
