package common.core.model;

import lombok.*;

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
public class Pagination {

    private Integer total;
    private Integer pageCount;
    private Integer currentPage;
    private Integer pageSize;
}
