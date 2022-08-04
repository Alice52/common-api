package common.database.model.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class PlainEntity implements Serializable {
    protected static final long serialVersionUID = 1L;

    @TableLogic private Boolean isDeleted;

    private LocalDateTime insertedTime;

    private Long insertedBy;
}
