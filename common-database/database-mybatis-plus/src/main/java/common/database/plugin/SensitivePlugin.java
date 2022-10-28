package common.database.plugin;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;

import com.mysql.jdbc.Statement;
import common.database.sensitive.SensitiveStrategy;
import common.database.sensitive.annotation.SensitiveField;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

/**
 * @author zack <br>
 * @create 2021-06-09 09:22 <br>
 * @project custom-test <br>
 */
@Deprecated
@Slf4j
@Intercepts(
        @Signature(
                type = ResultSetHandler.class,
                method = "handleResultSets",
                args = {Statement.class}))
public class SensitivePlugin implements Interceptor {
    @SuppressWarnings("unchecked")
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        List<Object> records = (List<Object>) invocation.proceed();
        records.forEach(this::sensitive);
        return records;
    }

    /**
     * This is no use.
     *
     * @param target
     * @return
     */
    @Override
    public Object plugin(Object target) {
        if (target instanceof ResultSetHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {}

    private void sensitive(Object source) {
        // 拿到返回值类型
        Class<?> sourceClass = source.getClass();
        // 初始化返回值类型的 MetaObject
        MetaObject metaObject = SystemMetaObject.forObject(source);
        // 捕捉到属性上的标记注解 @Sensitive 并进行对应的脱敏处理
        Stream.of(sourceClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(SensitiveField.class))
                .forEach(field -> doSensitive(metaObject, field));
    }

    private void doSensitive(MetaObject metaObject, Field field) {
        // 拿到属性名
        String name = field.getName();
        // 获取属性值
        Object value = metaObject.getValue(name);
        // 只有字符串类型才能脱敏  而且不能为null
        if (String.class == metaObject.getGetterType(name) && value != null) {
            SensitiveField annotation = field.getAnnotation(SensitiveField.class);
            // 获取对应的脱敏策略 并进行脱敏
            SensitiveStrategy type = annotation.strategy();
            Object o = type.getDesensitizer().apply((String) value);
            // 把脱敏后的值塞回去
            metaObject.setValue(name, o);
        }
    }
}
