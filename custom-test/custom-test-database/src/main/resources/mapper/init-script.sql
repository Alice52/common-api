CREATE TABLE IF NOT EXISTS `boot_cache_all_star_phase`
(
    `id` BIGINT
(
    20
) NOT NULL AUTO_INCREMENT,
    `phase_code` VARCHAR
(
    255
) NOT NULL COMMENT '阶段编码',
    `phase_name` VARCHAR
(
    255
) NOT NULL COMMENT '阶段名称',
    `start_time` datetime
(
    3
) NULL COMMENT '阶段开始时间',
    `end_time` datetime
(
    3
) NULL COMMENT '阶段结束时间',
    `type` VARCHAR
(
    255
) NULL COMMENT '阶段类型',
    `status` VARCHAR
(
    255
) NULL COMMENT '阶段状态',
    `is_deleted` tinyint
(
    1
) DEFAULT '0' COMMENT '是否已删除。删除后不可撤回。 0=记录未删除，1=记录已删除',
    `inserted_by` BIGINT
(
    20
) NULL,
    `updated_by` BIGINT
(
    20
) NULL,
    `inserted_time` datetime
(
    3
) NOT NULL DEFAULT CURRENT_TIMESTAMP
(
    3
),
    `updated_time` datetime
(
    3
) NOT NULL DEFAULT CURRENT_TIMESTAMP
(
    3
) ON UPDATE CURRENT_TIMESTAMP
(
    3
),
    PRIMARY KEY
(
    `id`
)
    ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '阶段表';


TRUNCATE TABLE `boot_cache_all_star_phase`;

INSERT INTO `boot_cache_all_star_phase`(`id`,
                                        `phase_code`,
                                        `phase_name`,
                                        `start_time`,
                                        `end_time`,
                                        `type`,
                                        `status`,
                                        `is_deleted`,
                                        `inserted_by`,
                                        `updated_by`,
                                        `inserted_time`,
                                        `updated_time`)
VALUES (1,
        'WARM_UP',
        '预热',
        '2021-04-10 00:00:00.000',
        '2021-04-20 00:00:00.000',
        NULL,
        NULL,
        0,
        NULL,
        NULL,
        '2021-04-06 11:54:06.382',
        '2021-04-07 10:14:23.844'),
       (2,
        'START_UP',
        '启动',
        '2021-04-20 00:00:00.000',
        '2021-04-30 00:00:00.000',
        NULL,
        NULL,
        0,
        NULL,
        NULL,
        '2021-04-06 11:54:06.516',
        '2021-04-07 10:14:45.812'),
       (3,
        'RESTAURANT_COMPETITION',
        '餐厅比拼',
        '2021-04-30 00:00:00.000',
        '2021-05-10 00:00:00.000',
        NULL,
        NULL,
        0,
        NULL,
        NULL,
        '2021-04-06 11:54:06.517',
        '2021-04-07 10:15:02.068'),
       (4,
        'ONLINE_PK',
        '线上pk',
        '2021-05-10 00:00:00.000',
        '2021-05-20 00:00:00.000',
        NULL,
        NULL,
        0,
        NULL,
        NULL,
        '2021-04-06 11:54:06.517',
        '2021-04-07 10:15:37.430'),
       (5,
        'SUMMARY',
        '总结',
        '2021-05-20 00:00:00.000',
        '2021-05-30 00:00:00.000',
        NULL,
        NULL,
        0,
        NULL,
        NULL,
        '2021-04-06 18:11:58.241',
        '2021-04-07 10:16:01.563');
