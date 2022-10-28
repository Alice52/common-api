package top.hubby.openapi.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import top.hubby.openapi.aspect.OpenApiSignatureAspect;
import top.hubby.openapi.filter.OpenApiHttpServletFilter;

/**
 * please notice if use this api, the OpenApiHttpServletFilter should be injected for read request
 * body.
 *
 * @see OpenApiHttpServletFilter
 * @see OpenApiSignatureAspect
 * @author zack <br>
 * @create 2022-04-07 18:34 <br>
 * @project mc-platform <br>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface OpenApiSignature {}
