DELIMITER $$
DROP PROCEDURE IF EXISTS MODIFY_COLUMN $$
CREATE PROCEDURE MODIFY_COLUMN()
BEGIN

create table if not exists app_live
(
    id              bigint auto_increment primary key ,
    name            varchar(128) not null,
    description     varchar(512) null,
    start_time      datetime(3)  not null,
    end_time        datetime(3)  not null,
    is_publish      tinyint(1)   not null default 0,
    channel         varchar(64)  not null,
    reserve_days    int(8)       not null default 90,
    is_top          tinyint(1)   not null default 0,
    top_sort        int(8)       default 0,
    cover_img       varchar(512) not null,
    live_link       varchar(512) not null,

    `is_deleted`    tinyint(1)            DEFAULT '0' COMMENT '是否已删除。 0=记录未删除，1=记录已删除',
    `source_id` varchar(100) DEFAULT ''          COMMENT '数据来源',
    `inserted_by`   bigint(20)   NULL,
    `updated_by`    bigint(20)   NULL,
    `inserted_time` datetime(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `updated_time`  datetime(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3)
)
    comment '应用-直播' charset = utf8mb4;

  IF NOT EXISTS ( SELECT COLUMN_NAME FROM information_schema.columns WHERE TABLE_SCHEMA='vc-fb' AND TABLE_NAME ='account_member' AND COLUMN_NAME='education')
    THEN ALTER TABLE account_member ADD education TINYINT(4) NULL COMMENT '学历' AFTER last_sync_date;
  END IF;

END $$
DELIMITER ;
CALL MODIFY_COLUMN;
DROP PROCEDURE MODIFY_COLUMN;
