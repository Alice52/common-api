package common.core.exception.assertion;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

import common.core.constant.enums.CommonResponseEnum;
import common.core.exception.BaseException;
import common.core.exception.WrapMessageException;

/**
 * @author zack <br>
 * @create 2021-06-01<br>
 * @project project-custom <br>
 */
public interface IBaseAssert {

    /**
     * change response body. <br>
     * default code is 999999999 and message is Unknown Error.
     */
    static void assertFail2Response() {
        throw new BaseException(CommonResponseEnum.INTERNAL_ERROR);
    }

    /**
     * change response body using errMsg and default code is 999999999.
     *
     * @param errMsg
     * @param args
     */
    static void assertFail2Response(String errMsg, Object... args) {
        throw new BaseException(
                CommonResponseEnum.INTERNAL_ERROR.getErrorCode(),
                MessageFormat.format(errMsg, args));
    }

    /**
     * change response body using errMsg and errCode.
     *
     * @param errCode
     * @param errMsg
     */
    static void assertFail2Response(Integer errCode, String errMsg) {
        throw new BaseException(errCode, errMsg);
    }

    /**
     * change response body using errMsg[args] and errCode
     *
     * @param errCode
     * @param errMsg
     * @param args
     */
    static void assertFail2Response(Integer errCode, String errMsg, Object... args) {
        throw new BaseException(errCode, MessageFormat.format(errMsg, args));
    }

    /**
     * This method must be Override by sub-class, which can create specified exception.
     *
     * @param args this is for error message[enum message value] palceholder
     * @return
     */
    BaseException newException(Object... args);

    /**
     * This method must be Override by sub-class, which can create specified exception.
     *
     * @param t
     * @param args this is for error message[enum message value] palceholder
     * @return
     */
    BaseException newException(Throwable t, Object... args);

    /**
     * Create exception for specified IXxAssert and put the errMsg to exception[log].
     *
     * @param errMsg Error message, which can contain placeholder.
     * @param args real args for error message place-holder.
     * @return
     */
    default BaseException newExceptionWithMsg(String errMsg, Object... args) {
        if (args != null && args.length > 0) {
            errMsg = MessageFormat.format(errMsg, args);
        }

        throw newException(new WrapMessageException(errMsg), args);
    }

    /**
     * Create exception for specified IXxAssert and put the errMsg to exception[log].
     *
     * @param errMsg Error message, which can contain placeholder.
     * @param args real args for error message place-holder.
     * @return
     */
    default BaseException newExceptionWithMsg(String errMsg, Throwable t, Object... args) {
        if (args != null && args.length > 0) {
            errMsg = MessageFormat.format(errMsg, args);
        }

        throw newException(new WrapMessageException(errMsg, t), args);
    }

    /**
     * Assert <code>obj</code> whether not null. <br>
     * If <code>obj</code> is null, thows exception.
     *
     * @param obj: Object to be judged
     */
    default void assertNotNull(Object obj) {
        if (obj == null) {
            throw newException();
        }
    }

    /**
     * Assert <code>obj</code> whether not null. <br>
     * If <code>obj</code> is null, thows exception.
     *
     * @param obj Object to be judged
     * @param args this is for error message[enum message value] palceholder
     */
    default void assertNotNull(Object obj, Object... args) {
        if (obj == null) {
            throw newException(args);
        }
    }

    /**
     * Assert <code>obj</code> whether not null. <br>
     * If <code>obj</code> is null, thows exception. And should avoid to join string before.
     *
     * @param obj Object to be judged
     * @param errMsg error message, which will be show in exception[log], and not in response.
     */
    default void assertNotNullWithMsg(Object obj, String errMsg) {
        if (obj == null) {
            throw newExceptionWithMsg(errMsg);
        }
    }

