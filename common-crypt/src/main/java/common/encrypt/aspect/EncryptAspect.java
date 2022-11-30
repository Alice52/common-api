package common.encrypt.aspect;

import cn.hutool.crypto.symmetric.AES;
import common.encrypt.annotation.Encrypt;
import common.encrypt.util.CryptUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

import javax.annotation.Resource;

/**
 * @author Zack Zhang
 */
@Order(-10)
@Aspect
@Slf4j
@AllArgsConstructor
@Import(CryptUtil.class)
public class EncryptAspect {

    @Resource private AES encryptAes;

    /**
     * 切入去点拦截
     *
     * @see Encrypt
     */
    @Pointcut("@annotation(encrypt)")
    public void pointCut(Encrypt encrypt) {}

    @Around("pointCut(encrypt)")
    public Object doPoint(ProceedingJoinPoint point, Encrypt encrypt) throws Throwable {

        Object proceed = point.proceed();

        // encryptAes do work

        return proceed;
    }
}
