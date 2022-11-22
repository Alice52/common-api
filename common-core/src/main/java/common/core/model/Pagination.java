package common.core.model;

import lombok.*;

import java.util.List;

/**
 * @author zack <br>
 * @create 2021-06-04 16:49 <br>
 * @project custom-test <br>
 */
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Deprecated
public class Pagination<T> {

    private Integer total;
    private Integer pageCount;
    private Integer currentPage;
    private Integer pageSize;
    protected List<T> records;
}
