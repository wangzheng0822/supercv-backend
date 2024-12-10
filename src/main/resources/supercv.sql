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
    `css_name` varchar(64) NOT NULL COMMENT 'CSS名称',
    `demo_resume_id` bigint COMMENT '简历ID',
    `is_deleted` boolean DEFAULT FALSE COMMENT '是否已删除',
    `create_time` datetime NOT NULL DEFAULT current_timestamp,
    `update_time` datetime NOT NULL DEFAULT current_timestamp on update current_timestamp,
    primary key (`id`),
    unique (`css_name`)
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