    /**
     * Assert <code>obj</code> whether not null. <br>
     * If <code>obj</code> is null, thows exception. And should avoid to join string before.
     *
     * @param obj Object to be judged
     * @param errMsg error message, containing placeholder.
     * @param args this is for errMsg palceholder, show in exception[log]
     */
    default void assertNotNullWithMsg(Object obj, String errMsg, Object... args) {
        if (obj == null) {
            throw newExceptionWithMsg(errMsg, args);
        }
    }

    /**
     * Assert <code>obj</code> whether not null. <br>
     * If <code>obj</code> is null, thows exception.
     *
     * @param obj Object to be judged
     * @param errMsg error message supplier function, which is show in exception.
     */
    default void assertNotNullWithMsg(Object obj, Supplier<String> errMsg) {
        if (obj == null) {
            throw newExceptionWithMsg(errMsg.get());
        }
    }

    /**
     * Assert <code>obj</code> whether not null. <br>
     * If <code>obj</code> is null, thows exception.
     *
     * @param obj Object to be judged
     * @param errMsg error message function containing placeholder, and show in exception
     * @param args this is for errMsg palceholder, show in exception[log]
     */
    default void assertNotNullWithMsg(Object obj, Supplier<String> errMsg, Object... args) {
        if (obj == null) {
            throw newExceptionWithMsg(errMsg.get(), args);
        }
    }

    /**
     * 断言字符串<code>str</code>不为空串（长度为0）。如果字符串<code>str</code>为空串，则抛出异常
     *
     * @param str 待判断字符串
     */
    default void assertNotEmpty(String str) {
        if (null == str || "".equals(str.trim())) {
            throw newException();
        }
    }

    /**
     * 断言字符串<code>str</code>不为空串（长度为0）。如果字符串<code>str</code>为空串，则抛出异常
     *
     * <p>异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
     *
     * @param str 待判断字符串
     * @param args message占位符对应的参数列表
     */
    default void assertNotEmpty(String str, Object... args) {
        if (str == null || "".equals(str.trim())) {
            throw newException(args);
        }
    }

    /**
     * 断言字符串<code>str</code>不为空串（长度为0）。如果字符串<code>str</code>为空串，则抛出异常
     *
     * @param str 待判断字符串
     * @param errMsg 自定义的错误信息
     */
    default void assertNotEmptyWithMsg(String str, String errMsg) {
        if (null == str || "".equals(str.trim())) {
            throw newExceptionWithMsg(errMsg);
        }
    }

    /**
     * 断言字符串<code>str</code>不为空串（长度为0）。如果字符串<code>str</code>为空串，则抛出异常
     *
     * <p>异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
     *
     * @param str 待判断字符串
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertNotEmptyWithMsg(String str, String errMsg, Object... args) {
        if (str == null || "".equals(str.trim())) {
            throw newExceptionWithMsg(errMsg, args);
        }
    }

    /**
     * 断言数组<code>arrays</code>大小不为0。如果数组<code>arrays</code>大小不为0，则抛出异常
     *
     * @param arrays 待判断数组
     */
    default void assertNotEmpty(Object[] arrays) {
        if (arrays == null || arrays.length == 0) {
            throw newException();
        }
    }

    /**
     * 断言数组<code>arrays</code>大小不为0。如果数组<code>arrays</code>大小不为0，则抛出异常
     *
     * <p>异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
     *
     * @param arrays 待判断数组
     * @param args message占位符对应的参数列表
     */
    default void assertNotEmpty(Object[] arrays, Object... args) {
        if (arrays == null || arrays.length == 0) {
            throw newException(args);
        }
    }

    /**
     * 断言数组<code>arrays</code>大小不为0。如果数组<code>arrays</code>大小不为0，则抛出异常
     *
     * @param arrays 待判断数组
     * @param errMsg 自定义的错误信息
     */
    default void assertNotEmptyWithMsg(Object[] arrays, String errMsg) {
        if (arrays == null || arrays.length == 0) {
            throw newExceptionWithMsg(errMsg);
        }
    }

