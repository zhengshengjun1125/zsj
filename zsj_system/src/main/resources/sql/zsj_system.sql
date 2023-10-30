/*
 Navicat Premium Data Transfer

 Source Server         : 8.0
 Source Server Type    : MySQL
 Source Server Version : 80100
 Source Host           : localhost:3307
 Source Schema         : zsj_system

 Target Server Type    : MySQL
 Target Server Version : 80100
 File Encoding         : 65001

 Date: 30/10/2023 19:51:47
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for blog_home_hp
-- ----------------------------
DROP TABLE IF EXISTS `blog_home_hp`;
CREATE TABLE `blog_home_hp`  (
  `id` bigint NOT NULL,
  `photo` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci NULL DEFAULT NULL COMMENT '博客首页图片',
  `belong_user_id` bigint NULL DEFAULT NULL COMMENT '归属用户id',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of blog_home_hp
-- ----------------------------

-- ----------------------------
-- Table structure for sys_captcha
-- ----------------------------
DROP TABLE IF EXISTS `sys_captcha`;
CREATE TABLE `sys_captcha`  (
  `uuid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'uuid',
  `code` varchar(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '验证码',
  `expire_time` datetime NULL DEFAULT NULL COMMENT '过期时间',
  `base64` varchar(10000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'base64',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统验证码' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci NULL DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci NULL DEFAULT NULL COMMENT '操作名称',
  `method` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci NULL DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci NULL DEFAULT NULL COMMENT '请求参数',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `parent_id` bigint NOT NULL DEFAULT 0 COMMENT '所属上级',
  `title` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '菜单标题',
  `component` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件名称',
  `sort_value` int NOT NULL DEFAULT 1 COMMENT '排序',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态(0:禁止,1:正常)',
  `has_children` tinyint NOT NULL DEFAULT 1 COMMENT '是否存在在子节点',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '删除标记（0:不可用 1:可用）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, '系统管理', 'system', 1, 1, 1, '2023-05-04 10:46:47', '2023-05-06 17:33:53', 0);
INSERT INTO `sys_menu` VALUES (2, 1, '用户管理', 'sysUser', 1, 1, 1, '2023-05-04 10:47:13', '2023-05-06 17:33:57', 0);
INSERT INTO `sys_menu` VALUES (3, 1, '角色管理', 'sysRole', 2, 1, 1, '2023-05-04 10:47:19', '2023-05-06 17:33:58', 0);
INSERT INTO `sys_menu` VALUES (4, 1, '菜单管理', 'sysMenu', 3, 1, 1, '2023-05-04 10:47:26', '2023-05-06 17:33:58', 0);
INSERT INTO `sys_menu` VALUES (21, 1, '操作日志', 'sysOperLog', 4, 1, 1, '2023-06-02 09:04:13', '2023-06-02 09:04:21', 0);
INSERT INTO `sys_menu` VALUES (31, 0, '博客管理', 'blog', 1, 1, 1, '2023-10-30 19:02:45', '2023-10-30 19:02:45', 0);
INSERT INTO `sys_menu` VALUES (32, 31, '文章管理', 'article', 1, 1, 1, '2023-10-30 19:09:53', '2023-10-30 19:09:53', 0);
INSERT INTO `sys_menu` VALUES (33, 31, '分类管理', 'type', 2, 1, 1, '2023-10-30 19:10:09', '2023-10-30 19:10:09', 0);
INSERT INTO `sys_menu` VALUES (34, 31, '封面管理', 'theHomeImg', 3, 1, 1, '2023-10-30 19:10:19', '2023-10-30 19:10:19', 0);

-- ----------------------------
-- Table structure for sys_oss
-- ----------------------------
DROP TABLE IF EXISTS `sys_oss`;
CREATE TABLE `sys_oss`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'URL地址',
  `create_date` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件上传' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_oss
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT ' ',
  `role_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci NOT NULL COMMENT '角色名称',
  `level` bigint NOT NULL DEFAULT 10 COMMENT '角色权重 越高越低',
  `status` tinyint NULL DEFAULT NULL COMMENT '角色状态',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 0, 1, '2023-10-09 13:06:00', '2023-10-09 13:06:02');
INSERT INTO `sys_role` VALUES (4, '游客', 100, 1, '2023-10-12 14:05:21', '2023-10-12 14:05:22');
INSERT INTO `sys_role` VALUES (14, '博主', 10, 1, '2023-10-14 14:54:32', '2023-10-14 14:54:32');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint NOT NULL DEFAULT 0,
  `menu_id` bigint NOT NULL DEFAULT 0,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '删除标记（0:不可用 1:可用）',
  `is_half` tinyint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_role_id`(`role_id`) USING BTREE,
  INDEX `idx_menu_id`(`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 112 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '角色菜单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (112, 1, 1, '2023-10-30 19:17:31', '2023-10-30 19:17:31', 0, 0);
INSERT INTO `sys_role_menu` VALUES (113, 1, 2, '2023-10-30 19:17:31', '2023-10-30 19:17:31', 0, 0);
INSERT INTO `sys_role_menu` VALUES (114, 1, 3, '2023-10-30 19:17:31', '2023-10-30 19:17:31', 0, 0);
INSERT INTO `sys_role_menu` VALUES (115, 1, 4, '2023-10-30 19:17:31', '2023-10-30 19:17:31', 0, 0);
INSERT INTO `sys_role_menu` VALUES (116, 1, 21, '2023-10-30 19:17:31', '2023-10-30 19:17:31', 0, 0);
INSERT INTO `sys_role_menu` VALUES (117, 1, 31, '2023-10-30 19:17:31', '2023-10-30 19:17:31', 0, 0);
INSERT INTO `sys_role_menu` VALUES (118, 1, 32, '2023-10-30 19:17:31', '2023-10-30 19:17:31', 0, 0);
INSERT INTO `sys_role_menu` VALUES (119, 1, 33, '2023-10-30 19:17:31', '2023-10-30 19:17:31', 0, 0);
INSERT INTO `sys_role_menu` VALUES (120, 1, 34, '2023-10-30 19:17:31', '2023-10-30 19:17:31', 0, 0);
INSERT INTO `sys_role_menu` VALUES (121, 14, 32, '2023-10-30 19:35:42', '2023-10-30 19:35:42', 0, 0);
INSERT INTO `sys_role_menu` VALUES (122, 14, 31, '2023-10-30 19:35:42', '2023-10-30 19:35:42', 0, 1);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci NOT NULL COMMENT '账号',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci NOT NULL COMMENT '密码',
  `role_id` bigint NOT NULL COMMENT '角色id',
  `avatar` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci NULL DEFAULT NULL COMMENT '管理员头像',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci NULL DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci NULL DEFAULT NULL COMMENT '电话',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态：0禁用 1正常',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `creat_user_id` bigint NULL DEFAULT NULL COMMENT '创建者id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE,
  UNIQUE INDEX `email`(`email`, `mobile`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'zsj', 'e10adc3949ba59abbe56e057f20f883e', 1, '\r\nhttps://edu-zsj-1010.oss-cn-beijing.aliyuncs.com/image/4208e0caccb41e120c8f20e442a6468.jpg', '1013361303@qq.com', '13048543747', 1, '2023-10-08 13:31:42', 1);
INSERT INTO `sys_user` VALUES (2, 'admin', '202cb962ac59075b964b07152d234b70', 4, 'https://request-oss-zsj.oss-cn-beijing.aliyuncs.com/2023-10-13/photo/0c887b95-95c3-4439-ae46-0b7f9b321789m1 (3).jpg', '757327510@qq.com', '15085818446', 0, '2023-10-09 17:11:08', 1);
INSERT INTO `sys_user` VALUES (3, 'mygf', 'f379eaf3c831b04de153469d1bec345e', 4, 'https://request-oss-zsj.oss-cn-beijing.aliyuncs.com/2023-10-13/photo/b3a85df9-6dcc-406a-afd5-5a6d36847928type.jpg', 'zsjemail666@163.com', '13985199063', 0, '2023-10-13 16:48:31', 1);
INSERT INTO `sys_user` VALUES (4, 'ptg', 'e10adc3949ba59abbe56e057f20f883e', 5, 'https://request-oss-zsj.oss-cn-beijing.aliyuncs.com/2023-10-13/photo/2c66534c-78cb-47eb-9916-c9922ad8154fChainsaw-Man-Reze-Chainsaw-Man-inoitoh-anime-girls-looking-at-viewer-choker-2263402-wallhere.com.jpg', '1641823706@qq.com', '15085818765', 0, '2023-10-13 17:04:28', 1);
INSERT INTO `sys_user` VALUES (5, 'killer_queen', '272e7b02a23578284113b15ba99ba770', 3, 'https://request-oss-zsj.oss-cn-beijing.aliyuncs.com/2023-10-13/photo/d2ff0063-e018-423f-9c02-a69348cdf31em1 (1).jpg', 'killqueen@qq.com', '15775866574', 0, '2023-10-13 19:11:24', 1);
INSERT INTO `sys_user` VALUES (6, 'jingxiang', '202cb962ac59075b964b07152d234b70', 2, 'https://request-oss-zsj.oss-cn-beijing.aliyuncs.com/2023-10-13/photo/93c8aba8-3e45-4b9a-87d1-0db86cb987afgirl.jpg', 'plmm@plmm.com', '13500015457', 0, '2023-10-13 19:45:22', 1);
INSERT INTO `sys_user` VALUES (7, 'rike2', '202cb962ac59075b964b07152d234b70', 4, 'https://request-oss-zsj.oss-cn-beijing.aliyuncs.com/2023-10-13/photo/f7425da7-1f55-4900-9b3b-096d7703238dm1 (2).jpg', 'rike1@qq.com', '13048543748', 0, '2023-10-13 19:46:56', 6);
INSERT INTO `sys_user` VALUES (8, 'human', '202cb962ac59075b964b07152d234b70', 14, 'https://request-oss-zsj.oss-cn-beijing.aliyuncs.com/2023-10-14/photo/undefinedm1 (1).jpg', '10145465@qq.com', '15456789444', 1, '2023-10-14 22:29:44', 1);
INSERT INTO `sys_user` VALUES (9, 'plmm', '202cb962ac59075b964b07152d234b70', 1, 'https://request-oss-zsj.oss-cn-beijing.aliyuncs.com/2023-10-15/photo/undefinedtype.jpg', 'plmm123@qq.com', '17765631809', 0, '2023-10-15 19:46:49', 8);
INSERT INTO `sys_user` VALUES (10, 'dsxh', '202cb962ac59075b964b07152d234b70', 1, 'https://request-oss-zsj.oss-cn-beijing.aliyuncs.com/2023-10-15/photo/undefinedChainsaw-Man-Kobeni-Chainsaw-Man-inoitoh-anime-girls-moles-mole-under-mouth-2263406-wallhere.com.jpg', 'dsxh66@qq.com', '13047588864', 0, '2023-10-15 20:01:31', 8);
INSERT INTO `sys_user` VALUES (11, 'dsxh2', '202cb962ac59075b964b07152d234b70', 1, 'https://request-oss-zsj.oss-cn-beijing.aliyuncs.com/2023-10-15/photo/undefinedChainsaw-Man-Kobeni-Chainsaw-Man-inoitoh-anime-girls-moles-mole-under-mouth-2263406-wallhere.com.jpg', 'dsxh662@qq.com', '13047588863', 0, '2023-10-15 20:02:05', 8);
INSERT INTO `sys_user` VALUES (12, 'testcount', '202cb962ac59075b964b07152d234b70', 14, 'https://request-oss-zsj.oss-cn-beijing.aliyuncs.com/2023-10-15/photo/undefinedcfbb2c1a9ee28f93a5013b65efd6de962075396288.jpg', '1014546225@qq.com', '13048533487', 1, '2023-10-15 20:04:06', 8);
INSERT INTO `sys_user` VALUES (13, 'fuck', '202cb962ac59075b964b07152d234b70', 4, 'https://request-oss-zsj.oss-cn-beijing.aliyuncs.com/2023-10-15/photo/undefined微信图片_20231015004548.jpg', 'fuckall@qq.com', '15087945988', 0, '2023-10-15 20:11:58', 8);
INSERT INTO `sys_user` VALUES (14, 'test', 'e10adc3949ba59abbe56e057f20f883e', 14, 'https://request-oss-zsj.oss-cn-beijing.aliyuncs.com/2023-10-30/photo/undefinedwechat_img.jpg', 'dsxh662@qq.com', '17785764588', 1, '2023-10-30 19:34:26', 1);

-- ----------------------------
-- Table structure for sys_user_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_token`;
CREATE TABLE `sys_user_token`  (
  `user_id` bigint NOT NULL,
  `token` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'token',
  `expire_time` datetime NULL DEFAULT NULL COMMENT '过期时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户Token' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_token
-- ----------------------------
INSERT INTO `sys_user_token` VALUES (1, 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6ImNlN2YxOGJmNmUyNzQ2MmVhYTBiN2E0MGEwNDkxMTZiIiwiYWNjb3VudCI6InpzaiJ9.Qj2ll_rzFT8SIgRHimXIYRopaduw5KfFrPa5FGCnpHE', '2023-10-31 19:37:09', '2023-10-30 19:37:09');
INSERT INTO `sys_user_token` VALUES (2, 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjRhZDAyNjY1NDJiMTQ3YzZiZDJlMzY0NjZiNDI1ZTEyIiwiYWNjb3VudCI6ImFkbWluIn0.8t_Lx7F0oKnM5zEI9wEmBxHNx5VkWNjuNJW_fIakrqk', '2023-10-14 17:47:26', '2023-10-13 17:47:26');
INSERT INTO `sys_user_token` VALUES (3, 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjRmMWNhNGUzMzYyMTQ3MGI5YWM3MzU3Y2VhN2EzYzg5IiwiYWNjb3VudCI6Im15Z2YifQ.Ta5YepwNW32Ru0VDivtqGVoZrZ35Ozs83LuSXf24S3M', '2023-10-14 16:59:57', '2023-10-13 16:59:57');
INSERT INTO `sys_user_token` VALUES (5, 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6ImYyYWE2NGI5NmY4ZTRkMDlhOGQyN2ZlN2I4Mzk0ZGZkIiwiYWNjb3VudCI6ImtpbGxlcl9xdWVlbiJ9.ehJvAH11sbsjVo13otcioFLcD2PzPSn-r_p4xNx-hzc', '2023-10-14 19:12:27', '2023-10-13 19:12:27');
INSERT INTO `sys_user_token` VALUES (6, 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjY3NjRjZDY1YjRmYzRkNTQ4MzEyMTEyNjI2ODlkZjA4IiwiYWNjb3VudCI6Imppbmd4aWFuZyJ9.oyr9v0yHlFVCqkFx6RnoeprFjUXrdTw8KSAiWjdtk98', '2023-10-14 19:45:32', '2023-10-13 19:45:32');
INSERT INTO `sys_user_token` VALUES (7, 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6ImRhYzQzNTNjMTQ2ZDQ1OTk4Njk4YjM5M2M0NmFhMjNhIiwiYWNjb3VudCI6InJpa2UyIn0.iYq6FvYnCebYgssqqBfyiDRlqBT2YcYWzKfUsi0EgFg', '2023-10-14 19:47:04', '2023-10-13 19:47:04');
INSERT INTO `sys_user_token` VALUES (8, 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjU1ZTEyYTQ3MmEwOTQ4NGZiMzM2MWNjYjAxODk0NzliIiwiYWNjb3VudCI6Imh1bWFuIn0.bpQV6gMBUA6NQwXU4CShKVsOMmVV9ZGeU5ChLx4Z-5w', '2023-10-16 20:03:17', '2023-10-15 20:03:17');
INSERT INTO `sys_user_token` VALUES (14, 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjE4ZTU5ZmY3NGFkOTQ3MGNhZDg3NmNjNzI4NDM2NmJkIiwiYWNjb3VudCI6InRlc3QifQ.JiIKPX2Gba15skryJSGNeHp8BuI0sXxtewplxLpNqOU', '2023-10-31 19:35:54', '2023-10-30 19:35:54');

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
