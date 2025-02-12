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
create table if not exists `admin` (
    `uid` bigint NOT NULL,
    primary key (`uid`)
);

-- 产品信息
create table if not exists `product` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '产品ID',
    `name` varchar(64) NOT NULL COMMENT '产品名称',
    `original_price` decimal(10, 2) NOT NULL COMMENT '原价',
    `discount_price` decimal(10, 2) NOT NULL COMMENT '折扣价',
    `duration_days` int NOT NULL DEFAULT 0 COMMENT '会员时长(天)',
    `ai_analysis_num` int DEFAULT 0 COMMENT '智能评分调用次数',
    `ai_optimization_num` int DEFAULT 0 COMMENT '智能优化调用次数',
    `sort_value` int NOT NULL COMMENT '排序值',
    `is_deleted` boolean DEFAULT FALSE COMMENT '是否删除 false为否 true为是',
    `create_time` datetime NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT current_timestamp on update current_timestamp COMMENT '更新时间',
    primary key(`id`)
);

-- 会员权益
create table if not exists `vip` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `uid` bigint NOT NULL,
    `expire_time` datetime NOT NULL COMMENT '过期时间',
    `ai_analysis_left_num` int NOT NULL COMMENT 'AI分析剩余次数',
    `ai_optimization_left_num` int NOT NULL COMMENT 'AI优化剩余次数',
    `create_time` datetime NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT current_timestamp on update current_timestamp COMMENT '更新时间',
    primary key (`id`),
    unique (`uid`)
);

-- 简历模板
create table if not exists `resume_template` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '模板ID',
    `name` varchar(128) NOT NULL COMMENT '模板名称',
    `page_frame` varchar(64) NOT NULL DEFAULT 'default' COMMENT 'vue页面结构',
    `page_style` varchar(64) NOT NULL COMMENT 'css样式',
    `demo_resume_id` bigint COMMENT '简历ID',
    `is_deleted` boolean DEFAULT FALSE COMMENT '是否已删除',
    `create_time` datetime NOT NULL DEFAULT current_timestamp,
    `update_time` datetime NOT NULL DEFAULT current_timestamp on update current_timestamp,
    primary key (`id`)
);

-- 简历
create table if not exists `resume` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '简历ID',
    `uid` bigint NOT NULL COMMENT '简历归属用户ID',
    `name` varchar(128) NOT NULL COMMENT '简历名称',
    `template_id` bigint NOT NULL COMMENT '模板ID',
    `original_resume_url` varchar(256) COMMENT '简历文件url(pdf/word等)',
    `thumbnail_url` varchar(256) COMMENT '简历缩略图url',
    `page_margin_horizontal` int DEFAULT 30 COMMENT '左右页边距',
    `page_margin_vertical` int DEFAULT 30 COMMENT '上下页边距',
    `module_margin` int DEFAULT 20 COMMENT '模块间距',
    `theme_color` varchar(16) DEFAULT '#000000' COMMENT '主题色',
    `font_size` int DEFAULT 15 COMMENT '字体大小',
    `font_family` varchar(64) DEFAULT 'Microsoft YaHei' COMMENT '字体',
    `line_height` int DEFAULT 24 COMMENT '行高',
    `is_template_demo` boolean DEFAULT FALSE COMMENT '是否是模板示例简历',
    `is_deleted` boolean DEFAULT FALSE COMMENT '是否已删除',
    `create_time` datetime NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT current_timestamp on update current_timestamp COMMENT '更新时间',
    primary key (`id`)
);

-- 简历-基本信息
create table if not exists `resume_baseinfo` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '基本信息ID',
    `resume_id` bigint NOT NULL COMMENT '简历ID',
    `head_img_url` varchar(256) COMMENT '头像',
    `is_head_img_enabled` boolean DEFAULT TRUE COMMENT '是否显示头像',
    `head_img_layout` tinyint DEFAULT 2 COMMENT '头像布局，默认居右，取值居左、居右、居上',
    `item_layout` tinyint DEFAULT 3 COMMENT '条目布局，默认居中，取值左对齐，右对齐，居中对齐',
    `is_enabled` boolean DEFAULT TRUE COMMENT '是否启用',
    `create_time` datetime NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT current_timestamp on update current_timestamp COMMENT '更新时间',
    primary key (`id`),
    unique (`resume_id`)
);

-- 简历-基本信息-条目
create table if not exists `resume_baseinfo_item` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '条目ID',
    `baseinfo_id` bigint NOT NULL COMMENT '基本信息ID',
    `key` varchar(32) NOT NULL COMMENT '条目key',
    `value` varchar(128) NOT NULL COMMENT '条目value',
    `sort_value` int NOT NULL COMMENT '排序值',
    `create_time` datetime NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT current_timestamp on update current_timestamp COMMENT '更新时间',
    primary key (`id`)
);

-- 简历-模块，比如教育经历、工作经历、项目经历、专业技能等
create table if not exists `resume_module` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '模块ID',
    `resume_id` bigint NOT NULL COMMENT '简历ID',
    `title` varchar(128) NOT NULL COMMENT '模块标题',
    `sort_value` int NOT NULL COMMENT '排序值',
    `is_default` boolean DEFAULT FALSE COMMENT '是否是默认模块，默认模块不可删除',
    `is_enabled` boolean DEFAULT TRUE COMMENT '是否启用',
    `create_time` datetime NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT current_timestamp on update current_timestamp COMMENT '更新时间',
    primary key (`id`)
);

