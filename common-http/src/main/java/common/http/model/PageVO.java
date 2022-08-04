package common.http.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author asd <br>
 * @create 2021-12-07 4:47 PM <br>
 * @project project-cloud-custom <br>
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageVO<T> {
    private String status;
    private String message;

    private long page;
    private long size;
    private long totalPages;
    private long totalElements;

    private List<T> responseData;
}
