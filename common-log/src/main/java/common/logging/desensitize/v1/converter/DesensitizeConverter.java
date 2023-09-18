package common.logging.desensitize.v1.converter;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.google.common.collect.Lists;
import common.logging.desensitize.v1.utils.SensitiveInfoUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used in logback.xml to log Object property.<br>
 * <code>
 *     <conversionRule conversionWord="msg" converterClass="common.converter.DesensitizeConverter"/>
 *     <property name="file.pattern" value="${[%t] %-40.40logger{39} : %msg%n}"/>
 * </code>
 *
 * @link https://blog.csdn.net/rainyear/article/details/120784396
 * @author zack <br>
 * @create 2020-03-06 22:31 <br>
 */
public class DesensitizeConverter extends MessageConverter {
    private static Pattern pattern = Pattern.compile("[0-9a-zA-Z]");
    private static boolean enableDesensitize = true;
    private static List<String> desensitizeKeys =
            Lists.newArrayList("idcard", "realname", "bankcard", "mobile");

    @Override
    public String convert(ILoggingEvent event) {
        String rawMsg = event.getFormattedMessage();

        return invokeMsg(rawMsg);
    }

    /**
     * 处理日志字符串，返回脱敏后的字符串
     *
     * @param rawMsg
     * @return
     */
    public String invokeMsg(String rawMsg) {
        if (!enableDesensitize) {
            return rawMsg;
        }

        for (String key : desensitizeKeys) {
            int index = -1;
            do {
                index = rawMsg.indexOf(key, index + 1);
                if (index != -1) {
                    // 判断key是否为单词字符
                    if (isWordChar(rawMsg, key, index)) {
                        continue;
                    }
                    // 寻找值的开始位置
                    int valueStart = getValueStartIndex(rawMsg, index + key.length());

                    // 查找值的结束位置（逗号，分号）........................
                    int valueEnd = getValuEndEIndex(rawMsg, valueStart);

                    // 对获取的值进行脱敏
                    String subStr = rawMsg.substring(valueStart, valueEnd);
                    subStr = tuomin(subStr, key);
                    ///
                    rawMsg = rawMsg.substring(0, valueStart) + subStr + rawMsg.substring(valueEnd);
                }
            } while (index != -1);
        }

        return rawMsg;
    }

    private String tuomin(String submsg, String key) {
        // idcard：身份证号, realname：姓名, bankcard：银行卡号, mobile：手机号
        if ("idcard".equals(key)) {
            return SensitiveInfoUtil.idCardNum(submsg);
        }
        if ("realname".equals(key)) {
            return SensitiveInfoUtil.chineseName(submsg);
        }
        if ("bankcard".equals(key)) {
            return SensitiveInfoUtil.bankCard(submsg);
        }
        if ("mobile".equals(key)) {
            return SensitiveInfoUtil.mobilePhone(submsg);
        }
        return "";
    }

    /**
     * 判断从字符串msg获取的key值是否为单词 ， index为key在msg中的索引值
     *
     * @return
     */
    private boolean isWordChar(String msg, String key, int index) {

        // 必须确定key是一个单词............................
        if (index != 0) { // 判断key前面一个字符
            char preCh = msg.charAt(index - 1);
            Matcher match = pattern.matcher(preCh + "");
            if (match.matches()) {
                return true;
            }
        }
        // 判断key后面一个字符
        char nextCh = msg.charAt(index + key.length());
        Matcher match = pattern.matcher(nextCh + "");
        if (match.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 获取value值的开始位置
     *
     * @param msg 要查找的字符串
     * @param valueStart 查找的开始位置
     * @return
     */
    private int getValueStartIndex(String msg, int valueStart) {
        // 寻找值的开始位置.................................
        do {
            char ch = msg.charAt(valueStart);
            if (ch == ':' || ch == '=') { // key与 value的分隔符
                valueStart++;
                ch = msg.charAt(valueStart);
                if (ch == '"') {
                    valueStart++;
                }
                break; // 找到值的开始位置
            } else {
                valueStart++;
            }
        } while (true);
        return valueStart;
    }

    /**
     * 获取value值的结束位置
     *
     * @return
     */
    private int getValuEndEIndex(String msg, int valueEnd) {
        do {
            if (valueEnd == msg.length()) {
                break;
            }
            char ch = msg.charAt(valueEnd);

            if (ch == '"') { // 引号时，判断下一个值是结束，分号还是逗号决定是否为值的结束
                if (valueEnd + 1 == msg.length()) {
                    break;
                }
                char nextCh = msg.charAt(valueEnd + 1);
                if (nextCh == ';' || nextCh == ',') {
                    // 去掉前面的 \  处理这种形式的数据
                    while (valueEnd > 0) {
                        char preCh = msg.charAt(valueEnd - 1);
                        if (preCh != '\\') {
                            break;
                        }
                        valueEnd--;
                    }
                    break;
                } else {
                    valueEnd++;
                }
            } else if (ch == ';' || ch == ',' || ch == '}') {
                break;
            } else {
                valueEnd++;
            }

        } while (true);
        return valueEnd;
    }
}
