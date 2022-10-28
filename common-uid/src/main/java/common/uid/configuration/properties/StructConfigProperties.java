package common.uid.configuration.properties;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zack <br>
 * @create 2021-06-25<br>
 * @project project-custom <br>
 */
@Data
@ConfigurationProperties(prefix = "common.uid")
public class StructConfigProperties {

    private Integer timeBits = 29;
    private Integer workerBits = 21;
    private Integer seqBits = 13;
    private String epochStr = "2016-09-20";
}
