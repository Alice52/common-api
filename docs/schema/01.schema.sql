-- common uid: 不存在则自动建库建表
drop table if exists `uid_worker_node`;
create table `uid_worker_node` (
       `id` bigint(20) not null auto_increment,
       `host_name` varchar(64) collate utf8mb4_bin not null,
       `port` varchar(64) collate utf8mb4_bin not null,
       `type` int(11) not null,
       `launch_date` date not null,
       `modified` datetime(3) not null default current_timestamp(3),
       `created` datetime(3) not null default current_timestamp(3) on update current_timestamp(3),
       primary key (`id`)
) engine = innodb default charset = utf8mb4 collate = utf8mb4_bin;
