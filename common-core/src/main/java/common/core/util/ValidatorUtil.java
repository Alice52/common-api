package common.core.util;

import cn.hutool.core.util.StrUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zack <br>
 * @create 2020-08-01 15:12 <br>
 * @project common-core <br>
 */
public final class ValidatorUtil {

    private static final Pattern MOBILE_PATTERN = Pattern.compile("^[1]\\d{10}$");
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[_A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");

    public static boolean validateMobile(String phoneNumber) {
        if (StrUtil.isEmpty(phoneNumber)) {
            return false;
        }
        Matcher m = MOBILE_PATTERN.matcher(phoneNumber);
        return m.matches();
    }

    public static boolean validateEmail(String email) {

        if (StrUtil.isEmpty(email)) {
            return false;
        }
        Matcher m = EMAIL_PATTERN.matcher(email);
        return m.matches();
    }
}
