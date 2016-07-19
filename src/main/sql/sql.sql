show databases;
CREATE DATABASE weitaomi;
use weitaomi;
/**测试用数据表**/
CREATE TABLE seckill(
  `seckill_id` bigint NOT NULL Auto_increment Comment 'id',
  `goodsName` VARCHAR(200) NOT NULL Comment '商品名称',
  `goodsNum` int not null comment '商品数量',
  `start_time` TIMESTAMP not NULL comment '秒杀开始时间',
  `end_time` TIMESTAMP not NULL comment '秒杀结束时间',
  `create_time` TIMESTAMP not NULL comment '时间',
  PRIMARY KEY  (seckill_id),
  KEY idx_start_time(start_time),
  KEY idx_end_time(end_time),
  KEY idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=UTF8 comment '秒杀库存表';
show TABLES ;

CREATE TABLE success_killed(
  `success_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `seckill_id` BIGINT NOT NULL COMMENT '秒杀产品ID',
  `user_phone` BIGINT NOT NULL COMMENT '用户手机号',
  `state` TINYINT NOT NULL DEFAULT -1 COMMENT '秒杀状态 -1：秒杀无效，0：秒杀成功，1：已付款',
  `create_time` TIMESTAMP NOT NULL  COMMENT '秒杀时间',
  PRIMARY KEY (success_id),
  key idx_create_time(create_time)
)ENGINE =InnoDB DEFAULT CHARSET =UTF8 COMMENT '秒杀记录表';


/*微淘米数据表*/
/*会员记录表*/
CREATE TABLE wtm_member(
  `member_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `memberName` VARCHAR(120) NOT NULL COMMENT '会员名称',
  `nickName` VARCHAR(120) NOT NULL COMMENT '会员昵称',
  `telephone` INT(11) NOT NULL COMMENT '电话',
  `salt` VARCHAR(50) NOT NULL COMMENT '加密盐',
  `password` VARCHAR(200) NOT NULL COMMENT '密码',
  `birth` BIGINT(20) COMMENT '会员生日',
  `email` VARCHAR(50) COMMENT '会员邮箱',
  `invitedCode` VARCHAR(20) COMMENT '邀请码',
  `imageUrl` VARCHAR(500) COMMENT '头像url',
  `createTime` BIGINT(20) COMMENT '创建日期',
  PRIMARY KEY (member_id),
  key idx_memberName (memberName),
  KEY idx_nickName(nickName),
  KEY idx_member_telephone(telephone),
  key idx_member_email(email),
  key idx_member_createTime(createTime)
)ENGINE=InnoDB  DEFAULT CHARSET=UTF8 comment '会员注册表';

/*第三方登陆记录表*/
CREATE TABLE wtm_third_login(
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `openId` VARCHAR(120) NOT NULL  COMMENT '开放平台ID',
  `memberId` INT(11) NOT NULL COMMENT '会员ID',
  `type` INT (11) NOT NULL COMMENT '类型 0：微信，1：支付宝，2：QQ',
  `nickname` VARCHAR(20) COMMENT '昵称',
  `remark` VARCHAR(500) COMMENT '备注',
  `createTime` BIGINT(20) COMMENT '创建日期',
  PRIMARY KEY (id),
  key idx_openId (openId),
  KEY idx_nickName(nickname),
  KEY idx_memberId(memberId),
  key idx_third_createTime(createTime)
)ENGINE=InnoDB  DEFAULT CHARSET=UTF8 comment '第三方平台登陆表';


/*商家开通记录表*/
CREATE TABLE wtm_user(
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `typeCode` INT(11) NOT NULL COMMENT '商家类型',
  `isOpen` INT(11) NOT NULL DEFAULT 0 COMMENT '是否开通 0：未开通，1：开通',
  `openId` VARCHAR(120) NOT NULL COMMENT '商家开通授权ID',
  `userName` VARCHAR(120) NOT NULL COMMENT '商家名称',
  `accountNumber` INT(11)  COMMENT '粉丝数量',
  `remark`  VARCHAR(500) COMMENT '备注信息',
  `createTime` BIGINT(20) COMMENT '创建日期',
  PRIMARY KEY (id),
  key idx_openId (openId),
  KEY idx_userName(userName),
  key idx_user_createTime(createTime)
)ENGINE=InnoDB  DEFAULT CHARSET=UTF8 comment '商家开通记录表';

/*商家类型记录表*/
CREATE TABLE wtm_user_type(
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `typeCode` INT(11) NOT NULL COMMENT '商家类型',
  `userTypeName` VARCHAR(50) NOT NULL COMMENT '商家类型名称',
  `createTime` BIGINT(20) COMMENT '创建日期',
  PRIMARY KEY (id),
  KEY idx_typeCode (typeCode),
  KEY idx_userTypeName(userTypeName),
  key idx_user_type_createTime(createTime)
)ENGINE=InnoDB  DEFAULT CHARSET=UTF8 comment '商家类型记录表';

/*文章列表*/
CREATE TABLE wtm_article(
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userId` BIGINT NOT NULL COMMENT '商家ID',
  `url` VARCHAR(500) NOT NULL COMMENT '文章地址',
  `title` VARCHAR(500) NOT NULL COMMENT '文章标题',
  `abstract` VARCHAR(500) NOT NULL COMMENT '文章摘要',
  `readNumber` INT(11)  COMMENT '总阅读数',
  `agreeNumber`  INT(11)  COMMENT '点赞数',
  `agreeIncreaseNumber` INT(11) NOT NULL DEFAULT 0 COMMENT '通过该平台点赞增长数',
  `readIncreaseNumber` INT(11) NOT NULL DEFAULT 0 COMMENT '通过该平台阅读增长数',
  `validNumber` INT(11)  DEFAULT 0 COMMENT '有效阅读数',
  `isTop` INT(11) NOT NULL DEFAULT 0 COMMENT '是否置顶 0：不置顶，1：置顶',
  `createTime` BIGINT(20) COMMENT '创建日期',
  PRIMARY KEY (id),
  KEY idx_userId(userId),
  KEY idx_title(title),
  KEY idx_isTop(isTop),
  KEY idx_createTime(createTime)
)ENGINE=InnoDB  DEFAULT CHARSET=UTF8 comment '文章列表';

CREATE TABLE wtm_article_read_record (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `memberId` BIGINT NOT NULL COMMENT '用户ID',
  `articleId` BIGINT NOT NULL COMMENT '文章ID',
  `createTime` BIGINT(20) COMMENT '创建日期',
  PRIMARY KEY (id),
  KEY idx_article_read_record_memberId(memberId),
  KEY idx_article_read_record_articleId(articleId)
)ENGINE=InnoDB  DEFAULT CHARSET=UTF8 comment '用户阅读记录表';

/*用户积分*/
CREATE TABLE wtm_member_score(
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `memberId` BIGINT NOT NULL COMMENT '用户ID',
  `memberScore` INT(11) NOT NULL DEFAULT 0 COMMENT '用户总积分',
  `validScore` INT(11) NOT NULL DEFAULT 0 COMMENT '可用积分',
  `createTime` BIGINT(20) COMMENT '创建日期',
  PRIMARY KEY (id),
  KEY idx_member_score_memberId(memberId),
  KEY idx_member_score_createTime(createTime)
)ENGINE=InnoDB  DEFAULT CHARSET=UTF8 comment '用户积分表用户积分';


/*用户积分流动表*/
CREATE TABLE wtm_member_score_flow(
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `memberScoreId` BIGINT NOT NULL COMMENT '用户积分ID',
  `memberId` BIGINT NOT NULL COMMENT '用户ID',
  `typeId` INT(11) NOT NULL DEFAULT 0 COMMENT '积分变动类型',
  `memberScoreBefore` INT(11) NOT NULL DEFAULT 0 COMMENT '用户流动前积分',
  `memberScoreAfter` INT(11) NOT NULL DEFAULT 0 COMMENT '用户流动后积分',
  `flowScore` INT(11) NOT NULL DEFAULT 0 COMMENT '流动积分',
  `detail` VARCHAR(500) NOT NULL COMMENT '积分流动描述',
  `createTime` BIGINT(20) COMMENT '创建日期',
  PRIMARY KEY (id),
  KEY idx_member_score_flow_memberId(memberId),
  KEY idx_member_score_flow_memberScoreId(memberScoreId),
  KEY idx_member_score_flow_typeId(typeId),
  KEY idx_member_score_flow_createTime(createTime)
)ENGINE=InnoDB  DEFAULT CHARSET=UTF8 comment '用户积分流动表';


/*积分流动类型表*/
CREATE TABLE wtm_member_score_flow_type(
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `typeName` VARCHAR(50) NOT NULL COMMENT '积分流动类型',
  `typeDesc` VARCHAR(500) NOT NULL COMMENT '积分流动类型描述',
  `createTime` BIGINT(20) COMMENT '创建日期',
  PRIMARY KEY (id),
  KEY idx_member_score_flow_type_typeName(typeName),
  KEY idx_member_score_flow_type_createTime(createTime)
)ENGINE=InnoDB  DEFAULT CHARSET=UTF8 comment '积分流动类型表';


/*用户邀请记录表（二阶分销表）*/
CREATE TABLE wtm_member_invited_record(
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `memberId` BIGINT NOT NULL COMMENT '被邀请用户ID',
  `parentId` BIGINT NOT NULL COMMENT '邀请用户',
  `createTime` BIGINT(20) COMMENT '创建日期',
  PRIMARY KEY (id),
  KEY idx_member_invited_memberId(memberId),
  KEY idx_member_invited_parentId(parentId),
  KEY idx_member_invited_record_createTime(createTime)
)ENGINE=InnoDB  DEFAULT CHARSET=UTF8 comment '用户邀请记录表（二阶分销表）';

