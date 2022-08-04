package top.hubby.openapi.filter;

import common.core.filter.RepeatReadHttpServletFilter;
import org.springframework.boot.web.servlet.ServletComponentScan;

import javax.servlet.annotation.WebFilter;

/**
 * @author zack <br>
 * @create 2022-04-08 16:07 <br>
 * @project mc-platform <br>
 */
@ServletComponentScan
@WebFilter(urlPatterns = {"/openapi/*"})
public class OpenApiHttpServletFilter extends RepeatReadHttpServletFilter {}
