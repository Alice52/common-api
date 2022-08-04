package common.database.interceptor;

import common.core.util.AESUtil;
import common.database.sensitive.annotation.SensitiveField;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.plugin.*;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.Properties;

/**
 * @author zack <br>
 * @create 2022-03-31 10:06 <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
@Intercepts({
    @Signature(
            type = ParameterHandler.class,
            method = "setParameters",
            args = PreparedStatement.class),
})
public class SensitiveFieldInterceptor implements Interceptor {

    /**
     * 1. @Signature 指定 type= parameterHandler 后, invocation.getTarget() 则为 ParameterHandler; 2.
     * 若指定为 ResultSetHandler, 则为 ResultSetHandler
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();
        // 获取参数对像: 即 mapper 中 paramsType 的实例
        Field parameterField = parameterHandler.getClass().getDeclaredField("parameterObject");
        parameterField.setAccessible(true);

        // 取出参数
        Object parameterObject = parameterField.get(parameterHandler);
        if (parameterObject != null) {
            Class<?> parameterObjectClass = parameterObject.getClass();
            // 找出该类被 @SensitiveFiled 注解修饰的字段
            val fields = parameterObjectClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(SensitiveField.class)) {
                    field.setAccessible(true);
                    Object object = field.get(parameterObject);
                    field.set(parameterObject, AESUtil.encrypt(object));
                }
            }
        }

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
        if (target instanceof ParameterHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    /**
     * 自定义配置写入, 没有自定义配置的可以直接置空此方法
     *
     * @param properties
     */
    @Override
    public void setProperties(Properties properties) {}
}
