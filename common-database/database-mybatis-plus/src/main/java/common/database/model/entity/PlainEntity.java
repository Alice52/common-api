package common.database.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

@Data
public class PlainEntity implements Serializable {
    protected static final long serialVersionUID = 1L;

    @TableLogic private Boolean isDeleted;

    private LocalDateTime insertedTime;

    private Long insertedBy;
}
