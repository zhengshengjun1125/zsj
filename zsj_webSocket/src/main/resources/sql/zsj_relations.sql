/*
 Navicat Premium Data Transfer

 Source Server         : 8.0
 Source Server Type    : MySQL
 Source Server Version : 80100
 Source Host           : localhost:3307
 Source Schema         : zsj_relations

 Target Server Type    : MySQL
 Target Server Version : 80100
 File Encoding         : 65001

 Date: 12/11/2023 21:59:32
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for relation_message
-- ----------------------------
DROP TABLE IF EXISTS `relation_message`;
CREATE TABLE `relation_message`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '信息id',
  `fromUser` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发送人',
  `toUser` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接收人',
  `message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息内容',
  `alread_read` enum('yes','no') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'no' COMMENT '接收状态',
  `create_time` date NOT NULL COMMENT '发起时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of relation_message
-- ----------------------------
INSERT INTO `relation_message` VALUES ('1723604722735198209', 'ruike', 'zsj', 's', 'yes', '2023-11-12');
INSERT INTO `relation_message` VALUES ('1723694876950908930', 'ruike', 'zsj', '你在干嘛阿？', 'yes', '2023-11-12');
INSERT INTO `relation_message` VALUES ('1723694964179849217', 'zsj', 'ruike', '干你的骚嘴', 'yes', '2023-11-12');

SET FOREIGN_KEY_CHECKS = 1;
