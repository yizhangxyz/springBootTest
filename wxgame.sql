/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80012
 Source Host           : localhost:3306
 Source Schema         : wxgame

 Target Server Type    : MySQL
 Target Server Version : 80012
 File Encoding         : 65001

 Date: 21/09/2018 16:06:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for subject_result
-- ----------------------------
DROP TABLE IF EXISTS `subject_result`;
CREATE TABLE `subject_result`  (
  `subjectId` int(11) NOT NULL COMMENT '题目id',
  `minScore` int(11) NOT NULL COMMENT '分数区间最小值',
  `maxScore` int(11) NOT NULL COMMENT '分数区间最大值',
  `result` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '答题结果'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of subject_result
-- ----------------------------
INSERT INTO `subject_result` VALUES (1, 1, 59, '差');
INSERT INTO `subject_result` VALUES (1, 60, 100, '及格');

-- ----------------------------
-- Table structure for subject_type
-- ----------------------------
DROP TABLE IF EXISTS `subject_type`;
CREATE TABLE `subject_type`  (
  `subjectId` int(11) NOT NULL COMMENT '题目id',
  `type` int(11) NOT NULL COMMENT '题目类型',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`subjectId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of subject_type
-- ----------------------------
INSERT INTO `subject_type` VALUES (1, 1, '普通题目');

-- ----------------------------
-- Table structure for subjects
-- ----------------------------
DROP TABLE IF EXISTS `subjects`;
CREATE TABLE `subjects`  (
  `subjectId` int(11) NOT NULL DEFAULT 0 COMMENT '所属题目id',
  `questionIndex` int(11) NOT NULL COMMENT '题目序号',
  `score` int(11) NOT NULL DEFAULT 0 COMMENT '题目分数',
  `content` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL COMMENT '内容',
  `answerType` int(11) NOT NULL DEFAULT 0 COMMENT '答题类型（0单选,1多选）',
  `answerCount` int(11) NOT NULL COMMENT '答案数量',
  `answers` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL DEFAULT '' COMMENT '答案，用\"*\"分隔',
  `weights` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL DEFAULT '0' COMMENT '答案权重，用\"*\"分隔',
  `nextQuestions` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL COMMENT '选择了不同答案后，转向的下一题，用\"*\"分隔',
  `analizer` int(11) NOT NULL DEFAULT 0 COMMENT '答案分析器（0无，1权重和为100,）'
) ENGINE = InnoDB CHARACTER SET = gbk COLLATE = gbk_chinese_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of subjects
-- ----------------------------
INSERT INTO `subjects` VALUES (1, 1, 100, '你来自哪个国家？', 0, 4, '中国*美国*日本*英国*', '100*0*0*0*', '0*0*0*0*', 1);

-- ----------------------------
-- Table structure for user_tokens
-- ----------------------------
DROP TABLE IF EXISTS `user_tokens`;
CREATE TABLE `user_tokens`  (
  `token` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL,
  `openId` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `updateTime` timestamp(6) DEFAULT NULL,
  PRIMARY KEY (`token`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_tokens
-- ----------------------------
INSERT INTO `user_tokens` VALUES ('0c148139ff6747a98b02c53b5b430f42', 'oubx35LXlyCO63TOVIPpFnANhtD0', '2018-09-18 15:49:53.000000');
INSERT INTO `user_tokens` VALUES ('1c12b7a7c617474eb1a3b741060a2866', 'oubx35IXr4hiswICym1zyID5yPNQ', '2018-09-19 18:03:51.000000');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `openId` varchar(255) CHARACTER SET gbk COLLATE gbk_chinese_ci NOT NULL COMMENT 'wx唯一标识',
  `token` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '服务器生成的标识',
  `sessionKey` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'wx session',
  `createTime` timestamp(6) DEFAULT NULL COMMENT '账号创建时间',
  `updateTime` timestamp(6) DEFAULT NULL COMMENT '账号更新时间',
  PRIMARY KEY (`openId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('oubx35IXr4hiswICym1zyID5yPNQ', '1c12b7a7c617474eb1a3b741060a2866', 'Of471g20F44Qp8oSKafZaA==', '2018-08-08 18:06:11.000000', '2018-09-19 18:03:51.000000');
INSERT INTO `users` VALUES ('oubx35LXlyCO63TOVIPpFnANhtD0', '0c148139ff6747a98b02c53b5b430f42', 'tLd3EAH0xifvjWyz/rWTMg==', '2018-08-28 10:27:39.000000', '2018-09-18 15:49:53.000000');

-- ----------------------------
-- Procedure structure for create_update_user
-- ----------------------------
DROP PROCEDURE IF EXISTS `create_update_user`;
delimiter ;;
CREATE DEFINER=`root`@`%` PROCEDURE `create_update_user`(IN `_openid` varchar(255),IN `_token` varchar(255),IN `_sessionKey` varchar(255),IN `_updateTime` timestamp)
BEGIN

	DECLARE ret_ int DEFAULT 0;
	DECLARE old_token_ VARCHAR(255) DEFAULT NULL;
	
	SELECT token INTO old_token_ FROM users WHERE openId=_openid;
	
	if old_token_ is not NULL then
		UPDATE users SET token = _token,sessionKey=_sessionKey,updateTime=_updateTime WHERE openId=_openid;
	ELSE
		INSERT INTO users(openId,token,sessionKey,createTime,updateTime) VALUES(_openid,_token,_sessionKey,_updateTime,_updateTime);
	END IF;
	
	DELETE FROM user_tokens WHERE token=old_token_;
	INSERT INTO user_tokens(token,openId,updateTime) VALUES(_token,_openid,_updateTime);
 
	SELECT ret_;
END
;;
delimiter ;

-- ----------------------------
-- Procedure structure for find_user_bytoken
-- ----------------------------
DROP PROCEDURE IF EXISTS `find_user_bytoken`;
delimiter ;;
CREATE DEFINER=`root`@`%` PROCEDURE `find_user_bytoken`(IN `_token` varchar(255))
BEGIN

	DECLARE ret_         int DEFAULT 1; 
	DECLARE openid_      VARCHAR(255) DEFAULT '';
	DECLARE token_       VARCHAR(255) DEFAULT '';
	DECLARE sessionKey_  VARCHAR(255) DEFAULT '';
	DECLARE createTime_  TIMESTAMP;
	DECLARE updateTime_  TIMESTAMP;
	
	SELECT openId INTO openid_ FROM user_tokens WHERE token=_token;
	
	IF openid_ <> '' THEN
		SET ret_ = 0;
		SELECT openId,token,sessionKey,createTime,updateTime INTO openid_,token_,sessionKey_,createTime_,updateTime_ FROM users WHERE openId=openid_;
	END IF;
	
	SELECT ret_,openid_,token_,sessionKey_,createTime_,updateTime_;


END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
