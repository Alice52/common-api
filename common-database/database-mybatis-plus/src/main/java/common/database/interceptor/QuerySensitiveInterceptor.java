package common.database.interceptor;

import cn.hutool.json.JSONUtil;
import common.core.util.security.AesUtil;
import common.database.sensitive.annotation.SensitiveField;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;

/**
 *
 *
 * <pre>
 * 想办法看看能不能再最贴近表的地方拼装SQL时做:
 *   1.
 * </pre>
 *
 * @author zack <br>
 * @create 2022-03-31 10:06 <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
@Intercepts({
    @Signature(
            type = Executor.class,
            method = "query",
            args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
@Deprecated
public class QuerySensitiveInterceptor implements Interceptor {
    private Method getDaoTargetMethod(MappedStatement mappedStatement) {
        Method method = null;
        try {
            String namespace = mappedStatement.getId();
            String className = namespace.substring(0, namespace.lastIndexOf("."));
            String methedName =
                    namespace.substring(namespace.lastIndexOf(".") + 1, namespace.length());
            Method[] ms = Class.forName(className).getMethods();
            for (Method m : ms) {
                if (m.getName().equals(methedName)) {
                    method = m;
                    break;
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            log.info("EncryptDaoInterceptor.getDaoTargetMethod方法异常==> " + e.getMessage());
            return method;

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            log.info("EncryptDaoInterceptor.getDaoTargetMethod方法异常==> " + e.getMessage());
            return method;
        }
        return method;
    }

    private boolean isEncryptStr(MappedStatement mappedStatement) throws ClassNotFoundException {
        boolean reslut = false;
        try {
            Method m = getDaoTargetMethod(mappedStatement);
            m.setAccessible(true);
            Annotation[][] parameterAnnotations = m.getParameterAnnotations();
            if (parameterAnnotations != null && parameterAnnotations.length > 0) {
                for (Annotation[] parameterAnnotation : parameterAnnotations) {
                    for (Annotation annotation : parameterAnnotation) {
                        if (annotation instanceof SensitiveField) {
                            reslut = true;
                        }
                    }
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            log.info("EncryptDaoInterceptor.isEncryptStr异常：==> " + e.getMessage());
            reslut = false;
        }
        return reslut;
    }

    public Object encryptParam(Object parameter, Invocation invocation) {
        MappedStatement statement = (MappedStatement) invocation.getArgs()[0];
        try {
            if (parameter instanceof String) {
                if (isEncryptStr(statement)) {
                    parameter = AesUtil.encrypt(parameter);
                }
            } else if (parameter instanceof Map) {
                log.info("{}", parameter);
            } else {
                parameter = CryptPojoUtils.encrypt(parameter);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            log.info("EncryptDaoInterceptor.encryptParam方法异常==> " + e.getMessage());
        }
        return parameter;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement statement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = invocation.getArgs()[1];

        log.info(statement.getId() + "未加密参数串:" + JSONUtil.toJsonStr(parameter));

        parameter = encryptParam(parameter, invocation);
        log.info(statement.getId() + "加密后参数:" + JSONUtil.toJsonStr(parameter));
        invocation.getArgs()[1] = parameter;

        return invocation.proceed();
    }

    /**
     * 加入拦截器链
     *
     * @param target
     * @return
     */
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /**
     * 自定义配置写入, 没有自定义配置的可以直接置空此方法
     *
     * @param properties
     */
    @Override
    public void setProperties(Properties properties) {}
}
