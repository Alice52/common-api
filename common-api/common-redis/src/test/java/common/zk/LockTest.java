package common.zk;

import common.redis.reactor.lock.WatchCallBack;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;

/**
 * @author zack <br>
 * @create 2022-03-20 18:48 <br>
 * @project project-cloud-custom <br>
 */
public class LockTest {

    @Test
    public void testlock() {
        ZooKeeper zk = null;
        for (int i = 0; i < 10; i++) {
            new Thread(
                            () -> {
                                WatchCallBack watchCallBack = new WatchCallBack();
                                watchCallBack.setZk(zk);
                                String name = Thread.currentThread().getName();
                                watchCallBack.setThreadName(name);

                                try {
                                    // tryLock
                                    watchCallBack.tryLock();
                                    System.out.println(name + " at work");
                                    watchCallBack.getRootData();
                                    // Thread.sleep(1000);
                                    // unLock
                                    watchCallBack.unLock();
                                } catch (Exception e) {
                                }
                            })
                    .start();
        }
        while (true) {}
    }
}