-- 简历-模块-条目，比如教育经历有好几条
create table if not exists `resume_module_item` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '子模块ID',
    `resume_id` bigint NOT NULL COMMENT '简历ID',
    `module_id` bigint NOT NULL COMMENT '模块ID',
    `title_major` varchar(128) NOT NULL COMMENT '主标题',
    `title_minor` varchar(128) COMMENT '次标题',
    `title_date` varchar(128) COMMENT '时间',
    `title_sort_type` tinyint DEFAULT 1 COMMENT 'title各个部分如何排序',
    `is_title_major_enabled` boolean DEFAULT TRUE COMMENT '是否显示主标题',
    `is_title_minor_enabled` boolean DEFAULT TRUE COMMENT '是否显示次标题',
    `is_title_date_enabled` boolean DEFAULT TRUE COMMENT '是否显示日期',
    `content` text COMMENT '内容',
    `sort_value` int NOT NULL COMMENT '排序值',
    `create_time` datetime NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT current_timestamp on update current_timestamp COMMENT '更新时间',
    primary key (`id`)
);

-- 订单
CREATE TABLE if not exists `cv_order` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `order_no` varchar(32) NOT NULL COMMENT '订单号',
    `uid` bigint NOT NULL COMMENT '用户ID',
    `product_id` bigint NOT NULL COMMENT '产品ID',
    `order_time` datetime NOT NULL DEFAULT current_timestamp COMMENT '下单时间',
    `payment_amount` decimal(10, 2) NOT NULL COMMENT '支付金额',
    `payment_no_3rd` varchar(64) COMMENT '三方支付订单号（微信支付），用于对账',
    `payment_channel_type` tinyint COMMENT '支付渠道：0_微信支付、1_支付宝',
    `payment_channel_id` bigint COMMENT '支付渠道ID',
    `payment_status` tinyint DEFAULT '1' COMMENT '支付状态：1_待支付、2_已支付、3_支付失败、4_过期、5_退款',
    `payment_time` datetime DEFAULT NULL COMMENT '支付时间',
    `payment_url` varchar(2048) COMMENT '支付二维码链接',
    `grant_status` tinyint DEFAULT '1' COMMENT '权益授予状态：1_未开通，2_已开通，3_失败',
    `grant_time` datetime DEFAULT NULL COMMENT '权益开通时间',
    `user_comment` varchar(255) DEFAULT NULL COMMENT '用户备注',
    `admin_comment` varchar(255) DEFAULT NULL COMMENT '管理员备注',
    `is_deleted` boolean NOT NULL DEFAULT false COMMENT '逻辑删除',
    `create_time` datetime NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT current_timestamp on update current_timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`),
    unique (`order_no`),
    unique (`payment_no_3rd`)
);

-- 支付渠道
CREATE TABLE IF NOT EXISTS `payment_channel` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '支付渠道ID',
    `type` tinyint NOT NULL COMMENT '支付渠道类型: 1_微信支付、2_支付宝',
    `name` varchar(256) COMMENT '支付渠道名称',
    `app_id` varchar(256) COMMENT '支付渠道应用ID',
    `mch_id` varchar(64) COMMENT '支付渠道商户号',
    `secret` varchar(64) COMMENT '支付渠道密钥',
    `api_url` varchar(256) COMMENT '支付渠道API地址',
    `callback_url` varchar(256) COMMENT '支付渠道回调地址',
    `return_url` varchar(256) COMMENT '支付渠道支付成功后跳转地址',
    `enabled` boolean default false,
    primary key (`id`)
);

-- 文章
create table if not exists `article` (
    `id` bigint not null auto_increment comment '文章ID',
    `uid` bigint not null comment '用户ID',
    `cate_type` tinyint not null comment '文章分类类型：1_案例参考、2_专家服务',
    `title` varchar(256) not null comment '文章标题',
    `sub_title` varchar(256) comment '文章副标题',
    `snippet` varchar(1024) comment '文章摘要',
    `cover_img` varchar(1024) comment '文章封面图',
    `content_id` bigint comment '文章正文ID',
    `is_free` boolean default false comment '是否免费',
    `create_time` datetime not null default current_timestamp,
    `update_time` datetime not null default current_timestamp on update current_timestamp,
    primary key (`id`)
);

-- 文章正文
create table if not exists `article_content` (
    `id` bigint not null auto_increment comment '文章正文ID',
    `content` mediumtext not null comment '正文内容',
    `create_time` datetime not null default current_timestamp,
    `update_time` datetime not null default current_timestamp on update current_timestamp,
    primary key (`id`)
);

-- oss token
CREATE TABLE IF NOT EXISTS `oss_sts`(
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
    `uid` bigint NOT NULL,
    `access_key_id` varchar(128),
    `access_key_secret` varchar(128),
    `security_token` varchar(5120),
    `expiration` bigint,
    primary key (`id`),
    unique (`uid`)
);
