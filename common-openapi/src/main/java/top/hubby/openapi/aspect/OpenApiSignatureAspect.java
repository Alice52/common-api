package top.hubby.openapi.aspect;

import javax.servlet.http.HttpServletRequest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import top.hubby.openapi.annotation.OpenApiSignature;
import top.hubby.openapi.util.OpenApiSecureUtil;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author zack <br>
 * @create 2022-04-07 18:34 <br>
 * @project mc-platform <br>
 */
@Order(10)
@Aspect
@Slf4j
@Component
@AllArgsConstructor
public class OpenApiSignatureAspect {

    /**
     * 切入去点拦截
     *
     * @see OpenApiSignature
     */
    @Pointcut("@annotation(openApiSignature)")
    public void pointCut(OpenApiSignature openApiSignature) {}

    @Around("pointCut(openApiSignature)")
    public Object doPoint(ProceedingJoinPoint point, OpenApiSignature openApiSignature)
            throws Throwable {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        OpenApiSecureUtil.validateSignature(request);

        return point.proceed();
    }
}
