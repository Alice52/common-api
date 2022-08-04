package common.database.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseEntity implements Serializable {
    protected static final long serialVersionUID = 1L;

    @TableLogic private Boolean isDeleted;

    /**
     * FieldFill.INSERT will work in create:
     *
     * <pre>
     *     Preparing:   INSERT INTO boot_cache_all_star_phase ( phase_code, phase_name, start_time, end_time, inserted_time, inserted_by ) VALUES ( ?, ?, ?, ?, ?, ? )
     *     Parameters:  WARM_UP(String), 活动(String), 2021-04-25 14:30:30.0(Timestamp), 2021-04-25 14:35:30.0(Timestamp), 2021-04-25 23:15:07.267(Timestamp), null
     * </pre>
     */
    @TableField(value = "inserted_time", fill = FieldFill.INSERT)
    private LocalDateTime insertedTime;

    @TableField(value = "updated_time", fill = FieldFill.UPDATE)
    private LocalDateTime updatedTime;

    @TableField(value = "inserted_by", fill = FieldFill.INSERT)
    private Long insertedBy;

    @TableField(value = "updated_by", fill = FieldFill.UPDATE)
    private Long updatedBy;
}
