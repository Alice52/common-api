package common.encrypt.aspect;

import cn.hutool.crypto.symmetric.AES;
import common.encrypt.advice.EncryptResponseAdvice;
import common.encrypt.annotation.Encrypt;
import common.encrypt.constants.enums.EncryptEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * @see EncryptResponseAdvice
 * @author Zack Zhang
 */
@Order(-10)
@Aspect
@Slf4j
@AllArgsConstructor
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

        EncryptEnum encryptEnum = encrypt.encrypt();
        Object proceed = point.proceed();

        // encryptAes do work
        BiFunction<AES, Object, Object> strategy = encryptEnum.getEncrypt();
        if (Objects.isNull(strategy)) {
            return proceed;
        }

        return strategy.apply(encryptAes, proceed);
    }
}
