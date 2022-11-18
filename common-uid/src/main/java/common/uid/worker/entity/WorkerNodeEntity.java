package common.uid.worker.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import common.uid.worker.WorkerNodeType;
import lombok.Data;
import lombok.ToString;

/**
 * @author zack <br>
 * @create 2021-06-23<br>
 * @project project-custom <br>
 */
@Data
@ToString
@TableName("uid_worker_node")
public class WorkerNodeEntity {

    /** Entity unique id (table unique) */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** Type of CONTAINER: HostName, ACTUAL : IP. */
    private String hostName;

    /** Type of CONTAINER: Port, ACTUAL : Timestamp + Random(0-10000) */
    private String port;

    /** type of {@link WorkerNodeType} */
    private int type;

    /** Worker launch date, default now */
    private Date launchDate = new Date();

    /** Created time */
    private Date created;

    /** Last modified */
    private Date modified;
}
