-- 用户
create table if not exists `cv_user` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `telephone` varchar(20) COMMENT '手机号',
    `union_id` varchar(64) COMMENT '微信UnionId',
    `open_id` varchar(64) COMMENT '微信OpenId',
    `nick_name` varchar(32) COMMENT '昵称',
    `head_img_url` varchar(256) COMMENT '头像',
    `create_time` datetime NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT current_timestamp on update current_timestamp COMMENT '更新时间',
    primary key (`id`),
    unique (`telephone`),
    unique (`open_id`),
    unique (`union_id`)
);

-- 登陆token
create table if not exists `auth_token` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `token` char(32) NOT NULL COMMENT 'token',
    `uid` bigint NOT NULL COMMENT '用户ID',
    `expire_time` datetime NOT NULL COMMENT '过期时间',
    `create_time` datetime NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    primary key (`id`),
    unique (`token`)
);

-- 短信验证码
create table if not exists `sms_code` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `telephone` varchar(20) NOT NULL COMMENT '手机号',
    `code` varchar(10) NOT NULL COMMENT '验证码',
    `is_used` boolean DEFAULT FALSE COMMENT '是否已使用',
    `send_time` datetime NOT NULL DEFAULT current_timestamp COMMENT '发送时间',
    primary key (`id`),
    unique (`telephone`)
);

-- 管理员账号
CREATE TABLE IF NOT EXISTS `admin` (
    `uid` bigint NOT NULL,
    primary key (`uid`)
);
