DROP TABLE IF EXISTS WORKER_NODE;
CREATE TABLE `worker_node`
(
    `ID`          bigint(20) NOT NULL AUTO_INCREMENT,
    `HOST_NAME`   varchar(64) COLLATE utf8mb4_bin NOT NULL,
    `PORT`        varchar(64) COLLATE utf8mb4_bin NOT NULL,
    `TYPE`        int(11) NOT NULL,
    `LAUNCH_DATE` date                            NOT NULL,
    `MODIFIED`    datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3),
    `CREATED`     datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP (3) ON UPDATE CURRENT_TIMESTAMP (3),
    PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
