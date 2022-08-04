package common.uid.worker.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import common.uid.worker.WorkerNodeType;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

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
    private long id;

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
