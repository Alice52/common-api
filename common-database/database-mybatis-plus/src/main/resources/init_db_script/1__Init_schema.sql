CREATE DATABASE IF NOT EXISTS `vc-fb` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

CREATE TABLE IF NOT EXISTS `account_member` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `phone_number` varchar(30) DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '0'           COMMENT '是否已删除。 0=记录未删除，1=记录已删除',
  `inserted_by` bigint(20) NULL,
  `updated_by` bigint(20) NULL,
  `inserted_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `updated_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
