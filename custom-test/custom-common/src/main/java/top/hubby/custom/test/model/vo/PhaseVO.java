package top.hubby.custom.test.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import common.core.annotation.DeSensitive;
import common.core.annotation.discriptor.SensitiveStrategy;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.hubby.custom.test.constants.enums.PhaseStatusEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zack <br>
 * @create 2021-04-09 10:15 <br>
 * @project integration <br>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhaseVO implements Serializable {
    private Long id;

    @ApiModelProperty("阶段 Code")
    private String phaseCode;

    @DeSensitive(strategy = SensitiveStrategy.USERNAME)
    @ApiModelProperty("阶段名称")
    private String phaseName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    private String type;

    private PhaseStatusEnum status;

    private Boolean isDeleted;

    private LocalDateTime insertedTime;

    private LocalDateTime updatedTime;

    private Long insertedBy;

    private Long updatedBy;

    public PhaseVO(String phaseCode, String phaseName) {
        this.phaseCode = phaseCode;
        this.phaseName = phaseName;
    }
}
