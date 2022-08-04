package common.database.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.List;

/**
 * @author zack <br>
 * @create 2021-06-02 15:28 <br>
 * @project database-mybatis-plus <br>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Deprecated
public class DataScope extends HashMap {
    /** 限制范围的字段名称 */
    private String scopeName = "deptId";

    /** 具体的数据范围 */
    private List<Integer> deptIds;

    /** 是否只查询本部门 */
    private Boolean isOnly = false;
}
