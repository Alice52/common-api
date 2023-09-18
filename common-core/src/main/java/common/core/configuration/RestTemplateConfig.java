package common.core.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * @author zack <br>
 * @create 2021-06-01<br>
 * @project project-custom <br>
 */
@Configuration
public class RestTemplateConfig {

    /**
     * This is for redis deserialization enum.
     *
     * @param objectMapper
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(
            ObjectMapper objectMapper) {
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate(MappingJackson2HttpMessageConverter converter) {
        return new RestTemplateBuilder().additionalMessageConverters(converter).build();
    }
}
