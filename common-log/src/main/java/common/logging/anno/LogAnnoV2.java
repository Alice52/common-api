package common.logging.anno;

import common.logging.anno.aspect.LogAnnoV2Aspect;
import common.logging.anno.filter.LogHttpServletFilter;

import java.lang.annotation.*;

/**
 * please notice if use this api, the OpenApiHttpServletFilter should be injected for read request
 * body.
 *
 * @see LogHttpServletFilter
 * @see LogAnnoV2Aspect
 * @author zack <br>
 * @create 2022-04-07 17:40 <br>
 * @project mc-platform <br>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogAnnoV2 {}
