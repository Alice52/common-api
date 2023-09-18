package common.logging.anno.filter;

import common.core.filter.RepeatReadHttpServletFilter;
import org.springframework.boot.web.servlet.ServletComponentScan;

import javax.servlet.annotation.WebFilter;

@ServletComponentScan
@WebFilter(urlPatterns = {"/*"})
public class LogHttpServletFilter extends RepeatReadHttpServletFilter {}
