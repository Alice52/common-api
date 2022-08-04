package common.core.exception.assertion;

/**
 * @author zack <br>
 * @create 2021-06-01<br>
 * @project project-custom <br>
 */
public interface IBaseErrorResponse {
    /**
     * Error Code. And this value cannot changed anyway.
     *
     * @return
     */
    Integer getErrorCode();

    /**
     * Error message.
     *
     * @return
     */
    String getErrorMsg();

    /**
     * Set Error message.
     *
     * @param errorMsg
     */
    default void setErrorMsg(String errorMsg) {}
}
