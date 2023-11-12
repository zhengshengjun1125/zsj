/*
 Navicat Premium Data Transfer

 Source Server         : 8.0
 Source Server Type    : MySQL
 Source Server Version : 80100
 Source Host           : localhost:3307
 Source Schema         : zsj_article

 Target Server Type    : MySQL
 Target Server Version : 80100
 File Encoding         : 65001

 Date: 09/11/2023 11:59:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for art_class
-- ----------------------------
DROP TABLE IF EXISTS `art_class`;
CREATE TABLE `art_class`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类id',
  `class_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci NOT NULL COMMENT '分类名称',
  `create_time` datetime NULL DEFAULT NULL COMMENT '分类创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '分类修改时间',
  `class_status` tinyint NULL DEFAULT 1 COMMENT '分类状态',
  `class_father_id` bigint NULL DEFAULT 0 COMMENT '分类父id 为0为根节点',
  `class_creater` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci NOT NULL COMMENT '分类创建者',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `class_name`(`class_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of art_class
-- ----------------------------
INSERT INTO `art_class` VALUES (3, '生活', '2023-10-07 20:41:28', '2023-10-07 20:41:30', 1, 0, 'zsj');
INSERT INTO `art_class` VALUES (17, '技能', '2023-10-15 18:28:15', '2023-10-15 18:28:15', 1, 0, 'zsj');
INSERT INTO `art_class` VALUES (19, '情感', '2023-10-15 19:19:34', '2023-10-15 19:19:34', 1, 3, 'zsj');
INSERT INTO `art_class` VALUES (20, '恋爱', '2023-10-15 19:19:48', '2023-10-15 19:19:48', 1, 19, 'zsj');
INSERT INTO `art_class` VALUES (21, '编程', '2023-10-15 19:20:11', '2023-10-15 19:20:11', 1, 17, 'zsj');
INSERT INTO `art_class` VALUES (22, '做饭', '2023-10-15 19:21:51', '2023-10-15 19:21:51', 1, 3, 'zsj');
INSERT INTO `art_class` VALUES (23, '小吃', '2023-10-15 19:22:03', '2023-10-15 19:22:03', 1, 22, 'zsj');
INSERT INTO `art_class` VALUES (24, '编程语言', '2023-10-15 19:22:24', '2023-10-15 19:22:24', 1, 21, 'zsj');
INSERT INTO `art_class` VALUES (25, 'Java', '2023-10-15 19:31:48', '2023-10-15 19:31:48', 1, 24, 'zsj');
INSERT INTO `art_class` VALUES (26, 'C++', '2023-11-01 19:12:15', '2023-11-01 19:12:15', 1, 24, 'zsj');
INSERT INTO `art_class` VALUES (27, 'PHP', '2023-11-01 19:12:22', '2023-11-01 19:12:22', 1, 24, 'zsj');
INSERT INTO `art_class` VALUES (28, 'Python', '2023-11-01 19:12:28', '2023-11-01 19:12:28', 1, 24, 'zsj');
INSERT INTO `art_class` VALUES (29, 'Rust', '2023-11-01 19:12:36', '2023-11-01 19:12:36', 1, 24, 'zsj');
INSERT INTO `art_class` VALUES (30, 'JavaScript', '2023-11-01 19:12:46', '2023-11-01 19:12:46', 1, 24, 'zsj');
INSERT INTO `art_class` VALUES (31, 'TypeScript', '2023-11-01 19:12:51', '2023-11-01 19:12:51', 1, 24, 'zsj');
INSERT INTO `art_class` VALUES (32, '框架', '2023-11-01 19:13:06', '2023-11-01 19:13:06', 1, 21, 'zsj');
INSERT INTO `art_class` VALUES (33, '中间件', '2023-11-01 19:13:11', '2023-11-01 19:13:11', 1, 21, 'zsj');
INSERT INTO `art_class` VALUES (34, 'Spring全家桶', '2023-11-01 19:13:24', '2023-11-01 19:13:24', 1, 32, 'zsj');
INSERT INTO `art_class` VALUES (35, '消息队列', '2023-11-01 19:13:36', '2023-11-01 19:13:36', 1, 33, 'zsj');
INSERT INTO `art_class` VALUES (36, '数据库', '2023-11-01 19:13:44', '2023-11-01 19:13:44', 1, 33, 'zsj');
INSERT INTO `art_class` VALUES (37, 'RabbitMq', '2023-11-01 19:13:55', '2023-11-01 19:13:55', 1, 35, 'zsj');
INSERT INTO `art_class` VALUES (38, 'Mysql', '2023-11-01 19:14:01', '2023-11-01 19:14:01', 1, 36, 'zsj');
INSERT INTO `art_class` VALUES (39, 'kafka', '2023-11-01 19:14:28', '2023-11-01 19:14:28', 1, 35, 'zsj');
INSERT INTO `art_class` VALUES (40, 'Spring', '2023-11-01 19:14:46', '2023-11-01 19:14:46', 1, 34, 'zsj');
INSERT INTO `art_class` VALUES (41, 'SpringMvc', '2023-11-01 19:14:56', '2023-11-01 19:14:56', 1, 34, 'zsj');
INSERT INTO `art_class` VALUES (42, 'SpringBoot', '2023-11-01 19:15:19', '2023-11-01 19:15:19', 1, 34, 'zsj');
INSERT INTO `art_class` VALUES (43, 'SpringCloud', '2023-11-01 19:15:25', '2023-11-01 19:15:25', 1, 34, 'zsj');
INSERT INTO `art_class` VALUES (44, 'Mybatis', '2023-11-01 19:15:37', '2023-11-01 19:15:37', 1, 32, 'zsj');
INSERT INTO `art_class` VALUES (45, 'Mybatis-Plus', '2023-11-01 19:15:53', '2023-11-01 19:15:53', 1, 32, 'zsj');
INSERT INTO `art_class` VALUES (46, 'NoSql', '2023-11-01 19:16:16', '2023-11-01 19:16:16', 1, 33, 'zsj');
INSERT INTO `art_class` VALUES (47, 'Redis', '2023-11-01 19:16:32', '2023-11-01 19:16:32', 1, 46, 'zsj');
INSERT INTO `art_class` VALUES (48, '全文检索', '2023-11-01 19:17:02', '2023-11-01 19:17:02', 1, 33, 'zsj');
INSERT INTO `art_class` VALUES (49, 'Elasticsearch', '2023-11-01 19:17:26', '2023-11-01 19:17:26', 1, 48, 'zsj');

-- ----------------------------
-- Table structure for art_entity
-- ----------------------------
DROP TABLE IF EXISTS `art_entity`;
CREATE TABLE `art_entity`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '文章id',
  `art_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci NOT NULL COMMENT '文章标题',
  `art_class_id` bigint NOT NULL COMMENT '文章所属分类id',
  `art_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci NOT NULL COMMENT '文章内容 需要是一个html格式或者md格式',
  `art_request_month` bigint NULL DEFAULT 0 COMMENT '文章月访问量',
  `art_request_day` bigint NULL DEFAULT 0 COMMENT '文章日访问量',
  `art_request_total` bigint NULL DEFAULT 0 COMMENT '文章总访问量',
  `art_auther` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_croatian_ci NOT NULL COMMENT '文章作者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '文章发布时间',
  `update_time` datetime NOT NULL COMMENT '文章修改时间',
  `art_status` tinyint NOT NULL DEFAULT 1 COMMENT '0表示删除 1表示正常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_croatian_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of art_entity
-- ----------------------------
INSERT INTO `art_entity` VALUES (1, 'vue3学习笔记', 25, '# 学习开始\n## 如果使用vue3\n- 无序列表\n- 1. 有序列表\n- |column1|column2|column3|\n|-|-|-|\n|content1|content2|content3|\n[链接](http://)\n![色图](https://request-oss-zsj.oss-cn-beijing.aliyuncs.com/2023-10-13/photo/93c8aba8-3e45-4b9a-87d1-0db86cb987afgirl.jpg)\n> 引用\n', 25, 11, 36588, 'zsj', '2023-10-07 20:43:34', '2023-10-07 20:43:37', 0);
INSERT INTO `art_entity` VALUES (2, '测试主题2', 11, ' <h1>这是一条测试文本2</h1>', 25, 11, 484894, 'zsj', '2023-10-07 20:43:34', '2023-10-07 20:43:37', 0);
INSERT INTO `art_entity` VALUES (3, '测试主题33', 7, ' <h1>这是一条测试文本3</h1>\n- 无序列表', 97979, 7878, 454545488, 'zsj', '2023-10-07 20:43:34', '2023-10-07 20:43:37', 0);
INSERT INTO `art_entity` VALUES (4, '码农的自传', 4, '**粗体**\n# 哈哈哈哈', 0, 0, 0, 'zsj', '2023-10-14 22:26:00', '2023-10-14 22:26:00', 0);
INSERT INTO `art_entity` VALUES (5, 'human的文章', 5, 'a ada **粗体**', 0, 0, 0, 'human', '2023-10-14 22:30:20', '2023-10-14 22:30:20', 0);
INSERT INTO `art_entity` VALUES (6, '从0到1的博客系统构建(后台)', 25, '# 关于技术选型\n:grinning::fearful:\n哈哈哈', 0, 0, 0, 'zsj', '2023-10-15 19:33:54', '2023-10-15 19:33:54', 1);
INSERT INTO `art_entity` VALUES (7, 'ruike的专属是谁呢?', 20, '是瑞克的老婆哈哈:joy:\n但是他是一个疯狂的科学家 除了莫迪没有人会爱他:relieved:', 0, 0, 0, 'ruike', '2023-11-01 18:50:06', '2023-11-01 18:50:06', 1);

SET FOREIGN_KEY_CHECKS = 1;
