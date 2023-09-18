### 百度[uid-generator](https://github.com/baidu/uid-generator/blob/master/README.zh_cn.md)

![avatar](/static/image/common/uuid-baidu.png)

1. uid-generator 使用的就是 snowflake, 只是在生产机器 id, 也叫做 workId 时有所不同
2. 结构

    - 64 bit 的正整数: long 类型
    - 1bit: 固定为 0 表示生成的 ID 都为正数
    - 28bit: 存储的是时间秒数的差值`当前时间-固定的开始时间`: 时间基点"2016-05-20"的增量值[8.7]
    - 22bit: workId, `同一个应用每重启一次就会消费一个workId`
    - 13bit: 序列号, 一毫秒最多生成 2^13=8192个. 默认是 9192^3 个

3. workId 的生成

    - 默认提供的策略: 应用启动时由数据库分配
    - 应用在启动时会往数据库表(uid-generator 需要新增一个 WORKER_NODE 表)中去插入一条数据, 数据插入成功后返回的该数据对应的自增唯一 id 就是该机器的 workId, 而数据由 host, port
      组成

4. 简介

    - 最终单机 QPS 可达 600 万
    - 适用于 docker 等虚拟化环境下实例自动重启、漂移等场景
    - UidGenerator 借用`未来时间`来解决 sequence 天然存在的并发限制:
        1. 采用 RingBuffer 来缓存已生成的 UID, 并行化 UID 的生产和消费
        2. 同时对 CacheLine 补齐, 避免了由 RingBuffer 带来的硬件级「伪共享」问题

5. 生成 UID 的过程: `Uid-RingBuffer[存Uid] + Flag-RingBuffer[存Uid状态]`

    - 根据配置初始化 RingBuffer`环形数组` 容量: 默认为 Snowflake 算法中 sequence 最大值[且为 2^N]
    - 并在 RingBuffer of UID 上填充 uid: RingBuffer 是一个只读的环+批量写的环
    - 且 RingBuffer of Flag 上填充 uid 对应的是否被消费的状态: RingBuffer 是一个读写的环
    - 当 Flag 环的使用率达到一定值就会在 UID 的环上从尾 Tail 开始填充环, 并将 Flag 对应的位置改为可用
    - 获取 uid 时是读取 flag 的环: 头 cursor 开始并下移[顺时针]一位
    - core code

      ```java
      // uuid + 多线程填充[次数不多, 所以没有解决伪共享问题]
      private final long[] slots;

   // uuid flag and FalseSharing // The CPU cache line commonly be 64 bytes, here is a sample of cache line after
   padding:
   // 64 bytes = 8 bytes (object reference) + 6 * 8 bytes (padded long) + 8 bytes (a long value)
      ```

6. RingBuffer 填充时机

    - 初始化预填充: **RingBuffer 初始化时, 预先填充满整个 RingBuffer**
    - 即时填充
        1. Take 消费时, 即时检查剩余可用 slot 量(tail - cursor)
        2. 如小于设定阈值, 则补全空闲 slots
        3. 阈值可通过 paddingFactor 来进行配置
    - 周期填充
        1. 通过 Schedule 线程, 定时补全空闲 slots
        2. 可通过 scheduleInterval 配置, 以应用定时填充功能, 并指定 Schedule 时间间隔

7. 环满了的拒绝策略 || 环空的拒绝策略
8. RingBuffer 的特点

    - 数组元素在内存中是连续分配的, 可最大程度利用 CPU cache 以提升性能
    - **可以使用缓存行 L1/2/3, 缓存行大小一般是 64 个字节, slot 是 8 个字节[long], 所以一次能读 8 个 uuid 到缓存中, 读 uuid 的速度直接走缓存**
    - 但缓存行**同时会带来「伪共享」 FalseSharing 问题**:

        1. 写的时候会互相竞争数据的写权限, 导致变慢[伪共享{只有写操作}]
        2. **cpu1 和 cpu2 竞争胜利的去写, 另一个在缓存中的数据就作废了**
        3. 数据每个线程都需要写[一个线程写前 8 个字节, 第二个线程写后 8 个字节], 两个线程竞争产生`伪共享`

    - 伪共享解决: 为此在 Tail、Cursor 指针、Flag-RingBuffer 中采用了 CacheLine[64] 补齐方式[PaddedAtomicLong]
    - 并发解决是 tail 和 cursor 都是 atomicLong 解决的

#### [伪共享](https://www.cnblogs.com/tong-yuan/p/FalseSharing.html)

- 伪共享是双写操作, 且发生在不同核心

1. CPU 缓存行:

    - CPU 与优先与缓存交互: L1/L2/L3`[只有L3是所有core公用的]`
    - 一般是 64byte: `一个缓存行存储大小`
    - CPU 获取缓存中数据的流程是: cpu -> L1 -> L2 -> L3 -> 主内存
    - 缓存行带来好处
        1. 如果访问一个 long 类型的数组时, 当数组中的一个值被加载到缓存中时, 另外 7 个元素也会被加载到缓存中
        2. 但是, 如果使用的数据结构中的项在内存中不是彼此相邻的, 比如链表, 那么将得不到免费缓存加载带来的好处
    - 免费加载也有一个坏处: 就是伪共享
        1. 设想 a, b 两个数据在同一个缓存行内
        2. 线程 A 去修改 a, 会把 a, b 都加载到缓存行中;
        3. 线程 A 修改完会写回 L1, L2, L3, 并写回主内存 + 通知其他包含此缓存行的 cpu 核心失效
        4. 同核心的线程 B 读取 b, 在 L1 内直接命中: `不存在伪共享`
        5. **不同核心的线程 B 读取 b[即使之前已经加载到缓存中也会被 A 失效], 需要重新去 L1 -> L2 -> L3(找到) 获取**

2. `AtomicLong + 伪共享在 uid 中的作用`
    - AtomicLong: 判断出每个 CPU 获取的数组下标 && 且每个元素都占有一个缓存行
    - cpu core1 需要缓存行 1
    - cpu core2 需要缓存行 2
    - core1 去修改 1 将其变为已获取, 之后会通知其他包含此值的缓存行都失效
    - core3 去修改 3 将其变为已获取, 之后会通知其他包含此值的缓存行都失效[在此过程中缓存行 1 也有可能被失效]

3. 结论

    - CPU 具有多级缓存，越接近 CPU 的缓存越小也越快
    - CPU 缓存中的数据是以缓存行为单位处理的
    - CPU 缓存行能带来免费加载数据的好处, 所以处理数组性能非常高
    - CPU 缓存行也带来了弊端, 多线程处理不相干的变量时会相互影响，也就是伪共享
    - 避免伪共享的主要思路就是让不相干的变量不要出现在同一个缓存行中
    - 一是每两个变量之间加七个 long 类型
    - 二是创建自己的 long 类型，而不是用原生的`[LongAdder]`

      ```java
      // -XX:-RestrictContended 才会使得 Contended 生效
      @sun.misc.Contended
      class MyLong {
         volatile long value;
      }
      ```

    - 三是使用 java8 提供的注解

4. jdk 中的应用

    - ConcurrentHashMap: size() 方法使用的是分段的思想来构造的, 每个段使用的类是 CounterCell, 它的类上就有 `@sun.misc.Contended` 注解
    - LongAdder