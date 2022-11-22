package common.uid.mp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import common.uid.generator.UidGenerator;

/**
 * https://blog.csdn.net/qq_43437874/article/details/115858034
 *
 * @see IdType#ASSIGN_ID
 * @author asd <br>
 * @create 2021-12-23 2:51 PM <br>
 * @project project-cloud-custom <br>
 */
public class CustomerIdGenerator implements IdentifierGenerator {

    /**
     * @Resource private UidGenerator uidGenerator;
     */
    private UidGenerator uidGenerator;

    public CustomerIdGenerator(UidGenerator uidGenerator) {
        this.uidGenerator = uidGenerator;
    }

    @Override
    public Number nextId(Object entity) {
        // 填充自己的Id生成器，
        return uidGenerator.getUID();
    }
}
