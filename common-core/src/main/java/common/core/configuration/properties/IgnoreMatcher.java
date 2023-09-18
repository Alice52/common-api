package common.core.configuration.properties;

import lombok.Data;
import org.springframework.http.HttpMethod;

/**
 * @author zack <br>
 * @create 2021-06-27<br>
 * @project project-cloud-custom <br>
 */
@Data
public class IgnoreMatcher {
    private HttpMethod method;
    private String antPattern;
}
