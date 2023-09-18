package common.uid.worker.service;

import com.baomidou.mybatisplus.extension.service.IService;
import common.uid.generator.DefaultUidGenerator;
import common.uid.worker.entity.WorkerNodeEntity;

/**
 * Represents a worker id assigner for {@link DefaultUidGenerator}
 *
 * @author zack <br>
 * @create 2021-06-23<br>
 * @project project-custom <br>
 */
public interface IWorkerIdAssigner extends IService<WorkerNodeEntity> {

    /**
     * Assign worker id for {@link DefaultUidGenerator}
     *
     * @return assigned worker id
     */
    long assignWorkerId();
}