    /**
     * 断言数组<code>arrays</code>大小不为0。如果数组<code>arrays</code>大小不为0，则抛出异常
     *
     * <p>异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
     *
     * @param arrays 待判断数组
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertNotEmptyWithMsg(Object[] arrays, String errMsg, Object... args) {
        if (arrays == null || arrays.length == 0) {
            throw newExceptionWithMsg(errMsg, args);
        }
    }

    /**
     * 断言数组<code>arrays</code>大小不为0。如果数组<code>arrays</code>大小不为0，则抛出异常
     *
     * @param arrays 待判断数组
     * @param errMsg 自定义的错误信息
     */
    default void assertNotEmptyWithMsg(Object[] arrays, Supplier<String> errMsg) {
        if (arrays == null || arrays.length == 0) {
            throw newExceptionWithMsg(errMsg.get());
        }
    }

    /**
     * 断言数组<code>arrays</code>大小不为0。如果数组<code>arrays</code>大小不为0，则抛出异常
     *
     * <p>异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
     *
     * @param arrays 待判断数组
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertNotEmptyWithMsg(Object[] arrays, Supplier<String> errMsg, Object... args) {
        if (arrays == null || arrays.length == 0) {
            throw newExceptionWithMsg(errMsg.get(), args);
        }
    }

    /**
     * 断言集合<code>c</code>大小不为0。如果集合<code>c</code>大小不为0，则抛出异常
     *
     * @param c 待判断数组
     */
    default void assertNotEmpty(Collection<?> c) {
        if (c == null || c.isEmpty()) {
            throw newException();
        }
    }

    /**
     * 断言集合<code>c</code>大小不为0。如果集合<code>c</code>大小不为0，则抛出异常
     *
     * @param c 待判断数组
     * @param args message占位符对应的参数列表
     */
    default void assertNotEmpty(Collection<?> c, Object... args) {
        if (c == null || c.isEmpty()) {
            throw newException(args);
        }
    }

    /**
     * 断言集合<code>c</code>大小不为0。如果集合<code>c</code>大小不为0，则抛出异常
     *
     * @param c 待判断数组
     * @param errMsg 自定义的错误信息
     */
    default void assertNotEmptyWithMsg(Collection<?> c, String errMsg) {
        if (c == null || c.isEmpty()) {
            throw newExceptionWithMsg(errMsg);
        }
    }

    /**
     * 断言集合<code>c</code>大小不为0。如果集合<code>c</code>大小不为0，则抛出异常
     *
     * @param c 待判断数组
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertNotEmptyWithMsg(Collection<?> c, String errMsg, Object... args) {
        if (c == null || c.isEmpty()) {
            throw newExceptionWithMsg(errMsg, args);
        }
    }

    /**
     * 断言集合<code>c</code>大小不为0。如果集合<code>c</code>大小不为0，则抛出异常
     *
     * @param c 待判断数组
     * @param errMsg 自定义的错误信息
     */
    default void assertNotEmptyWithMsg(Collection<?> c, Supplier<String> errMsg) {
        if (c == null || c.isEmpty()) {
            throw newExceptionWithMsg(errMsg.get());
        }
    }

    /**
     * 断言集合<code>c</code>大小不为0。如果集合<code>c</code>大小不为0，则抛出异常
     *
     * @param c 待判断数组
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertNotEmptyWithMsg(Collection<?> c, Supplier<String> errMsg, Object... args) {
        if (c == null || c.isEmpty()) {
            throw newExceptionWithMsg(errMsg.get(), args);
        }
    }

    /**
     * 断言Map<code>map</code>大小不为0。如果Map<code>map</code>大小不为0，则抛出异常
     *
     * @param map 待判断Map
     */
    default void assertNotEmpty(Map<?, ?> map) {
        if (map == null || map.isEmpty()) {
            throw newException();
        }
    }

