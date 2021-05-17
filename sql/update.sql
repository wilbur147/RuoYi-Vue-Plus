-- ----------------------------
-- Table structure for file_uploader   2021.4.2 新增
-- ----------------------------
DROP TABLE IF EXISTS `file_uploader`;
CREATE TABLE `file_uploader`  (
  `file_id` int(11) NOT NULL AUTO_INCREMENT,
  `unique_id` varchar(30) NULL DEFAULT NULL COMMENT '文件唯一码',
  `storage_type` char(10) NULL DEFAULT NULL COMMENT '存储类型 0 本地  1 七牛云 2 阿里云OSS',
  `upload_type` varchar(50) NULL DEFAULT NULL COMMENT '上传类型',
  `original_file_name` varchar(255) NULL DEFAULT NULL COMMENT '文件名称',
  `file_size` bigint(20) UNSIGNED NULL DEFAULT NULL COMMENT '文件大小',
  `suffix` varchar(20) NULL DEFAULT NULL COMMENT '文件后缀',
  `file_path` varchar(255) NULL DEFAULT NULL COMMENT '相对路径',
  `full_file_path` varchar(255) NULL DEFAULT NULL COMMENT '全路径',
  `file_hash` varchar(255) NULL DEFAULT NULL,
  `upload_start_time` datetime(0) NULL DEFAULT NULL,
  `upload_end_time` datetime(0) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`file_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 COMMENT = '基础文件信息' ;

-- ----------------------------
-- Table structure for material_group  2021.4.9 新增
-- ----------------------------
DROP TABLE IF EXISTS `material_group`;
CREATE TABLE `material_group`  (
  `material_group_id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '分组主键id',
  `group_name` varchar(50) NOT NULL COMMENT '内容分组名称',
  `parent_id` int(11) NOT NULL COMMENT '父组别id(系统默认的组别id为0)',
  `is_void` char(2)  NOT NULL DEFAULT '1' COMMENT '是否有效(Y有效 N无效)',
  `create_by` varchar(64)  NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64)  NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500)  NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`material_group_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 COMMENT = '素材分组' ;

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('素材分组', '1061', '1', 'group', 'material/group/index', 1, 0, 'C', '0', '0', 'material:group:list', '#', 'admin', sysdate(), '', null, '素材分组菜单');
-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();
-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('素材分组查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'material:group:query',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('素材分组新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'material:group:add',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('素材分组修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'material:group:edit',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('素材分组删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'material:group:remove',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('素材分组导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'material:group:export',       '#', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- Table structure for material_resources 2021.4.9 新增
-- ----------------------------
DROP TABLE IF EXISTS `material_resources`;
CREATE TABLE `material_resources`  (
  `material_resources_id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '素材资源主键id',
  `material_group_id` int(11) UNSIGNED NOT NULL COMMENT '分组id',
  `file_unique_id` varchar(500) NOT NULL COMMENT '资源地址',
  `resource_name` varchar(255) NULL DEFAULT NULL COMMENT '资源名称',
  `resource_type` varchar(20) NOT NULL COMMENT '资源类型(GRAPHIC--图文,IMG--图片,VIDEO--视频,AUDIO--音频)',
  `summary` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '摘要',
  `is_void` char(2)  NOT NULL DEFAULT 'Y' COMMENT '是否有效(Y有效 N无效)',
  `create_by` varchar(64)  NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64)  NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500)  NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`material_resources_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 COMMENT = '素材资源' ;

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('素材资源', '1061', '1', 'resources', 'material/resources/index', 1, 0, 'C', '0', '0', 'material:resources:list', '#', 'admin', sysdate(), '', null, '素材资源菜单');
-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();
-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('素材资源查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'material:resources:query',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('素材资源新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'material:resources:add',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('素材资源修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'material:resources:edit',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('素材资源删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'material:resources:remove',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('素材资源导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'material:resources:export',       '#', 'admin', sysdate(), '', null, '');


-- ----------------------------
-- Table structure for ry_blog  2021.4.23 新增
-- ----------------------------
DROP TABLE IF EXISTS `ry_blog`;
CREATE TABLE `ry_blog`  (
  `blog_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NULL DEFAULT NULL COMMENT '博客标题',
  `summary` varchar(200) NULL DEFAULT NULL COMMENT '博客简介',
  `content` longtext NULL COMMENT '博客内容',
  `blog_tag_id` varchar(255) NULL DEFAULT NULL COMMENT '博客标签id',
  `blog_sort_id` varchar(32) NULL DEFAULT NULL COMMENT '博客分类id',
  `click_count` int(11) UNSIGNED NULL DEFAULT 0 COMMENT '博客点击数',
  `file_unique_id` varchar(255) NULL DEFAULT NULL COMMENT '封面图uniqueid',
  `status` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '状态 0 正常  1 关闭',
  `author` varchar(255) NULL DEFAULT NULL COMMENT '作者',
  `articles_part` varchar(255) NULL DEFAULT NULL COMMENT '文章出处',
  `is_original` varchar(1) NULL DEFAULT 'Y' COMMENT '是否原创：Y：是，N：否',
  `is_publish` varchar(1) NULL DEFAULT 'Y' COMMENT '是否发布：Y：是，N：否',
  `sort` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序字段',
  `open_comment` varchar(1) NOT NULL DEFAULT 'Y' COMMENT '是否开启评论(Y：是，N：否)',
  `type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '类型【0 博客， 1：推广】',
  `editor_type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '编辑器类型【0 富文本编辑器， 1：Markdown编辑器】',
  `outside_link` varchar(1024) NULL DEFAULT NULL COMMENT '外链【如果是推广，那么将跳转到外链】',
  `create_by` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`blog_id`) USING BTREE,
  INDEX `idx_sort_id`(`blog_sort_id`) USING BTREE,
  INDEX `idx_tag_id`(`blog_tag_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 COMMENT = '博客表' ;

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('博客文章', '1062', '1', 'blog', 'blog/blog/index', 1, 0, 'C', '0', '0', 'blog:blog:list', '#', 'admin', sysdate(), '', null, '博客文章菜单');
-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();
-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('博客文章查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'blog:blog:query',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('博客文章新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'blog:blog:add',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('博客文章修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'blog:blog:edit',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('博客文章删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'blog:blog:remove',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('博客文章导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'blog:blog:export',       '#', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- Table structure for ry_blog_sort  2021.4.23 新增
-- ----------------------------
DROP TABLE IF EXISTS `ry_blog_sort`;
CREATE TABLE `ry_blog_sort`  (
  `blog_sort_id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `sort_name` varchar(100) NOT NULL COMMENT '分类名称',
  `status` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '状态 0 正常 1 停用',
  `remark` varchar(500) NULL DEFAULT NULL COMMENT '分类简介',
  `sort` int(11) NULL DEFAULT 0 COMMENT '排序字段',
  `create_by` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`blog_sort_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 COMMENT = '博客分类' ;

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('博客分类', '1062', '1', 'sort', 'blog/sort/index', 1, 0, 'C', '0', '0', 'blog:sort:list', '#', 'admin', sysdate(), '', null, '博客分类菜单');
-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();
-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('博客分类查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'blog:sort:query',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('博客分类新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'blog:sort:add',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('博客分类修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'blog:sort:edit',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('博客分类删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'blog:sort:remove',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('博客分类导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'blog:sort:export',       '#', 'admin', sysdate(), '', null, '');

-- ----------------------------
-- Table structure for ry_blog_tag   2021.4.23 新增
-- ----------------------------
DROP TABLE IF EXISTS `ry_blog_tag`;
CREATE TABLE `ry_blog_tag`  (
  `blog_tag_id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(100) NOT NULL COMMENT '标签名称',
  `status` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '状态 0 正常 1 停用',
  `remark` varchar(500) NULL DEFAULT NULL COMMENT '标签简介',
  `sort` int(11) NULL DEFAULT 0 COMMENT '排序字段',
  `create_by` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`blog_tag_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 COMMENT = '博客标签' ;

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('博客标签', '1062', '1', 'tag', 'blog/tag/index', 1, 0, 'C', '0', '0', 'blog:tag:list', '#', 'admin', sysdate(), '', null, '博客标签菜单');
-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();
-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('博客标签查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'blog:tag:query',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('博客标签新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'blog:tag:add',          '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('博客标签修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'blog:tag:edit',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('博客标签删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'blog:tag:remove',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('博客标签导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'blog:tag:export',       '#', 'admin', sysdate(), '', null, '');


-- ----------------------------
-- Records of sys_dict_type   2021.4.25
-- ----------------------------
INSERT INTO `sys_dict_type` (dict_name,dict_type,status,create_by,create_time,update_by,update_time,remark) VALUES
('存储区域', 'sys_storage_region', '0', 'admin', '2020-12-29 15:37:02', 'admin', '2021-03-16 14:56:50', '七牛云存储区域'),
('文件显示优先级', 'sys_file_priority', '0', 'admin', '2020-12-29 15:42:14', 'admin', '2021-03-16 14:56:46', '七牛云、本地、阿里云OSS显示优先级'),
('资源类型', 'resource_type_dict', '0', 'admin', '2021-03-24 10:45:02', '', NULL, '资源类型字典'),
('文章类型', 'article_type', '0', 'admin', '2021-04-13 11:13:51', '', NULL, '文章类型（博客，推广）'),
('是否原创', 'original_status', '0', 'admin', '2021-04-15 10:50:44', 'admin', '2021-04-15 10:53:49', '是否原创： 原创   转载'),
('发布状态', 'publish_status', '0', 'admin', '2021-04-15 10:52:59', '', NULL, '发布状态： 发布 下架'),
('评论状态', 'open_comment_status', '0', 'admin', '2021-04-15 10:55:46', '', NULL, '评论状态： 开起  关闭'),
('编辑器类型', 'editor_type', '0', 'admin', '2021-04-23 15:33:02', '', NULL, '编辑器类型 （富文本，Markdown）');

-- ----------------------------
-- Records of sys_dict_data   2021.4.25
-- ----------------------------
INSERT INTO `sys_dict_data` (dict_sort,dict_label,dict_value,dict_type,css_class,list_class,is_default,status,create_by,create_time,update_by,update_time,remark) VALUES
(0, '东南亚', 'as0', 'sys_storage_region', NULL, NULL, 'N', '0', 'admin', '2020-12-29 15:37:42', 'admin', '2021-03-16 14:56:57', '存储区域 东南亚'),
(1, '北美', 'na0', 'sys_storage_region', NULL, NULL, 'N', '0', 'admin', '2020-12-29 15:39:57', 'admin', '2021-03-16 14:56:59', '存储区域 北美'),
(2, '华南', 'z2', 'sys_storage_region', NULL, NULL, 'N', '0', 'admin', '2020-12-29 15:40:16', 'admin', '2021-03-16 14:57:01', '存储区域 华南'),
(3, '华北', 'z1', 'sys_storage_region', NULL, NULL, 'N', '0', 'admin', '2020-12-29 15:40:34', 'admin', '2021-03-16 14:57:03', '存储区域 华北'),
(4, '华东', 'z0', 'sys_storage_region', NULL, NULL, 'N', '0', 'admin', '2020-12-29 15:40:59', 'admin', '2021-03-16 14:57:06', '存储区域 华东'),
(0, '本地文件存储', '0', 'sys_file_priority', NULL, NULL, 'N', '0', 'admin', '2020-12-29 15:43:05', 'admin', '2021-03-16 14:56:17', '文件显示优先级 本地'),
(1, '七牛云对象存储', '1', 'sys_file_priority', NULL, NULL, 'N', '0', 'admin', '2020-12-29 15:43:24', 'admin', '2021-03-16 14:56:21', '文件显示优先级 七牛云'),
(2, '阿里云OSS存储', '2', 'sys_file_priority', NULL, NULL, 'N', '0', 'admin', '2020-12-29 15:43:38', 'admin', '2021-03-16 14:56:27', '文件显示优先级 阿里云OSS'),
(0, '图文列表', 'GRAPHIC', 'resource_type_dict', NULL, NULL, 'N', '0', 'admin', '2021-03-24 10:45:44', 'admin', '2021-03-26 16:17:24', NULL),
(1, '图片列表', 'IMG', 'resource_type_dict', NULL, NULL, 'N', '0', 'admin', '2021-03-24 10:46:12', 'admin', '2021-03-26 16:17:28', NULL),
(2, '视频列表', 'VIDEO', 'resource_type_dict', NULL, NULL, 'N', '0', 'admin', '2021-03-24 10:46:26', 'admin', '2021-03-26 16:17:32', NULL),
(3, '音频列表', 'AUDIO', 'resource_type_dict', NULL, NULL, 'N', '0', 'admin', '2021-03-24 10:46:41', 'admin', '2021-03-26 16:17:35', NULL),
(0, '博客', '0', 'article_type', NULL, NULL, 'N', '0', 'admin', '2021-04-13 11:14:24', '', NULL, NULL),
(1, '推广', '1', 'article_type', NULL, NULL, 'N', '0', 'admin', '2021-04-13 11:14:33', '', NULL, NULL),
(0, '原创', 'Y', 'original_status', NULL, NULL, 'N', '0', 'admin', '2021-04-15 10:51:27', 'admin', '2021-04-15 10:54:00', NULL),
(1, '转载', 'N', 'original_status', NULL, NULL, 'N', '0', 'admin', '2021-04-15 10:51:35', 'admin', '2021-04-15 10:54:10', NULL),
(0, '发布', 'Y', 'publish_status', NULL, NULL, 'N', '0', 'admin', '2021-04-15 10:54:32', '', NULL, NULL),
(1, '下架', 'N', 'publish_status', NULL, NULL, 'N', '0', 'admin', '2021-04-15 10:54:41', '', NULL, NULL),
(0, '开起', 'Y', 'open_comment_status', NULL, NULL, 'N', '0', 'admin', '2021-04-15 10:56:15', 'admin', '2021-04-15 10:56:55', NULL),
(1, '关闭', 'N', 'open_comment_status', NULL, NULL, 'N', '0', 'admin', '2021-04-15 10:56:25', 'admin', '2021-04-15 10:56:59', NULL),
(0, '富文本', '0', 'editor_type', NULL, NULL, 'N', '0', 'admin', '2021-04-23 15:33:18', 'admin', '2021-04-23 15:41:22', NULL),
(1, 'Markdown', '1', 'editor_type', NULL, NULL, 'N', '0', 'admin', '2021-04-23 15:33:27', 'admin', '2021-04-23 15:41:29', NULL);

-- ----------------------------
-- Records of sys_config   2021.4.25
-- ----------------------------
INSERT INTO `sys_config` (config_name,config_key,config_value,config_type,create_by,create_time,update_by,update_time,remark) VALUES
('文件优先选择显示', 'sys.config.filePriorityShow', '0', 'N', 'admin', '2021-03-15 17:38:20', 'admin', '2021-04-19 16:42:28', NULL),
('是否文件上传本地', 'sys.config.uploadLocal', 'Y', 'N', 'admin', '2021-03-15 17:54:21', 'admin', '2021-04-19 16:42:28', NULL);


