/*
Navicat MariaDB Data Transfer

Source Server         : homeDataBase
Source Server Version : 100508
Source Host           : localhost:3306
Source Database       : category

Target Server Type    : MariaDB
Target Server Version : 100508
File Encoding         : 65001

Date: 2020-12-09 12:51:30
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for hibernate_sequence
-- ----------------------------
DROP TABLE IF EXISTS `hibernate_sequence`;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hibernate_sequence
-- ----------------------------
INSERT INTO `hibernate_sequence` VALUES ('8');
INSERT INTO `hibernate_sequence` VALUES ('8');
INSERT INTO `hibernate_sequence` VALUES ('8');
INSERT INTO `hibernate_sequence` VALUES ('8');
INSERT INTO `hibernate_sequence` VALUES ('8');
INSERT INTO `hibernate_sequence` VALUES ('8');
INSERT INTO `hibernate_sequence` VALUES ('8');
INSERT INTO `hibernate_sequence` VALUES ('8');
INSERT INTO `hibernate_sequence` VALUES ('8');

-- ----------------------------
-- Table structure for t_aria2
-- ----------------------------
DROP TABLE IF EXISTS `t_aria2`;
CREATE TABLE `t_aria2` (
  `gid` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`gid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_aria2
-- ----------------------------

-- ----------------------------
-- Table structure for t_aria2_user
-- ----------------------------
DROP TABLE IF EXISTS `t_aria2_user`;
CREATE TABLE `t_aria2_user` (
  `aria2file_gid` varchar(255) NOT NULL,
  `user_uid` bigint(20) NOT NULL,
  KEY `FKdlxti3ten0q7qhf8d9wh7igha` (`user_uid`),
  KEY `FKb5po7n0pl2q36j660uwqi8snd` (`aria2file_gid`),
  CONSTRAINT `FKb5po7n0pl2q36j660uwqi8snd` FOREIGN KEY (`aria2file_gid`) REFERENCES `t_aria2` (`gid`),
  CONSTRAINT `FKdlxti3ten0q7qhf8d9wh7igha` FOREIGN KEY (`user_uid`) REFERENCES `t_user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_aria2_user
-- ----------------------------

-- ----------------------------
-- Table structure for t_device
-- ----------------------------
DROP TABLE IF EXISTS `t_device`;
CREATE TABLE `t_device` (
  `id` bigint(20) NOT NULL,
  `custom_name` varchar(255) DEFAULT NULL,
  `device_name` varchar(255) DEFAULT NULL,
  `size` bigint(20) DEFAULT NULL,
  `folder_name` varchar(255) DEFAULT NULL,
  `rules` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_device
-- ----------------------------
INSERT INTO `t_device` VALUES ('2', '/api/Files_0', '随机-0', null, '/usr/local/Folder', '*');

-- ----------------------------
-- Table structure for t_files
-- ----------------------------
DROP TABLE IF EXISTS `t_files`;
CREATE TABLE `t_files` (
  `fid` bigint(20) NOT NULL,
  `create_date` datetime DEFAULT NULL,
  `is_dir` smallint(6) DEFAULT NULL,
  `parent_name` varchar(255) DEFAULT NULL,
  `self_name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`fid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_files
-- ----------------------------

-- ----------------------------
-- Table structure for t_files_aria2file
-- ----------------------------
DROP TABLE IF EXISTS `t_files_aria2file`;
CREATE TABLE `t_files_aria2file` (
  `files_fid` bigint(20) NOT NULL,
  `aria2file_gid` varchar(255) NOT NULL,
  PRIMARY KEY (`files_fid`,`aria2file_gid`),
  KEY `FKg0nbjmub0acw9ofk69ct257rw` (`aria2file_gid`),
  CONSTRAINT `FK99ves29ru7vd4i3rvrxajvt20` FOREIGN KEY (`files_fid`) REFERENCES `t_files` (`fid`),
  CONSTRAINT `FKg0nbjmub0acw9ofk69ct257rw` FOREIGN KEY (`aria2file_gid`) REFERENCES `t_aria2` (`gid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_files_aria2file
-- ----------------------------

-- ----------------------------
-- Table structure for t_files_origin_file
-- ----------------------------
DROP TABLE IF EXISTS `t_files_origin_file`;
CREATE TABLE `t_files_origin_file` (
  `files_fid` bigint(20) NOT NULL,
  `origin_file_oid` bigint(20) NOT NULL,
  PRIMARY KEY (`files_fid`,`origin_file_oid`),
  KEY `FKd51pyjqfhklu0qp9ot1o9ps18` (`origin_file_oid`),
  CONSTRAINT `FKd51pyjqfhklu0qp9ot1o9ps18` FOREIGN KEY (`origin_file_oid`) REFERENCES `t_origin_file` (`oid`),
  CONSTRAINT `FKdmqnayf7enixsvxh64ihpqxjh` FOREIGN KEY (`files_fid`) REFERENCES `t_files` (`fid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_files_origin_file
-- ----------------------------

-- ----------------------------
-- Table structure for t_files_user
-- ----------------------------
DROP TABLE IF EXISTS `t_files_user`;
CREATE TABLE `t_files_user` (
  `files_fid` bigint(20) NOT NULL,
  `user_uid` bigint(20) NOT NULL,
  KEY `FKggqn8gc1eyvlnxynexil8oa1n` (`user_uid`),
  KEY `FK9vha9ahu0dlow77avjmhekyja` (`files_fid`),
  CONSTRAINT `FK9vha9ahu0dlow77avjmhekyja` FOREIGN KEY (`files_fid`) REFERENCES `t_files` (`fid`),
  CONSTRAINT `FKggqn8gc1eyvlnxynexil8oa1n` FOREIGN KEY (`user_uid`) REFERENCES `t_user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_files_user
-- ----------------------------

-- ----------------------------
-- Table structure for t_files_version
-- ----------------------------
DROP TABLE IF EXISTS `t_files_version`;
CREATE TABLE `t_files_version` (
  `group_id` bigint(20) NOT NULL,
  `desc_` varchar(255) DEFAULT NULL,
  `group_name` varchar(255) DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `version` double DEFAULT NULL,
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_files_version
-- ----------------------------

-- ----------------------------
-- Table structure for t_files_version_origin_file_set
-- ----------------------------
DROP TABLE IF EXISTS `t_files_version_origin_file_set`;
CREATE TABLE `t_files_version_origin_file_set` (
  `files_version_group_id` bigint(20) NOT NULL,
  `origin_file_set_oid` bigint(20) NOT NULL,
  PRIMARY KEY (`files_version_group_id`,`origin_file_set_oid`),
  KEY `FK1215jr95qq2yebe1l7stmhxjg` (`origin_file_set_oid`),
  CONSTRAINT `FK1215jr95qq2yebe1l7stmhxjg` FOREIGN KEY (`origin_file_set_oid`) REFERENCES `t_origin_file` (`oid`),
  CONSTRAINT `FKgi4o1xmqff9ag2rcenwk24n0u` FOREIGN KEY (`files_version_group_id`) REFERENCES `t_files_version` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_files_version_origin_file_set
-- ----------------------------

-- ----------------------------
-- Table structure for t_files_version_users
-- ----------------------------
DROP TABLE IF EXISTS `t_files_version_users`;
CREATE TABLE `t_files_version_users` (
  `files_version_group_id` bigint(20) NOT NULL,
  `users_uid` bigint(20) NOT NULL,
  PRIMARY KEY (`files_version_group_id`,`users_uid`),
  KEY `FKgux13k3ufteqolyxvhdcqfe8p` (`users_uid`),
  CONSTRAINT `FKgux13k3ufteqolyxvhdcqfe8p` FOREIGN KEY (`users_uid`) REFERENCES `t_user` (`uid`),
  CONSTRAINT `FKk6tbx7q1mkbfwrsmh2lnseiyr` FOREIGN KEY (`files_version_group_id`) REFERENCES `t_files_version` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_files_version_users
-- ----------------------------

-- ----------------------------
-- Table structure for t_origin_file
-- ----------------------------
DROP TABLE IF EXISTS `t_origin_file`;
CREATE TABLE `t_origin_file` (
  `oid` bigint(20) NOT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `md5` varchar(255) DEFAULT NULL,
  `size` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`oid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_origin_file
-- ----------------------------

-- ----------------------------
-- Table structure for t_origin_file_hard_disk_device
-- ----------------------------
DROP TABLE IF EXISTS `t_origin_file_hard_disk_device`;
CREATE TABLE `t_origin_file_hard_disk_device` (
  `origin_file_oid` bigint(20) NOT NULL,
  `hard_disk_device_id` bigint(20) NOT NULL,
  PRIMARY KEY (`origin_file_oid`,`hard_disk_device_id`),
  KEY `FKlruwh3ryx1u3p3msyp1ffh72u` (`hard_disk_device_id`),
  CONSTRAINT `FKlruwh3ryx1u3p3msyp1ffh72u` FOREIGN KEY (`hard_disk_device_id`) REFERENCES `t_device` (`id`),
  CONSTRAINT `FKp209ao9tx9twiv63rdc8cfgyq` FOREIGN KEY (`origin_file_oid`) REFERENCES `t_origin_file` (`oid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_origin_file_hard_disk_device
-- ----------------------------

-- ----------------------------
-- Table structure for t_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_permission`;
CREATE TABLE `t_permission` (
  `pid` bigint(20) NOT NULL,
  `name` varchar(20) NOT NULL,
  `rid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_permission
-- ----------------------------

-- ----------------------------
-- Table structure for t_plugin
-- ----------------------------
DROP TABLE IF EXISTS `t_plugin`;
CREATE TABLE `t_plugin` (
  `id` bigint(20) NOT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `mapping` varchar(255) DEFAULT NULL,
  `plugin_name` varchar(255) DEFAULT NULL,
  `port` varchar(255) DEFAULT NULL,
  `status` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_plugin
-- ----------------------------

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `rid` bigint(20) NOT NULL,
  `name` varchar(20) NOT NULL,
  `status` bit(1) DEFAULT NULL,
  PRIMARY KEY (`rid`),
  UNIQUE KEY `UKbkpm7njy2ort1yoiddc7jg8gj` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES ('1', 'admin', '');

-- ----------------------------
-- Table structure for t_token
-- ----------------------------
DROP TABLE IF EXISTS `t_token`;
CREATE TABLE `t_token` (
  `id` bigint(20) NOT NULL,
  `token` varchar(255) NOT NULL,
  `user` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6v50finhpx4gmna9why0jbcbp` (`user`,`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_token
-- ----------------------------
INSERT INTO `t_token` VALUES ('7', 'f330ac27-8079-4206-a6fb-8c2608d473aa', '6');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `uid` bigint(20) NOT NULL,
  `create_date` bigint(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  `nick_name` varchar(20) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `UKi6qjjoe560mee5ajdg7v1o6mi` (`email`),
  KEY `FKoq4kvs7cl82x4jucwxbnmkqxe` (`role`),
  CONSTRAINT `FKoq4kvs7cl82x4jucwxbnmkqxe` FOREIGN KEY (`role`) REFERENCES `t_role` (`rid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('6', '1607489465949', 'admin@test.com', 'admin', 'd7db29350633c6a73d2591a3f8101b0e', '1');
