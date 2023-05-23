package common.token.model;

/**
 * @author T04856 <br>
 * @create 2023-05-22 2:55 PM <br>
 * @project project-cloud-custom <br>
 */
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    private String accessToken;
    private long ttl;
    private long expireTime;
}
