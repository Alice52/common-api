package common.uid.worker.service;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import common.uid.utils.DockerUtils;
import common.uid.utils.NetUtils;
import common.uid.worker.WorkerNodeType;
import common.uid.worker.entity.WorkerNodeEntity;
import common.uid.worker.mapper.WorkerNodeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/**
 * Represents an implementation of {@link IWorkerIdAssigner}, the worker id will be discarded after
 * assigned to the UidGenerator
 *
 * @author zack <br>
 * @create 2021-06-23<br>
 * @project project-custom <br>
 */
@Slf4j
public class DisposableWorkerIdAssigner extends ServiceImpl<WorkerNodeMapper, WorkerNodeEntity>
        implements IWorkerIdAssigner {

    /**
     * Assign worker id base on database.
     *
     * <p>If there is host name & port in the environment, we considered that the node runs in
     * Docker container<br>
     * Otherwise, the node runs on an actual machine.
     *
     * @return assigned worker id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public long assignWorkerId() {
        // build worker node entity
        WorkerNodeEntity workerNodeEntity = buildWorkerNode();

        // add worker node for new (ignore the same IP + PORT)
        baseMapper.insert(workerNodeEntity);
        log.info("Add worker node:" + workerNodeEntity);

        return workerNodeEntity.getId();
    }

    /** Build worker node entity by IP and PORT */
    private WorkerNodeEntity buildWorkerNode() {
        WorkerNodeEntity workerNodeEntity = new WorkerNodeEntity();
        if (DockerUtils.isDocker()) {
            workerNodeEntity.setType(WorkerNodeType.CONTAINER.value());
            workerNodeEntity.setHostName(DockerUtils.getDockerHost());
            workerNodeEntity.setPort(DockerUtils.getDockerPort());

        } else {
            workerNodeEntity.setType(WorkerNodeType.ACTUAL.value());
            workerNodeEntity.setHostName(NetUtils.getLocalAddress());
            workerNodeEntity.setPort(
                    System.currentTimeMillis() + "-" + RandomUtil.randomInt(100000));
        }

        return workerNodeEntity;
    }
}
