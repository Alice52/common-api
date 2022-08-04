package common.redis.reactor.lock;

import lombok.Data;
import lombok.SneakyThrows;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author zack <br>
 * @create 2022-03-20 18:39 <br>
 * @project project-cloud-custom <br>
 */
@Data
public class WatchCallBack
        implements Watcher,
                AsyncCallback.StringCallback,
                AsyncCallback.Children2Callback,
                AsyncCallback.StatCallback,
                AsyncCallback.DataCallback {

    private ZooKeeper zk;
    private String lockName;
    private String threadName;
    private CountDownLatch cc = new CountDownLatch(1);

    @SneakyThrows
    public void tryLock() {
        // 重入
        zk.create(
                "/lock",
                threadName.getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL,
                this,
                threadName);
        cc.await();
    }

    public void getRootData() throws KeeperException, InterruptedException {
        byte[] data = zk.getData("/", false, new Stat());
        System.out.println(new String(data));
    }

    @SneakyThrows
    public void unLock() {
        zk.delete("/" + lockName, -1);
    }

    /**
     * get children callback
     *
     * @param rc
     * @param path
     * @param ctx
     * @param children
     * @param stat
     */
    @SneakyThrows
    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {

        // 获得所目录的所有有序节点，然后排序，然后取自己在有序list中的index
        if (children == null) {
            System.out.println(ctx.toString() + "list null");
            return;
        }

        Collections.sort(children);
        int i = children.indexOf(lockName);
        if (i < 1) {
            System.out.println(threadName + " i am first...");
            zk.setData("/", threadName.getBytes(), -1);
            cc.countDown();
        } else {
            System.out.println(threadName + " watch " + children.get(i - 1));
            zk.exists("/" + children.get(i - 1), this);
        }
    }

    /**
     * create callback
     *
     * @param rc
     * @param path
     * @param ctx
     * @param name
     */
    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
        // 每个线程启动后创建锁，然后get锁目录的所有孩子，不注册watch在锁目录
        System.out.println(ctx.toString() + " create path: " + name);
        lockName = name.substring(1);
        zk.getChildren("/", false, this, ctx);
    }

    @Override
    public void process(WatchedEvent event) {
        // 如果第一个哥们，那个锁释放了，其实只有第二个收到了回调事件！！
        // 如果，不是第一个哥们，某一个，挂了，也能造成他后边的收到这个通知，从而让他后边那个跟去watch挂掉这个哥们前边的。。。
        switch (event.getType()) {
            case None:
                break;
            case NodeCreated:
                break;
            case NodeDeleted:
                zk.getChildren("/", false, this, "sdf");
                break;
            case NodeDataChanged:
                break;
            case NodeChildrenChanged:
                break;
            default:
                break;
        }
    }

    /**
     * DataCallback
     *
     * @param rc
     * @param path
     * @param ctx
     * @param data
     * @param stat
     */
    @Override
    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {}

    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {

        // 监控失败了怎么办
    }
}
