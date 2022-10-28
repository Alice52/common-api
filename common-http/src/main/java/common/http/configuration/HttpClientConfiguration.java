package common.http.configuration;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.ComponentScan;

/**
 * @author asd <br>
 * @create 2021-12-07 4:53 PM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
@ComponentScan("common.http")
// @EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class HttpClientConfiguration {}
