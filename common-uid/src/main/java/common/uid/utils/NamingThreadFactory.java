package common.uid.utils;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Named thread in ThreadFactory. If there is no specified name for thread, it will auto detect
 * using the invoker classname instead.
 *
 * @author zack <br>
 * @create 2021-06-23<br>
 * @project project-custom <br>
 */
@Slf4j
public class NamingThreadFactory implements ThreadFactory {

    /** Sequences for multi thread name prefix */
    private final ConcurrentHashMap<String, AtomicLong> sequences;
    /** Thread name pre */
    private String name;
    /** Is daemon thread */
    private boolean daemon;
    /** UncaughtExceptionHandler */
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    /** Constructors */
    public NamingThreadFactory() {
        this(null, false, null);
    }

    public NamingThreadFactory(String name) {
        this(name, false, null);
    }

    public NamingThreadFactory(String name, boolean daemon) {
        this(name, daemon, null);
    }

    public NamingThreadFactory(
            String name, boolean daemon, Thread.UncaughtExceptionHandler handler) {
        this.name = name;
        this.daemon = daemon;
        this.uncaughtExceptionHandler = handler;
        this.sequences = new ConcurrentHashMap<>();
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setDaemon(this.daemon);

        // If there is no specified name for thread,
        // it will auto detect using the invoker class name instead.
        // Notice that auto detect may cause some performance overhead
        String prefix = this.name;
        if (StrUtil.isBlank(prefix)) {
            prefix = getInvoker(2);
        }
        thread.setName(prefix + "-" + getSequence(prefix));

        // no specified uncaughtExceptionHandler, just do logging.
        if (this.uncaughtExceptionHandler != null) {
            thread.setUncaughtExceptionHandler(this.uncaughtExceptionHandler);
        } else {
            thread.setUncaughtExceptionHandler(
                    (t, e) -> {
                        log.error(
                                "unhandled exception in thread: " + t.getId() + ":" + t.getName(),
                                e);
                    });
        }

        return thread;
    }

    /**
     * Get the method invoker's class name
     *
     * @param depth
     * @return
     */
    private String getInvoker(int depth) {
        Exception e = new Exception();
        StackTraceElement[] stes = e.getStackTrace();
        if (stes.length > depth) {
            return ClassUtil.getShortClassName(stes[depth].getClassName());
        }
        return getClass().getSimpleName();
    }

    /**
     * Get sequence for different naming prefix
     *
     * @param invoker
     * @return
     */
    private long getSequence(String invoker) {
        AtomicLong r = this.sequences.get(invoker);
        if (r == null) {
            r = new AtomicLong(0);
            AtomicLong previous = this.sequences.putIfAbsent(invoker, r);
            if (previous != null) {
                r = previous;
            }
        }

        return r.incrementAndGet();
    }

    /** Getters & Setters */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDaemon() {
        return daemon;
    }

    public void setDaemon(boolean daemon) {
        this.daemon = daemon;
    }

    public Thread.UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return uncaughtExceptionHandler;
    }

    public void setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler handler) {
        this.uncaughtExceptionHandler = handler;
    }
}
