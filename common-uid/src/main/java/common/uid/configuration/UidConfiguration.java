package common.uid.configuration;

import common.uid.configuration.properties.StructConfigProperties;
import common.uid.generator.CachedUidGenerator;
import common.uid.generator.DefaultUidGenerator;
import common.uid.generator.UidGenerator;
import common.uid.mp.MpAssignIdGenerator;
import common.uid.worker.service.DisposableWorkerIdAssigner;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;

/**
 * @author zack <br>
 * @create 2021-06-23<br>
 * @project project-custom <br>
 */
@Slf4j
@Configuration
@ConditionalOnProperty(name = "common.uid.enabled", matchIfMissing = true)
@EnableConfigurationProperties(StructConfigProperties.class)
@MapperScan("common.uid.worker")
public class UidConfiguration {

    @Resource private StructConfigProperties configProperties;

    public MpAssignIdGenerator assignIdGenerator(UidGenerator uidGenerator) {
        return new MpAssignIdGenerator(uidGenerator);
    }

    @Bean
    public DisposableWorkerIdAssigner disposableWorkerIdAssigner() {
        return new DisposableWorkerIdAssigner();
    }

    @Lazy(value = false)
    @Bean
    public DefaultUidGenerator defaultUidGenerator() {

        DefaultUidGenerator uidGenerator = new DefaultUidGenerator();
        uidGenerator.setWorkerIdAssigner(disposableWorkerIdAssigner());
        uidGenerator.setTimeBits(configProperties.getTimeBits());
        uidGenerator.setEpochStr(configProperties.getEpochStr());
        uidGenerator.setSeqBits(configProperties.getSeqBits());
        uidGenerator.setWorkerBits(configProperties.getWorkerBits());

        return uidGenerator;
    }

    @Primary
    @Bean
    public CachedUidGenerator cachedUidGenerator() {
        CachedUidGenerator cachedUidGenerator = new CachedUidGenerator();
        cachedUidGenerator.setWorkerIdAssigner(disposableWorkerIdAssigner());
        // RingBuffer size扩容参数, 可提高UID生成的吞吐量
        // 默认 3, 原bufferSize=8192, 扩容后bufferSize= 8192 << 3 = 65536
        cachedUidGenerator.setBoostPower(3);

        // 指定一种RingBuffer填充时机UID, 取值为百分比(0, 100), 默认为50
        // 举例: bufferSize=1024, paddingFactor=50 -> threshold=1024 * 50 / 100 = 512.
        // 当环上可用UID数量 < 512时, 将自动对RingBuffer进行填充补全
        cachedUidGenerator.setPaddingFactor(50);

        // 另外一种RingBuffer填充时机, 在Schedule线程中, 周期性检查填充
        // 默认:不配置此项, 即不实用Schedule线程. 如需使用, 请指定Schedule线程时间间隔, 单位:秒
        // cachedUidGenerator.setScheduleInterval(60);

        // 拒绝策略: 当环已满, 无法继续填充时
        // 默认无需指定, 将丢弃Put操作, 仅日志记录.
        // 如有特殊需求, 请实现RejectedPutBufferHandler接口(支持Lambda表达式)
        // cachedUidGenerator.setRejectedPutBufferHandler(XxxxYourPutRejectPolicy);

        // 拒绝策略: 当环已空, 无法继续获取时;
        // 默认无需指定, 将记录日志, 并抛出异常.
        // 如有特殊需求, 请实现RejectedTakeBufferHandler接口(支持Lambda表达式)
        // cachedUidGenerator.setRejectedTakeBufferHandler(XxxxYourTakeRejectPolicy);
        return cachedUidGenerator;
    }
}
