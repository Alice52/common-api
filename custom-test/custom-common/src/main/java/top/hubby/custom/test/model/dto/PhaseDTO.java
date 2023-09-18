package top.hubby.custom.test.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import common.core.util.valid.Add;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.hubby.custom.test.constants.enums.PhaseStatusEnum;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zack <br>
 * @create 2021-04-09 10:21 <br>
 * @project integration <br>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhaseDTO implements Serializable {

    @ApiModelProperty(hidden = true)
    private Long id;

    @ApiModelProperty("阶段Code")
    @NotNull(
            groups = {Add.class},
            message = "phaseCode 不能为空")
    private String phaseCode;

    @ApiModelProperty("阶段名称")
    @NotNull(
            groups = {Add.class},
            message = "phaseName 不能为空")
    private String phaseName;

    @ApiModelProperty("阶段开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(
            groups = {Add.class},
            message = "startTime 不能为空")
    private LocalDateTime startTime;

    @ApiModelProperty("阶段结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(
            groups = {Add.class},
            message = "endTime 不能为空")
    private LocalDateTime endTime;

    @ApiModelProperty(hidden = true)
    private String type;

    @ApiModelProperty(hidden = true)
    private PhaseStatusEnum status;

    public PhaseDTO(Long id, String type) {
        this.id = id;
        this.type = type;
    }

    public PhaseDTO(Long id) {
        this.id = id;
    }
}
