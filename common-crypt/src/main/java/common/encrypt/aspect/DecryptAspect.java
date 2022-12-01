package common.encrypt.aspect;

import cn.hutool.crypto.symmetric.AES;
import common.encrypt.advice.DecryptRequestAdvice;
import common.encrypt.annotation.Decrypt;
import common.encrypt.annotation.Encrypt;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

import javax.annotation.Resource;

/**
 * @see DecryptRequestAdvice
 * @author Zack Zhang
 */
@Deprecated
@Order(-10)
@Aspect
@Slf4j
@AllArgsConstructor
public class DecryptAspect {

    @Resource private AES decryptAes;

    /**
     * 切入去点拦截
     *
     * @see Encrypt
     */
    @Pointcut("@annotation(decrypt)")
    public void pointCut(Decrypt decrypt) {}

    @Around("pointCut(decrypt)")
    public Object doPoint(ProceedingJoinPoint point, Decrypt decrypt) throws Throwable {

        // https://blog.csdn.net/t194978/article/details/122993515

        // 1. get request
        // ServletRequestAttributes attributes = (ServletRequestAttributes)
        // RequestContextHolder.getRequestAttributes();
        // 2. get method args
        // Object[] args = point.getArgs();

        return null;
    }
}
