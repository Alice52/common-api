package top.hubby.mq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import top.hubby.mq.constants.enums.EventStatus;
import top.hubby.mq.mapper.DtxEventMapper;
import top.hubby.mq.model.entity.DtxEvent;
import top.hubby.mq.service.DtxEventService;

/**
 * @author zack <br>
 * @create 2022-04-12 12:10 <br>
 * @project project-cloud-custom <br>
 */
public class DtxEventServiceImpl extends ServiceImpl<DtxEventMapper, DtxEvent>
        implements DtxEventService {

    @Override
    public boolean updateEventStatus(Long id, EventStatus status) {

        return lambdaUpdate().set(DtxEvent::getStatus, status).eq(DtxEvent::getId, id).update();
    }

    @Override
    public Long createEvent(long uid, String type, String content, EventStatus status) {
        DtxEvent event =
                DtxEvent.builder().id(uid).eventType(type).content(content).status(status).build();

        baseMapper.insert(event);

        return event.getId();
    }
}