    /**
     * 断言Map<code>map</code>大小不为0。如果Map<code>map</code>大小不为0，则抛出异常
     *
     * @param map 待判断Map
     * @param args message占位符对应的参数列表
     */
    default void assertNotEmpty(Map<?, ?> map, Object... args) {
        if (map == null || map.isEmpty()) {
            throw newException(args);
        }
    }

    /**
     * 断言Map<code>map</code>大小不为0。如果Map<code>map</code>大小不为0，则抛出异常
     *
     * @param map 待判断Map
     * @param errMsg 自定义的错误信息
     */
    default void assertNotEmptyWithMsg(Map<?, ?> map, String errMsg) {
        if (map == null || map.isEmpty()) {
            throw newExceptionWithMsg(errMsg);
        }
    }

    /**
     * 断言Map<code>map</code>大小不为0。如果Map<code>map</code>大小不为0，则抛出异常
     *
     * @param map 待判断Map
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertNotEmptyWithMsg(Map<?, ?> map, String errMsg, Object... args) {
        if (map == null || map.isEmpty()) {
            throw newExceptionWithMsg(errMsg, args);
        }
    }

    /**
     * 断言Map<code>map</code>大小不为0。如果Map<code>map</code>大小不为0，则抛出异常
     *
     * @param map 待判断Map
     * @param errMsg 自定义的错误信息
     */
    default void assertNotEmptyWithMsg(Map<?, ?> map, Supplier<String> errMsg) {
        if (map == null || map.isEmpty()) {
            throw newExceptionWithMsg(errMsg.get());
        }
    }

    /**
     * 断言Map<code>map</code>大小不为0。如果Map<code>map</code>大小不为0，则抛出异常
     *
     * @param map 待判断Map
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertNotEmptyWithMsg(Map<?, ?> map, Supplier<String> errMsg, Object... args) {
        if (map == null || map.isEmpty()) {
            throw newExceptionWithMsg(errMsg.get(), args);
        }
    }

    /**
     * 断言布尔值<code>expression</code>为false。如果布尔值<code>expression</code>为true，则抛出异常
     *
     * @param expression 待判断布尔变量
     */
    default void assertIsFalse(boolean expression) {
        if (expression) {
            throw newException();
        }
    }

    /**
     * 断言布尔值<code>expression</code>为false。如果布尔值<code>expression</code>为true，则抛出异常
     *
     * @param expression 待判断布尔变量
     * @param args message占位符对应的参数列表
     */
    default void assertIsFalse(boolean expression, Object... args) {
        if (expression) {
            throw newException(args);
        }
    }

    /**
     * 断言布尔值<code>expression</code>为false。如果布尔值<code>expression</code>为true，则抛出异常
     *
     * @param expression 待判断布尔变量
     * @param errMsg 自定义的错误信息
     */
    default void assertIsFalseWithMsg(boolean expression, String errMsg) {
        if (expression) {
            throw newExceptionWithMsg(errMsg);
        }
    }

    /**
     * 断言布尔值<code>expression</code>为false。如果布尔值<code>expression</code>为true，则抛出异常
     *
     * @param expression 待判断布尔变量
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertIsFalseWithMsg(boolean expression, String errMsg, Object... args) {
        if (expression) {
            throw newExceptionWithMsg(errMsg, args);
        }
    }

    /**
     * 断言布尔值<code>expression</code>为false。如果布尔值<code>expression</code>为true，则抛出异常
     *
     * @param expression 待判断布尔变量
     * @param errMsg 自定义的错误信息
     */
    default void assertIsFalseWithMsg(boolean expression, Supplier<String> errMsg) {
        if (expression) {
            throw newExceptionWithMsg(errMsg.get());
        }
    }

