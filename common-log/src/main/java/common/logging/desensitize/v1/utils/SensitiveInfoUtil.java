package common.logging.desensitize.v1.utils;

public class SensitiveInfoUtil {

    /**
     * [姓名] 只显示第一个汉字, 其他隐藏为星号<例子：李**>
     *
     * @param fullName
     * @return
     */
    public static String chineseName(String fullName) {
        return null;
    }

    /**
     * [身份证号] 显示最后四位，其他隐藏。共计18位或者15位。<例子：*************5762>
     *
     * @param idCardNum
     * @return
     */
    public static String idCardNum(String idCardNum) {
        return null;
    }

    /**
     * [手机号码] 前三位，后四位，其他隐藏<例子:138******1234>
     *
     * @param num
     * @return
     */
    public static String mobilePhone(String num) {
        return null;
    }

    /**
     * [银行卡号] 前六位，后四位，其他用星号隐藏每位1个星号<例子:6222600**********1234>
     *
     * @param cardNum
     * @return
     */
    public static String bankCard(String cardNum) {
        return null;
    }
}
