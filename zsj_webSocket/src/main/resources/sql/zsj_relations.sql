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

 Date: 13/11/2023 22:35:32
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
INSERT INTO `relation_message` VALUES ('1724001737101119490', 'zsj', 'ruike', 'alert(\"哈哈\")', 'yes', '2023-11-13');
INSERT INTO `relation_message` VALUES ('1724001795494219778', 'zsj', 'ruike', 'windows.aleat(\"哈哈哈哈\")', 'yes', '2023-11-13');
INSERT INTO `relation_message` VALUES ('1724001882911903746', 'ruike', 'zsj', '  window.alert(\'哈哈哈\')', 'yes', '2023-11-13');
INSERT INTO `relation_message` VALUES ('1724001900066607106', 'zsj', 'ruike', '  window.alert(\'哈哈哈\')', 'yes', '2023-11-13');
INSERT INTO `relation_message` VALUES ('1724008871587057665', 'zsj', 'ruike', 'ddd', 'yes', '2023-11-13');
INSERT INTO `relation_message` VALUES ('1724008885369540609', 'zsj', 'ruike', ':grinning:', 'yes', '2023-11-13');
INSERT INTO `relation_message` VALUES ('1724011761605120002', 'ruike', 'zsj', '啊啊啊', 'yes', '2023-11-13');
INSERT INTO `relation_message` VALUES ('1724014514792402945', 'zsj', 'ruike', '&#128512;', 'yes', '2023-11-13');
INSERT INTO `relation_message` VALUES ('1724015238058184706', 'ruike', 'zsj', '😀', 'yes', '2023-11-13');
INSERT INTO `relation_message` VALUES ('1724027232089903106', 'ruike', 'zsj', '🤡', 'yes', '2023-11-13');
INSERT INTO `relation_message` VALUES ('1724028010334621698', 'ruike', 'zsj', '你是小丑吗？🤡🤡🤡🤡🤡🤡🤡🤡🤡', 'yes', '2023-11-13');
INSERT INTO `relation_message` VALUES ('1724028064256593922', 'zsj', 'ruike', '🦴', 'yes', '2023-11-13');
INSERT INTO `relation_message` VALUES ('1724031950224723969', 'zsj', 'ruike', '💪EMOJIEMOJIEMOJIEMOJIEMOJIEMOJIEMOJI💪EMOJIEMOJIEMOJIEMOJIEMOJIEMOJIEMOJI💪EMOJIEMOJIEMOJIEMOJIEMOJIEMOJIEMOJI💪EMOJIEMOJIEMOJIEMOJIEMOJIEMOJIEMOJI', 'yes', '2023-11-13');
INSERT INTO `relation_message` VALUES ('1724031959263449090', 'zsj', 'ruike', '💪EMOJIEMOJIEMOJIEMOJIEMOJIEMOJIEMOJI💪EMOJIEMOJIEMOJIEMOJIEMOJIEMOJIEMOJI💪EMOJIEMOJIEMOJIEMOJIEMOJIEMOJIEMOJI💪EMOJIEMOJIEMOJIEMOJIEMOJIEMOJIEMOJI💪EMOJIEMOJIEMOJIEMOJIEMOJIEMOJIEMOJI💪EMOJIEMOJIEMOJIEMOJIEMOJIEMOJIEMOJI💪EMOJIEMOJIEMOJIEMOJIEMOJIEMOJIEMOJI', 'yes', '2023-11-13');
INSERT INTO `relation_message` VALUES ('1724032033401966594', 'ruike', 'zsj', 'are u fucking fool?\n', 'yes', '2023-11-13');
INSERT INTO `relation_message` VALUES ('1724033248328257538', 'zsj', 'ruike', 'dsadas\n\n', 'yes', '2023-11-13');
INSERT INTO `relation_message` VALUES ('1724033325889327105', 'zsj', 'ruike', '你好阿\n', 'yes', '2023-11-13');
INSERT INTO `relation_message` VALUES ('1724033494278049793', 'zsj', 'ruike', '我从\n\n', 'yes', '2023-11-13');
INSERT INTO `relation_message` VALUES ('1724033501882322946', 'zsj', 'ruike', '我操\n', 'yes', '2023-11-13');
INSERT INTO `relation_message` VALUES ('1724033511202066434', 'zsj', 'ruike', '你是谁阿\n', 'yes', '2023-11-13');
INSERT INTO `relation_message` VALUES ('1724033519590674433', 'zsj', 'ruike', '我喜欢你\n', 'yes', '2023-11-13');
INSERT INTO `relation_message` VALUES ('1724033535625498625', 'zsj', 'ruike', '我好饿哦\n', 'yes', '2023-11-13');
INSERT INTO `relation_message` VALUES ('1724033559285567489', 'zsj', 'ruike', 'fuck u day and night\n', 'yes', '2023-11-13');

SET FOREIGN_KEY_CHECKS = 1;