    /**
     * 断言布尔值<code>expression</code>为false。如果布尔值<code>expression</code>为true，则抛出异常
     *
     * @param expression 待判断布尔变量
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertIsFalseWithMsg(boolean expression, Supplier<String> errMsg, Object... args) {
        if (expression) {
            throw newExceptionWithMsg(errMsg.get(), args);
        }
    }

    /**
     * 断言布尔值<code>expression</code>为true。如果布尔值<code>expression</code>为false，则抛出异常
     *
     * @param expression 待判断布尔变量
     */
    default void assertIsTrue(boolean expression) {
        if (!expression) {
            throw newException();
        }
    }

    /**
     * 断言布尔值<code>expression</code>为true。如果布尔值<code>expression</code>为false，则抛出异常
     *
     * @param expression 待判断布尔变量
     * @param args message占位符对应的参数列表
     */
    default void assertIsTrue(boolean expression, Object... args) {
        if (!expression) {
            throw newException(args);
        }
    }

    /**
     * 断言布尔值<code>expression</code>为true。如果布尔值<code>expression</code>为false，则抛出异常
     *
     * @param expression 待判断布尔变量
     * @param errMsg 自定义的错误信息
     */
    default void assertIsTrueWithMsg(boolean expression, String errMsg) {
        if (!expression) {
            throw newExceptionWithMsg(errMsg);
        }
    }

    /**
     * 断言布尔值<code>expression</code>为true。如果布尔值<code>expression</code>为false，则抛出异常
     *
     * @param expression 待判断布尔变量
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertIsTrueWithMsg(boolean expression, String errMsg, Object... args) {
        if (!expression) {
            throw newExceptionWithMsg(errMsg, args);
        }
    }

    /**
     * 断言布尔值<code>expression</code>为true。如果布尔值<code>expression</code>为false，则抛出异常
     *
     * @param expression 待判断布尔变量
     * @param errMsg 自定义的错误信息
     */
    default void assertIsTrueWithMsg(boolean expression, Supplier<String> errMsg) {
        if (!expression) {
            throw newExceptionWithMsg(errMsg.get());
        }
    }

    /**
     * 断言布尔值<code>expression</code>为true。如果布尔值<code>expression</code>为false，则抛出异常
     *
     * @param expression 待判断布尔变量
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertIsTrueWithMsg(boolean expression, Supplier<String> errMsg, Object... args) {
        if (!expression) {
            throw newExceptionWithMsg(errMsg.get(), args);
        }
    }

    /**
     * 断言对象<code>obj</code>为<code>null</code>。如果对象<code>obj</code>不为<code>null</code>，则抛出异常
     *
     * @param obj 待判断对象
     */
    default void assertIsNull(Object obj) {
        if (obj != null) {
            throw newException();
        }
    }

    /**
     * 断言对象<code>obj</code>为<code>null</code>。如果对象<code>obj</code>不为<code>null</code>，则抛出异常
     *
     * @param obj 待判断布尔变量
     * @param args message占位符对应的参数列表
     */
    default void assertIsNull(Object obj, Object... args) {
        if (obj != null) {
            throw newException(args);
        }
    }

    /**
     * 断言对象<code>obj</code>为<code>null</code>。如果对象<code>obj</code>不为<code>null</code>，则抛出异常
     *
     * @param obj 待判断对象
     * @param errMsg 自定义的错误信息
     */
    default void assertIsNullWithMsg(Object obj, String errMsg) {
        if (obj != null) {
            throw newExceptionWithMsg(errMsg);
        }
    }

    /**
     * 断言对象<code>obj</code>为<code>null</code>。如果对象<code>obj</code>不为<code>null</code>，则抛出异常
     *
     * @param obj 待判断布尔变量
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertIsNullWithMsg(Object obj, String errMsg, Object... args) {
        if (obj != null) {
            throw newExceptionWithMsg(errMsg, args);
        }
    }

    /**
     * 断言对象<code>obj</code>为<code>null</code>。如果对象<code>obj</code>不为<code>null</code>，则抛出异常
     *
     * @param obj 待判断对象
     * @param errMsg 自定义的错误信息
     */
    default void assertIsNullWithMsg(Object obj, Supplier<String> errMsg) {
        if (obj != null) {
            throw newExceptionWithMsg(errMsg.get());
        }
    }

