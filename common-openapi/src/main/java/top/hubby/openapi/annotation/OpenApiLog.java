package top.hubby.openapi.annotation;

import top.hubby.openapi.aspect.OpenApiSignatureAspect;
import top.hubby.openapi.filter.OpenApiHttpServletFilter;

import java.lang.annotation.*;

/**
 * please notice if use this api, the OpenApiHttpServletFilter should be injected for read request
 * body.
 *
 * @see OpenApiHttpServletFilter
 * @see OpenApiSignatureAspect
 * @author zack <br>
 * @create 2022-04-07 17:40 <br>
 * @project mc-platform <br>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OpenApiLog {}
