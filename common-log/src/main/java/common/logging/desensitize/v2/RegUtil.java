package common.logging.desensitize.v2;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zack <br/>
 * @create 2023-02-01 00:21 <br/>
 * @project common-api <br/>
 */
@UtilityClass
public class RegUtil {
    public boolean isEmail(String str) {
        return str.matches("^[\\w-]+@[\\w-]+(\\.[\\w-]+)+$");
    }

    public boolean isIdentity(String str) {
        return str.matches("(^\\d{18}$)|(^\\d{15}$)");
    }

    public boolean isMobile(String str) {
        return str.matches("^1[0-9]{10}$");
    }
}
