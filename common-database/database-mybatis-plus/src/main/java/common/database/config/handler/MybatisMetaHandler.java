package common.database.config.handler;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import org.springframework.stereotype.Component;

/**
 * @author zack <br>
 * @create 2021-04-20 17:28 <br>
 * @project database-mybatis-plus <br>
 */
@Slf4j
@Component
public class MybatisMetaHandler implements MetaObjectHandler {
    /**
     * 新增数据执行
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("insertedTime", LocalDateTime.now(), metaObject);
        Long memberId = 0L;

        if (memberId != null) {
            this.setFieldValByName("insertedBy", memberId, metaObject);
        }
    }

    /**
     * 更新数据执行
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updatedTime", LocalDateTime.now(), metaObject);
        Long memberId = 0L;

        if (memberId != null) {
            this.setFieldValByName("updatedBy", memberId, metaObject);
        }
    }
}