    /**
     * 断言对象<code>obj</code>为<code>null</code>。如果对象<code>obj</code>不为<code>null</code>，则抛出异常
     *
     * @param obj 待判断布尔变量
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertIsNullWithMsg(Object obj, Supplier<String> errMsg, Object... args) {
        if (obj != null) {
            throw newExceptionWithMsg(errMsg.get(), args);
        }
    }

    /**
     * 断言对象<code>o1</code>与对象<code>o2</code>相等，此处的相等指（o1.equals(o2)为true）。 如果两对象不相等，则抛出异常
     *
     * @param o1 待判断对象，若<code>o1</code>为null，也当作不相等处理
     * @param o2 待判断对象
     */
    default void assertEquals(Object o1, Object o2) {
        if (o1 == o2) {
            return;
        }
        if (o1 == null || !o1.equals(o2)) {
            throw newException();
        }
    }

    /**
     * 断言对象<code>o1</code>与对象<code>o2</code>相等，此处的相等指（o1.equals(o2)为true）。 如果两对象不相等，则抛出异常
     *
     * @param o1 待判断对象，若<code>o1</code>为null，也当作不相等处理
     * @param o2 待判断对象
     * @param args message占位符对应的参数列表
     */
    default void assertEquals(Object o1, Object o2, Object... args) {
        if (o1 == o2) {
            return;
        }
        if (o1 == null || !o1.equals(o2)) {
            throw newException(args);
        }
    }

    /**
     * 断言对象<code>o1</code>与对象<code>o2</code>相等，此处的相等指（o1.equals(o2)为true）。 如果两对象不相等，则抛出异常
     *
     * @param o1 待判断对象，若<code>o1</code>为null，也当作不相等处理
     * @param o2 待判断对象
     * @param errMsg 自定义的错误信息
     */
    default void assertEqualsWithMsg(Object o1, Object o2, String errMsg) {
        if (o1 == o2) {
            return;
        }
        if (o1 == null || !o1.equals(o2)) {
            throw newExceptionWithMsg(errMsg);
        }
    }

    /**
     * 断言对象<code>o1</code>与对象<code>o2</code>相等，此处的相等指（o1.equals(o2)为true）。 如果两对象不相等，则抛出异常
     *
     * @param o1 待判断对象，若<code>o1</code>为null，也当作不相等处理
     * @param o2 待判断对象
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertEqualsWithMsg(Object o1, Object o2, String errMsg, Object... args) {
        if (o1 == o2) {
            return;
        }
        if (o1 == null || !o1.equals(o2)) {
            throw newExceptionWithMsg(errMsg, args);
        }
    }

    /**
     * 断言对象<code>o1</code>与对象<code>o2</code>相等，此处的相等指（o1.equals(o2)为true）。 如果两对象不相等，则抛出异常
     *
     * @param o1 待判断对象，若<code>o1</code>为null，也当作不相等处理
     * @param o2 待判断对象
     * @param errMsg 自定义的错误信息
     */
    default void assertEqualsWithMsg(Object o1, Object o2, Supplier<String> errMsg) {
        if (o1 == o2) {
            return;
        }
        if (o1 == null || !o1.equals(o2)) {
            throw newExceptionWithMsg(errMsg.get());
        }
    }

    /**
     * 断言对象<code>o1</code>与对象<code>o2</code>相等，此处的相等指（o1.equals(o2)为true）。 如果两对象不相等，则抛出异常
     *
     * @param o1 待判断对象，若<code>o1</code>为null，也当作不相等处理
     * @param o2 待判断对象
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertEqualsWithMsg(
            Object o1, Object o2, Supplier<String> errMsg, Object... args) {
        if (o1 == o2) {
            return;
        }
        if (o1 == null || !o1.equals(o2)) {
            throw newExceptionWithMsg(errMsg.get(), args);
        }
    }

    /** 直接抛出异常 */
    default void assertFail() {
        throw newException();
    }

