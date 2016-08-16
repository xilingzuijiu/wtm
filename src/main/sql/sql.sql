-- --------------------------------------------------------
-- 主机:                           121.42.196.236
-- 服务器版本:                        5.6.31-0ubuntu0.14.04.2 - (Ubuntu)
-- 服务器操作系统:                      debian-linux-gnu
-- HeidiSQL 版本:                  9.3.0.4984
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 导出 weitaomi 的数据库结构
CREATE DATABASE IF NOT EXISTS `weitaomi` /*!40100 DEFAULT CHARACTER SET gbk */;
USE `weitaomi`;


-- 导出  表 weitaomi.seckill 结构
CREATE TABLE IF NOT EXISTS `seckill` (
  `seckill_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `goodsName` varchar(200) NOT NULL COMMENT '商品名称',
  `goodsNum` int(11) NOT NULL COMMENT '商品数量',
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '秒杀开始时间',
  `end_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '秒杀结束时间',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '时间',
  PRIMARY KEY (`seckill_id`),
  KEY `idx_start_time` (`start_time`),
  KEY `idx_end_time` (`end_time`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';

-- 正在导出表  weitaomi.seckill 的数据：~0 rows (大约)
DELETE FROM `seckill`;
/*!40000 ALTER TABLE `seckill` DISABLE KEYS */;
/*!40000 ALTER TABLE `seckill` ENABLE KEYS */;


-- 导出  表 weitaomi.success_killed 结构
CREATE TABLE IF NOT EXISTS `success_killed` (
  `success_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `seckill_id` bigint(20) NOT NULL COMMENT '秒杀产品ID',
  `user_phone` bigint(20) NOT NULL COMMENT '用户手机号',
  `state` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '秒杀状态 -1：秒杀无效，0：秒杀成功，1：已付款',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '秒杀时间',
  PRIMARY KEY (`success_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀记录表';

-- 正在导出表  weitaomi.success_killed 的数据：~0 rows (大约)
DELETE FROM `success_killed`;
/*!40000 ALTER TABLE `success_killed` DISABLE KEYS */;
/*!40000 ALTER TABLE `success_killed` ENABLE KEYS */;


-- 导出  表 weitaomi.wtm_article 结构
CREATE TABLE IF NOT EXISTS `wtm_article` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userId` bigint(20) NOT NULL COMMENT '商家ID',
  `url` varchar(500) NOT NULL COMMENT '文章地址',
  `title` varchar(500) NOT NULL COMMENT '文章标题',
  `articleAbstract` varchar(500) NOT NULL COMMENT '文章摘要',
  `readNumber` int(11) DEFAULT NULL COMMENT '总阅读数',
  `agreeNumber` int(11) DEFAULT NULL COMMENT '点赞数',
  `agreeIncreaseNumber` int(11) NOT NULL DEFAULT '0' COMMENT '通过该平台点赞增长数',
  `readIncreaseNumber` int(11) NOT NULL DEFAULT '0' COMMENT '通过该平台阅读增长数',
  `validNumber` int(11) DEFAULT '0' COMMENT '有效阅读数',
  `isTop` int(11) NOT NULL DEFAULT '0' COMMENT '是否置顶 0：不置顶，1：置顶',
  `createTime` bigint(20) DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`userId`),
  KEY `idx_title` (`title`(255)),
  KEY `idx_isTop` (`isTop`),
  KEY `idx_createTime` (`createTime`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='文章列表';

-- 正在导出表  weitaomi.wtm_article 的数据：~8 rows (大约)
DELETE FROM `wtm_article`;
/*!40000 ALTER TABLE `wtm_article` DISABLE KEYS */;
INSERT INTO `wtm_article` (`id`, `userId`, `url`, `title`, `articleAbstract`, `readNumber`, `agreeNumber`, `agreeIncreaseNumber`, `readIncreaseNumber`, `validNumber`, `isTop`, `createTime`) VALUES
  (1, 1, '/123/123/123', '美妆的重要性', '很重要', 10, 5, 3, 8, 6, 1, 1467949227),
  (2, 1, '/123/123/123', '食物', '榴莲虽然很臭，但是很有营养', 10, 5, 3, 8, 6, 1, 1467949229),
  (3, 1, '/123/123/123', '理发', '平头很好看', 10, 5, 3, 8, 6, 1, 1467949230),
  (4, 1, '/123/123/123', '时间', '时间管理细则', 10, 5, 3, 8, 6, 1, 1467949235),
  (5, 1, '/123/123/123', '萌娃', '萌娃大比拼', 10, 5, 3, 8, 6, 1, 1467949236),
  (6, 2, '/123/123/123', '动物', '动物世界', 10, 5, 3, 8, 6, 1, 1467949237),
  (7, 2, '/123/123/123', '鸟', '鸟类的世界很有意思', 10, 5, 3, 8, 6, 1, 1467949238),
  (8, 2, '/123/123/123', '朋友', '朋友值得尊敬', 10, 5, 3, 8, 6, 1, 1467949239);
/*!40000 ALTER TABLE `wtm_article` ENABLE KEYS */;


-- 导出  表 weitaomi.wtm_article_read_record 结构
CREATE TABLE IF NOT EXISTS `wtm_article_read_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `memberId` bigint(20) NOT NULL COMMENT '用户ID',
  `articleId` bigint(20) NOT NULL COMMENT '文章ID',
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '类型 0：阅读 1：点赞',
  `createTime` bigint(20) NOT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `idx_article_read_record_memberId` (`memberId`),
  KEY `idx_article_read_record_articleId` (`articleId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='用户阅读记录表';

-- 正在导出表  weitaomi.wtm_article_read_record 的数据：~3 rows (大约)
DELETE FROM `wtm_article_read_record`;
/*!40000 ALTER TABLE `wtm_article_read_record` DISABLE KEYS */;
INSERT INTO `wtm_article_read_record` (`id`, `memberId`, `articleId`, `type`, `createTime`) VALUES
  (1, 4, 1, 0, 1467949235),
  (2, 4, 2, 0, 1467949235),
  (3, 4, 6, 0, 1467949235);
/*!40000 ALTER TABLE `wtm_article_read_record` ENABLE KEYS */;


-- 导出  表 weitaomi.wtm_member 结构
CREATE TABLE IF NOT EXISTS `wtm_member` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `memberName` varchar(120) NOT NULL COMMENT '会员名称',
  `nickName` varchar(120) DEFAULT NULL COMMENT '会员昵称',
  `sex` int(1) DEFAULT '0' COMMENT '性别 0：男，1：女，2，保密',
  `telephone` varchar(20) NOT NULL COMMENT '电话',
  `salt` varchar(50) NOT NULL COMMENT '加密盐',
  `password` varchar(200) NOT NULL COMMENT '密码',
  `birth` bigint(20) DEFAULT NULL COMMENT '会员生日',
  `email` varchar(50) DEFAULT NULL COMMENT '会员邮箱',
  `invitedCode` varchar(20) DEFAULT NULL COMMENT '邀请码',
  `imageUrl` varchar(500) DEFAULT NULL COMMENT '头像url',
  `source` varchar(20) DEFAULT NULL COMMENT '来源',
  `createTime` bigint(20) DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `idx_memberName` (`memberName`),
  KEY `idx_nickName` (`nickName`),
  KEY `idx_member_telephone` (`telephone`),
  KEY `idx_member_email` (`email`),
  KEY `idx_member_createTime` (`createTime`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='会员注册表';

-- 正在导出表  weitaomi.wtm_member 的数据：~3 rows (大约)
DELETE FROM `wtm_member`;
/*!40000 ALTER TABLE `wtm_member` DISABLE KEYS */;
INSERT INTO `wtm_member` (`id`, `memberName`, `nickName`, `sex`, `telephone`, `salt`, `password`, `birth`, `email`, `invitedCode`, `imageUrl`, `source`, `createTime`) VALUES
  (1, 'guest', '游客凭证', 0, '13131313131', 'qwerty', '859d4ce4528b1292335f45a3f36f734bb5782c177a47f395ca5db702e2c913e4', 100000000, 'weitaomi@test.com', '100086', '/admin/image/10039689.jpg', NULL, 100000000),
  (7, 'loyal', NULL, 0, '13953186923', 'RgxEix', 'd8d13c0805f8115b1632e5c32f0bb4db7556adc504c4e1a9ca44a1a59b93f477', NULL, NULL, 'dRznFM', NULL, '苹果', 1471143130),
  (14, 'soder', NULL, 0, '17806235418', '6iXEga', 'fc705e86422b4612dc35619c16580118c9ef80ad29ec43f4dad96ba8b32ab128', NULL, NULL, 'OoBTMB', NULL, '安卓', 1471163689);
/*!40000 ALTER TABLE `wtm_member` ENABLE KEYS */;


-- 导出  表 weitaomi.wtm_member_daily_task 结构
CREATE TABLE IF NOT EXISTS `wtm_member_daily_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `count` bigint(20) NOT NULL DEFAULT '0' COMMENT '奖励积分',
  `taskName` varchar(50) NOT NULL DEFAULT '' COMMENT '任务名称',
  `taskDesc` varchar(50) NOT NULL DEFAULT '' COMMENT '任务描述',
  `createTime` bigint(20) DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='每日任务列表';

-- 正在导出表  weitaomi.wtm_member_daily_task 的数据：~0 rows (大约)
DELETE FROM `wtm_member_daily_task`;
/*!40000 ALTER TABLE `wtm_member_daily_task` DISABLE KEYS */;
/*!40000 ALTER TABLE `wtm_member_daily_task` ENABLE KEYS */;


-- 导出  表 weitaomi.wtm_member_invited_record 结构
CREATE TABLE IF NOT EXISTS `wtm_member_invited_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `memberId` bigint(20) NOT NULL COMMENT '被邀请用户ID',
  `parentId` bigint(20) NOT NULL COMMENT '邀请用户',
  `isAccessible` int(11) NOT NULL COMMENT '是否有效',
  `createTime` bigint(20) DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `idx_member_invited_memberId` (`memberId`),
  KEY `idx_member_invited_parentId` (`parentId`),
  KEY `idx_member_invited_record_createTime` (`createTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户邀请记录表（二阶分销表）';

-- 正在导出表  weitaomi.wtm_member_invited_record 的数据：~0 rows (大约)
DELETE FROM `wtm_member_invited_record`;
/*!40000 ALTER TABLE `wtm_member_invited_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `wtm_member_invited_record` ENABLE KEYS */;


-- 导出  表 weitaomi.wtm_member_score 结构
CREATE TABLE IF NOT EXISTS `wtm_member_score` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `memberId` bigint(20) NOT NULL COMMENT '用户ID',
  `memberScore` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '用户总积分',
  `validScore` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '可用积分',
  `rate` decimal(11,2) NOT NULL DEFAULT '1.00' COMMENT '倍率',
  `updateTime` bigint(20) NOT NULL COMMENT '更新日期',
  `createTime` bigint(20) NOT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `idx_member_score_memberId` (`memberId`),
  KEY `idx_member_score_createTime` (`createTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户积分表用户积分';

-- 正在导出表  weitaomi.wtm_member_score 的数据：~0 rows (大约)
DELETE FROM `wtm_member_score`;
/*!40000 ALTER TABLE `wtm_member_score` DISABLE KEYS */;
/*!40000 ALTER TABLE `wtm_member_score` ENABLE KEYS */;


-- 导出  表 weitaomi.wtm_member_score_flow 结构
CREATE TABLE IF NOT EXISTS `wtm_member_score_flow` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `memberScoreId` bigint(20) NOT NULL COMMENT '用户积分ID',
  `memberId` bigint(20) NOT NULL COMMENT '用户ID',
  `typeId` bigint(20) NOT NULL DEFAULT '0' COMMENT '积分变动类型',
  `memberScoreBefore` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '用户流动前积分',
  `memberScoreAfter` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '用户流动后积分',
  `flowScore` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '流动积分',
  `detail` varchar(500) NOT NULL COMMENT '积分流动描述',
  `createTime` bigint(20) DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `idx_member_score_flow_memberId` (`memberId`),
  KEY `idx_member_score_flow_memberScoreId` (`memberScoreId`),
  KEY `idx_member_score_flow_typeId` (`typeId`),
  KEY `idx_member_score_flow_createTime` (`createTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户积分流动表';

-- 正在导出表  weitaomi.wtm_member_score_flow 的数据：~0 rows (大约)
DELETE FROM `wtm_member_score_flow`;
/*!40000 ALTER TABLE `wtm_member_score_flow` DISABLE KEYS */;
/*!40000 ALTER TABLE `wtm_member_score_flow` ENABLE KEYS */;


-- 导出  表 weitaomi.wtm_member_score_flow_type 结构
CREATE TABLE IF NOT EXISTS `wtm_member_score_flow_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `flowCount` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '积分数量',
  `typeName` varchar(50) NOT NULL COMMENT '积分流动类型',
  `typeDesc` varchar(500) NOT NULL COMMENT '积分流动类型描述',
  `createTime` bigint(20) DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `idx_member_score_flow_type_typeName` (`typeName`),
  KEY `idx_member_score_flow_type_createTime` (`createTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='积分流动类型表';

-- 正在导出表  weitaomi.wtm_member_score_flow_type 的数据：~0 rows (大约)
DELETE FROM `wtm_member_score_flow_type`;
/*!40000 ALTER TABLE `wtm_member_score_flow_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `wtm_member_score_flow_type` ENABLE KEYS */;


-- 导出  表 weitaomi.wtm_member_task_history 结构
CREATE TABLE IF NOT EXISTS `wtm_member_task_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `memberId` bigint(20) NOT NULL COMMENT '用户ID',
  `count` bigint(20) NOT NULL DEFAULT '0' COMMENT '奖励积分',
  `taskName` varchar(50) NOT NULL DEFAULT '' COMMENT '任务名称',
  `taskDesc` varchar(50) NOT NULL DEFAULT '' COMMENT '任务描述',
  `isFinished` int(11) NOT NULL DEFAULT '0' COMMENT '是否已经完成 0未完成1已完成',
  `createTime` bigint(20) DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务记录表';

-- 正在导出表  weitaomi.wtm_member_task_history 的数据：~0 rows (大约)
DELETE FROM `wtm_member_task_history`;
/*!40000 ALTER TABLE `wtm_member_task_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `wtm_member_task_history` ENABLE KEYS */;


-- 导出  表 weitaomi.wtm_office_member 结构
CREATE TABLE IF NOT EXISTS `wtm_office_member` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `officeAccountId` bigint(20) NOT NULL COMMENT '公众号ID',
  `memberId` bigint(20) NOT NULL COMMENT '会员ID',
  `isAccessNow` int(11) NOT NULL DEFAULT '0' COMMENT '当前是否还在关注  0 ：未关注， 1 ：关注',
  `createTime` bigint(20) DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `idx_office_member_memberId` (`memberId`),
  KEY `idx_office_user_createTime` (`createTime`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='本地会员关注表';

-- 正在导出表  weitaomi.wtm_office_member 的数据：~1 rows (大约)
DELETE FROM `wtm_office_member`;
/*!40000 ALTER TABLE `wtm_office_member` DISABLE KEYS */;
INSERT INTO `wtm_office_member` (`id`, `officeAccountId`, `memberId`, `isAccessNow`, `createTime`) VALUES
  (1, 1, 1, 0, NULL);
/*!40000 ALTER TABLE `wtm_office_member` ENABLE KEYS */;


-- 导出  表 weitaomi.wtm_office_user 结构
CREATE TABLE IF NOT EXISTS `wtm_office_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `officeAccountId` bigint(20) NOT NULL COMMENT '公众号ID',
  `unionId` varchar(50) DEFAULT '' COMMENT '用户唯一识别标识',
  `openId` varchar(50) DEFAULT '' COMMENT '公号分配给用户的ID',
  `nickname` varchar(50) DEFAULT '' COMMENT '昵称',
  `createTime` bigint(20) DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `idx_office_user_openId` (`openId`),
  KEY `idx_office_user_createTime` (`createTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='公号关注表';

-- 正在导出表  weitaomi.wtm_office_user 的数据：~0 rows (大约)
DELETE FROM `wtm_office_user`;
/*!40000 ALTER TABLE `wtm_office_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `wtm_office_user` ENABLE KEYS */;


-- 导出  表 weitaomi.wtm_official_accounts 结构
CREATE TABLE IF NOT EXISTS `wtm_official_accounts` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sellerTypeId` int(11) NOT NULL COMMENT '商家类型',
  `memberId` bigint(20) NOT NULL COMMENT '用户ID',
  `isOpen` int(11) NOT NULL DEFAULT '0' COMMENT '是否开通 0：未开通，1：开通',
  `appId` varchar(120) NOT NULL COMMENT '公众号appId',
  `appSecret` varchar(120) NOT NULL COMMENT '公众号appsercret',
  `openId` varchar(120) NOT NULL COMMENT '商家开通授权ID',
  `originId` varchar(120) NOT NULL COMMENT '公众号原始ID',
  `accessToken` varchar(120) NOT NULL COMMENT '公众号第三方token',
  `tokenUpdateTime` bigint(20) DEFAULT NULL COMMENT 'token更新时间',
  `userName` varchar(120) NOT NULL COMMENT '商家名称',
  `accountNumber` int(11) DEFAULT NULL COMMENT '粉丝数量',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注信息',
  `addUrl` varchar(500) NOT NULL COMMENT '关注地址',
  `imageUrl` varchar(500) NOT NULL COMMENT '头像地址',
  `createTime` bigint(20) DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `idx_openId` (`openId`),
  KEY `idx_userName` (`userName`),
  KEY `idx_user_createTime` (`createTime`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='商家开通记录表';

-- 正在导出表  weitaomi.wtm_official_accounts 的数据：~2 rows (大约)
DELETE FROM `wtm_official_accounts`;
/*!40000 ALTER TABLE `wtm_official_accounts` DISABLE KEYS */;
INSERT INTO `wtm_official_accounts` (`id`, `sellerTypeId`, `memberId`, `isOpen`, `appId`, `appSecret`, `openId`, `originId`, `accessToken`, `tokenUpdateTime`, `userName`, `accountNumber`, `remark`, `addUrl`, `imageUrl`, `createTime`) VALUES
  (1, 1, 0, 0, '', '', '', '', '', NULL, '', NULL, NULL, '', '', 1467949235),
  (2, 1, 0, 0, '', '', '', '', '', NULL, '', NULL, NULL, '', '', 1467949235);
/*!40000 ALTER TABLE `wtm_official_accounts` ENABLE KEYS */;


-- 导出  表 weitaomi.wtm_payment_approve 结构
CREATE TABLE IF NOT EXISTS `wtm_payment_approve` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `memberId` bigint(20) NOT NULL COMMENT '用户ID',
  `accountNumber` varchar(50) NOT NULL COMMENT '收款账号',
  `accountName` varchar(50) NOT NULL COMMENT '收款人姓名',
  `amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '提现金额',
  `remark` varchar(500) NOT NULL COMMENT '备注',
  `isPaid` int(11) DEFAULT '0' COMMENT '是否提现成功标识位 0：未成功',
  `createTime` bigint(20) DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `idx_payment_approve_memberId` (`memberId`),
  KEY `idx_payment_approve_createTime` (`createTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户提现申请表';

-- 正在导出表  weitaomi.wtm_payment_approve 的数据：~0 rows (大约)
DELETE FROM `wtm_payment_approve`;
/*!40000 ALTER TABLE `wtm_payment_approve` DISABLE KEYS */;
/*!40000 ALTER TABLE `wtm_payment_approve` ENABLE KEYS */;


-- 导出  表 weitaomi.wtm_payment_history 结构
CREATE TABLE IF NOT EXISTS `wtm_payment_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `payCode` varchar(16) NOT NULL COMMENT '支付单号',
  `memberId` bigint(20) NOT NULL COMMENT '用户ID',
  `platform` varchar(20) NOT NULL COMMENT '支付平台',
  `params` varchar(500) NOT NULL COMMENT '请求参数',
  `isPaySuccess` int(11) NOT NULL DEFAULT '0' COMMENT '是否支付成功 0:未成功，1成功',
  `serialNumber` int(32) DEFAULT NULL COMMENT '交易流水号',
  `payType` int(32) NOT NULL DEFAULT '0' COMMENT '交易类型，0：付款给商家，1：付款给用户',
  `batchPayNo` varchar(50) DEFAULT NULL COMMENT '批量付款批次号，payType=1时 使用',
  `createTime` bigint(20) DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `idx_payment_history_memberId` (`memberId`),
  KEY `idx_payment_history_createTime` (`createTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='支付流水历史表';

-- 正在导出表  weitaomi.wtm_payment_history 的数据：~0 rows (大约)
DELETE FROM `wtm_payment_history`;
/*!40000 ALTER TABLE `wtm_payment_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `wtm_payment_history` ENABLE KEYS */;


-- 导出  表 weitaomi.wtm_seller_type 结构
CREATE TABLE IF NOT EXISTS `wtm_seller_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `typeCode` int(11) NOT NULL COMMENT '商家类型',
  `userTypeName` varchar(50) NOT NULL COMMENT '商家类型名称',
  `createTime` bigint(20) DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `idx_typeCode` (`typeCode`),
  KEY `idx_userTypeName` (`userTypeName`),
  KEY `idx_user_typecreateTime` (`createTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商家类型记录表';

-- 正在导出表  weitaomi.wtm_seller_type 的数据：~0 rows (大约)
DELETE FROM `wtm_seller_type`;
/*!40000 ALTER TABLE `wtm_seller_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `wtm_seller_type` ENABLE KEYS */;


-- 导出  表 weitaomi.wtm_third_login 结构
CREATE TABLE IF NOT EXISTS `wtm_third_login` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `openId` varchar(120) NOT NULL COMMENT '开放平台ID',
  `unionId` varchar(120) NOT NULL COMMENT '用户唯一识别ID',
  `memberId` bigint(20) NOT NULL COMMENT '会员ID',
  `type` int(11) NOT NULL COMMENT '类型 0：微信，1：支付宝，2：QQ',
  `nickname` varchar(20) DEFAULT NULL COMMENT '昵称',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `createTime` bigint(20) DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `idx_openId` (`openId`),
  KEY `idx_nickName` (`nickname`),
  KEY `idx_memberId` (`memberId`),
  KEY `idx_third_createTime` (`createTime`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='第三方平台登陆表';

-- 正在导出表  weitaomi.wtm_third_login 的数据：~2 rows (大约)
DELETE FROM `wtm_third_login`;
/*!40000 ALTER TABLE `wtm_third_login` DISABLE KEYS */;
INSERT INTO `wtm_third_login` (`id`, `openId`, `unionId`, `memberId`, `type`, `nickname`, `remark`, `createTime`) VALUES
  (1, 'oKfbJvp1_Bb-Xy1aqw6o4L5H55Y0', 'oaPViwbX7W-ddS5BKHk7PtSZxHDM', 7, 0, 'loyal', '', 1471143130),
  (2, 'oKfbJvm9HKiUBP3l0lk9gQzKmSSs', 'oaPViwWITU6kY8kxu_Y1AAFY6YSs', 14, 0, 'soder', '哈希骂死', 1471163689);
/*!40000 ALTER TABLE `wtm_third_login` ENABLE KEYS */;


-- 导出  表 weitaomi.wtm_weitaomi_official_member 结构
CREATE TABLE IF NOT EXISTS `wtm_weitaomi_official_member` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `memberId` bigint(20) NOT NULL COMMENT '会员ID',
  `unionId` varchar(50) NOT NULL DEFAULT '' COMMENT '用户唯一识别标识',
  `openId` varchar(50) NOT NULL DEFAULT '' COMMENT '公号分配给用户的ID',
  `nickname` varchar(50) NOT NULL DEFAULT '' COMMENT '昵称',
  `followTime` bigint(20) NOT NULL COMMENT '关注时间',
  `createTime` bigint(20) DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`),
  KEY `idx_weitaomi_official_member` (`unionId`),
  KEY `idx_weitaomi_official_createTime` (`createTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='微淘米公众号关注';

-- 正在导出表  weitaomi.wtm_weitaomi_official_member 的数据：~0 rows (大约)
DELETE FROM `wtm_weitaomi_official_member`;
/*!40000 ALTER TABLE `wtm_weitaomi_official_member` DISABLE KEYS */;
/*!40000 ALTER TABLE `wtm_weitaomi_official_member` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
