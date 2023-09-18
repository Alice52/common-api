package common.uid.generator;

import common.core.exception.BaseException;

/**
 * Represents a unique id generator.
 *
 * @author zack <br>
 * @create 2021-06-23<br>
 * @project project-custom <br>
 */
public interface UidGenerator {

    /**
     * Get a unique ID
     *
     * @return UID
     * @throws BaseException
     */
    long getUID() throws BaseException;

    /**
     * Parse the UID into elements which are used to generate the UID. <br>
     * Such as timestamp & workerId & sequence...
     *
     * @param uid
     * @return Parsed info
     */
    String parseUID(long uid);
}