    /**
     * 直接抛出异常
     *
     * @param args message占位符对应的参数列表
     */
    default void assertFail(Object... args) {
        throw newException(args);
    }

    /**
     * 直接抛出异常，并包含原异常信息
     *
     * <p>当捕获非运行时异常（非继承{@link RuntimeException}）时，并该异常进行业务描述时， 必须传递原始异常，作为新异常的cause
     *
     * @param t 原始异常
     */
    default void assertFail(Throwable t) {
        throw newException(t);
    }

    /**
     * 直接抛出异常，并包含原异常信息
     *
     * <p>当捕获非运行时异常（非继承{@link RuntimeException}）时，并该异常进行业务描述时， 必须传递原始异常，作为新异常的cause
     *
     * @param t 原始异常
     * @param args message占位符对应的参数列表
     */
    default void assertFail(Throwable t, Object... args) {
        throw newException(t, args);
    }

    /**
     * 直接抛出异常
     *
     * @param errMsg 自定义的错误信息
     */
    default void assertFailWithMsg(String errMsg) {
        throw newExceptionWithMsg(errMsg);
    }

    /**
     * 直接抛出异常
     *
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertFailWithMsg(String errMsg, Object... args) {
        throw newExceptionWithMsg(errMsg, args);
    }

    /**
     * 直接抛出异常，并包含原异常信息
     *
     * <p>当捕获非运行时异常（非继承{@link RuntimeException}）时，并该异常进行业务描述时， 必须传递原始异常，作为新异常的cause
     *
     * @param errMsg 自定义的错误信息
     * @param t 原始异常
     */
    default void assertFailWithMsg(String errMsg, Throwable t) {
        throw newExceptionWithMsg(errMsg, t);
    }

    /**
     * 直接抛出异常，并包含原异常信息
     *
     * <p>当捕获非运行时异常（非继承{@link RuntimeException}）时，并该异常进行业务描述时， 必须传递原始异常，作为新异常的cause
     *
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param t 原始异常
     * @param args message占位符对应的参数列表
     */
    default void assertFailWithMsg(String errMsg, Throwable t, Object... args) {
        throw newExceptionWithMsg(errMsg, t, args);
    }

    /**
     * 直接抛出异常
     *
     * @param errMsg 自定义的错误信息
     */
    default void assertFailWithMsg(Supplier<String> errMsg) {
        throw newExceptionWithMsg(errMsg.get());
    }

    /**
     * 直接抛出异常
     *
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args message占位符对应的参数列表
     */
    default void assertFailWithMsg(Supplier<String> errMsg, Object... args) {
        throw newExceptionWithMsg(errMsg.get(), args);
    }

    /**
     * 直接抛出异常，并包含原异常信息
     *
     * <p>当捕获非运行时异常（非继承{@link RuntimeException}）时，并该异常进行业务描述时， 必须传递原始异常，作为新异常的cause
     *
     * @param errMsg 自定义的错误信息
     * @param t 原始异常
     */
    default void assertFailWithMsg(Supplier<String> errMsg, Throwable t) {
        throw newExceptionWithMsg(errMsg.get(), t);
    }

    /**
     * 直接抛出异常，并包含原异常信息
     *
     * <p>当捕获非运行时异常（非继承{@link RuntimeException}）时，并该异常进行业务描述时， 必须传递原始异常，作为新异常的cause
     *
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param t 原始异常
     * @param args message占位符对应的参数列表
     */
    default void assertFailWithMsg(Supplier<String> errMsg, Throwable t, Object... args) {
        throw newExceptionWithMsg(errMsg.get(), t, args);
    }
}
