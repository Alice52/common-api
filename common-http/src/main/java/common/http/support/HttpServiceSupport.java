package common.http.support;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import common.core.constant.SecurityConstants;
import common.core.util.R;
import common.http.model.PageVO;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author asd <br>
 * @create 2021-12-07 4:57 PM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
public class HttpServiceSupport {

    @Deprecated private static final long DEFAULT_PAGE_SIZE = 200;
    /** the value should less than 7, otherwise it's will lead to longer completed-time */
    private static final int RETRY_LIMIT = 4;

    /**
     * @param sup 调用 HMP 系统获取数据相关的方法
     * @param doUpdateFunc 拿到数据之后修改本地数据库的相关方法
     * @param notifyFunc 发送短信相关的方法
     * @param <T>
     * @param <U>
     */
    public static <T, U> void doSyncTemplate(
            BiFunction<String, Page<T>, R<PageVO<U>>> sup,
            Consumer<List<U>> doUpdateFunc,
            Consumer<List<Long>> notifyFunc) {
        Set<Page<T>> failedPages = new HashSet<>();
        int currentPage = 0;
        Page<T> page = new Page<>(currentPage, DEFAULT_PAGE_SIZE);
        do {
            page.setCurrent(currentPage++);
            if (!doUpdateSpecifiedPage(page, sup, doUpdateFunc)) {
                failedPages.add(new Page<>(page.getCurrent(), page.getSize()));
            }
        } while (currentPage * DEFAULT_PAGE_SIZE < page.getTotal());

        doUpdate4FailedPage(RETRY_LIMIT, failedPages, sup, doUpdateFunc, notifyFunc);
    }

    private static <U, T> void doUpdate4FailedPage(
            int retry,
            Set<Page<T>> failedPages,
            BiFunction<String, Page<T>, R<PageVO<U>>> sup,
            Consumer<List<U>> doUpdateFunc,
            Consumer<List<Long>> notifyFunc) {
        if (CollUtil.isEmpty(failedPages)) {
            return;
        }

        if (retry < 1) {
            List<Long> failedList =
                    failedPages.stream().map(Page::getCurrent).collect(Collectors.toList());
            log.error(
                    "update data failed for page: {} with retry time: {}, "
                            + "and please notice if contains first[zero] page, which means all data sync is failed",
                    failedList,
                    RETRY_LIMIT);
            // send msg when sync failed.
            notifyFunc.accept(failedList);
            return;
        }

        try {
            TimeUnit.SECONDS.sleep(1L << retry);
        } catch (InterruptedException e) {
            log.warn("sleep is interrupted when doUpdate4FailedPage, and ignore this exception");
        }

        HashSet<Page<T>> newFailedPages = new HashSet<>();
        for (Page<T> x : failedPages) {
            boolean success = doUpdateSpecifiedPage(x, sup, doUpdateFunc);
            if (!success) {
                newFailedPages.add(x);
            }
        }

        doUpdate4FailedPage(--retry, newFailedPages, sup, doUpdateFunc, notifyFunc);
    }

    /**
     * @param page 正在进行的页数
     * @param sup 调用 HMP 系统获取数据相关的方法
     * @param doUpdateFunc 拿到数据之后修改本地数据库的相关方法
     * @param <T>
     * @param <U>
     * @return
     */
    public static <T, U> boolean doUpdateSpecifiedPage(
            Page<T> page,
            BiFunction<String, Page<T>, R<PageVO<U>>> sup,
            Consumer<List<U>> doUpdateFunc) {

        R<PageVO<U>> rawData = sup.apply(SecurityConstants.FROM_IN, page);
        PageVO<U> realData;
        if (ObjectUtil.isNull(rawData) || ObjectUtil.isNull(realData = rawData.getData())) {
            log.warn(
                    "the data got from middleware service is empty for page: {}, R: {}",
                    page.getCurrent(),
                    rawData);
            return false;
        }

        page.setTotal(realData.getTotalElements());
        boolean isSuccess = true;
        List<U> records = realData.getResponseData();
        try {
            doUpdateFunc.accept(records);
        } catch (Exception ex) {
            log.warn("update data failed for page: {}, due to: {}", page.getCurrent(), ex);
            isSuccess = false;
        }

        return isSuccess;
    }
}
