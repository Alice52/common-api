package common.database.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import common.core.util.security.AesUtil;
import common.database.sensitive.annotation.SensitiveField;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

/**
 * @author zack <br>
 * @create 2022-03-31 10:52 <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
@Intercepts({
    @Signature(
            type = ResultSetHandler.class,
            method = "handleResultSets",
            args = {Statement.class})
})
public class DeSensitiveFieldInterceptor implements Interceptor {

    private static void decryptObject(Object result) throws IllegalAccessException {
        val fileds = result.getClass().getDeclaredFields();
        for (Field field : fileds) {
            if (field.isAnnotationPresent(SensitiveField.class)) {
                field.setAccessible(true);
                Object orgin = field.get(result);
                if (orgin instanceof String) {
                    try {
                        Object decrypt = AesUtil.decryptOrNull((String) orgin, field.getType());
                        field.set(result, decrypt);
                    } catch (Exception ex) {
                        log.error("decrypt failed for filed: {}", JSONUtil.toJsonStr(fileds));
                    }
                }
            }
        }
    }

    private static boolean needToDecrypt(Object object) {

        Object newObj;
        if (!(object instanceof List)) {
            newObj = object;
        } else {
            List<?> list = (List<?>) object;
            if (CollUtil.isEmpty(list)) {
                return false;
            }
            newObj = list.get(0);
        }

        Field[] fields = newObj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(SensitiveField.class)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object resultObject = invocation.proceed();
        if (Objects.isNull(resultObject)) {
            return null;
        }

        if (!needToDecrypt(resultObject)) {
            return resultObject;
        }

        if (!(resultObject instanceof List)) {
            decryptObject(resultObject);
            return resultObject;
        }

        List<?> resultList = (List<?>) resultObject;
        for (Object result : resultList) {
            decryptObject(result);
        }

        return resultObject;
    }

    @Override
    public Object plugin(Object target) {

        if (target instanceof ResultSetHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {}
}
