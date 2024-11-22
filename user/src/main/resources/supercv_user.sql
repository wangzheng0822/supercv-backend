USE `supercv_db_test`;

drop table if exists `ok_test`;
CREATE TABLE IF NOT EXISTS `ok_test` (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT,
    `name` varchar(20),
    `create_time` datetime NOT NULL DEFAULT current_timestamp,
    `update_time` datetime NOT NULL DEFAULT current_timestamp on update current_timestamp,
    primary key (`id`)
);