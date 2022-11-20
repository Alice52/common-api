package common.uid.mp;

import common.uid.generator.UidGenerator;

/**
 * @author zack <br>
 * @create 2022-11-18 23:29 <br>
 * @project custom-auth <br>
 */
public class MpAssignIdGenerator extends CustomerIdGenerator {
    public MpAssignIdGenerator(UidGenerator uidGenerator) {
        super(uidGenerator);
    }
}
