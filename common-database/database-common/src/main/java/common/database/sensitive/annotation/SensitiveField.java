package common.database.sensitive.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import common.database.sensitive.SensitiveStrategy;

import static common.database.sensitive.SensitiveStrategy.USERNAME;

/**
 * store sensitive data. <br>
 * this can impl by type handler of @TableField. <br>
 * // TODO: but it's just work for insert and query.
 *
 * @see DeSensitive
 * @see SensitiveJsonSerializer
 * @author zack <br>
 * @create 2021-06-09 09:14 <br>
 * @project custom-test <br>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SensitiveField {

    /**
     * this filed has no impact.
     *
     * @return
     */
    @Deprecated
    SensitiveStrategy strategy() default USERNAME;
}
