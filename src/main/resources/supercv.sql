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

-- 订单
CREATE TABLE if not exists `order` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `order_no` varchar(32) NOT NULL COMMENT '订单号',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `product_id` bigint NOT NULL COMMENT '产品ID',
    `order_time` datetime NOT NULL DEFAULT current_timestamp COMMENT '下单时间',
    `payment_no_3rd` varchar(64) COMMENT '三方支付订单号（微信支付），用于对账',
    `payment_channel_type` tinyint COMMENT '支付渠道：0_微信支付、1_支付宝',
    `payment_channel_id` bigint COMMENT '支付渠道ID',
    `payment_status` tinyint NOT NULL DEFAULT '0' COMMENT '支付状态：0_待支付、1_已支付、2_过期、3_支付失败',
    `payment_time` datetime DEFAULT NULL COMMENT '支付时间',
    `grant_status` tinyint NOT NULL DEFAULT '0' COMMENT '权益授予状态：0_未开通，1_已开通，2_失败',
    `grant_time` datetime DEFAULT NULL COMMENT '权益开通时间',
    `user_comment` varchar(255) DEFAULT NULL COMMENT '用户备注',
    `admin_comment` varchar(255) DEFAULT NULL COMMENT '管理员备注',
    `is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除，0_未删除，1_删除',
    `create_time` datetime NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT current_timestamp on update current_timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`),
    unique (`order_no`),
    unique (`payment_no_3rd`)
);
