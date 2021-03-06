package com.weitaomi.application.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weitaomi.application.model.bean.*;
import com.weitaomi.application.model.dto.*;
import com.weitaomi.application.model.mapper.*;
import com.weitaomi.application.service.interf.*;
import com.weitaomi.systemconfig.constant.SystemConfig;
import com.weitaomi.systemconfig.exception.BusinessException;
import com.weitaomi.systemconfig.exception.InfoException;
import com.weitaomi.systemconfig.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by supumall on 2016/8/9.
 */
@Service
public class OfficeAccountService extends BaseService implements IOfficeAccountService {
    private static Logger logger = LoggerFactory.getLogger(OfficeAccountService.class);
    @Autowired
    private OfficalAccountMapper officalAccountMapper;
    @Autowired
    private MemberTaskHistoryMapper memberTaskHistoryMapper;
    @Autowired
    private WtmOfficialMemberMapper wtmOfficialMemberMapper;
    @Autowired
    private ThirdLoginMapper thirdLoginMapper;
    @Autowired
    private MemberTaskMapper memberTaskMapper;
    @Autowired
    private ICacheService cacheService;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private TaskPoolMapper taskPoolMapper;
    @Autowired
    private OfficeMemberMapper officeMemberMapper;
    @Autowired
    private IMemberScoreService memberScoreService;
    @Autowired
    private IMemberTaskHistoryService memberTaskHistoryService;
    @Autowired
    private AccountAdsMapper accountAdsMapper;
    @Autowired
    private TaskFailPushToWechatMapper taskFailPushToWechatMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 查看已关注公众号
     */
    @Override
    public List<MemberAccountLabel> getOfficialAccountMsgList(Long memberId, Integer sourceType) {
        List<MemberAccountLabel> officialAccountNameList = officeMemberMapper.getOfficialAccountNameList(memberId, sourceType);
        return officialAccountNameList;
    }

    /**
     * 更新已关注公众号
     */
    @Override
    public Integer signOfficialAccountMsgList(Long memberId, Integer sourceType) {
        List<MemberAccountLabel> officialAccountNameList = officeMemberMapper.getOfficialAccountNameList(memberId, sourceType);
        Integer num = officeMemberMapper.updateOfficialMemberList(memberId);
        for (MemberAccountLabel memberAccountLabel : officialAccountNameList) {
            String key = null;
            try {
                key = Base64Utils.encodeToString(memberAccountLabel.getNickName().getBytes("UTF-8")) + ":" + memberAccountLabel.getSex() + ":" + memberAccountLabel.getOriginId();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            logger.info("用户执行标注公众号操作，key：{}", key);
            int number = memberTaskHistoryMapper.deleteMemberTaskUnfinished(memberId, 0, memberAccountLabel.getOriginId());
            cacheService.delKeyFromRedis(key);
        }
        return num;
    }

    @Override
    @Transactional
    public boolean pushAddRequest(Long memberId, AddOfficalAccountDto addOfficalAccountDto) {
        logger.info("app用户ID：{}开始拉取公众号列表，参数为：{}", memberId, JSON.toJSONString(addOfficalAccountDto));
        List<OfficeMember> officeMembers = officeMemberMapper.getOfficeMemberList(memberId);
        if (!officeMembers.isEmpty()) {
            String info = "您还有未关注公众号，请到公众号内完成\n未完成任务将会在1小时后删除\n若领任务之前已关注，请标注这些公众号";
            throw new InfoException(info);
        }
        if (addOfficalAccountDto == null) {
            throw new BusinessException("任务列表为空");
        }
        String unionId = addOfficalAccountDto.getUnionId();
        if (StringUtil.isEmpty(unionId)) {
            throw new BusinessException("用户唯一识别码为空");
        }
        List<OfficialAccountMsg> list = addOfficalAccountDto.getLinkList();
        if (list.isEmpty()) {
            throw new BusinessException("要关注的公号列表为空");
        }
        return this.dealWithAddRequest(memberId,unionId,list);
    }
    @Transactional
    private  boolean dealWithAddRequest(Long memberId, String unionId, List<OfficialAccountMsg> officialAccountMsgList){
        List<OfficeMember> officeMemberList = new ArrayList<>();
        List<Long> idList = new ArrayList<>();
        for (OfficialAccountMsg officialAccountMsg : officialAccountMsgList) {
            ThirdLoginDto thirdLoginDto = thirdLoginMapper.getThirdlogInDtoMemberId(memberId, 0);
            String nickName = null;
            try {
                nickName = Base64Utils.encodeToString(thirdLoginDto.getNickname().getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String key = nickName + ":" + thirdLoginDto.getSex() + ":" + officialAccountMsg.getOriginId();
            logger.info("key is {}", key);
            OfficialAccountWithScore officialAccountWithScore = officalAccountMapper.getOfficialAccountWithScoreById(officialAccountMsg.getOriginId());
            TaskPool taskPool = taskPoolMapper.getTaskPoolByOfficialId(officialAccountWithScore.getId(), 1);
            //添加到待完成任务记录中
            if (taskPool != null) {
                String tableName = "follow:account:taskPoolId:" + taskPool.getId();
                Integer number = cacheService.getCacheByKey(tableName, Integer.class);
                boolean flag = true;
                if (number != null && number > 0) {
                    if (number >= taskPool.getNeedNumber()) {
                        flag = false;
                    }
                } else {
                    cacheService.setCacheByKey(tableName, 1, null);
                    number = 0;
                }
                Double score = taskPool.getTotalScore() - taskPool.getSingleScore();
                if (flag) {
                    if (officialAccountWithScore != null && score >= 0) {
                        MemberCheck memberCheck = null;
                        if (officialAccountWithScore != null) {
                            memberCheck = new MemberCheck(memberId, officialAccountWithScore.getId(), taskPool.getId());
                        }
                        MemberCheck valueTemp = cacheService.getCacheByKey(key, MemberCheck.class);
                        if (valueTemp != null) {
                            throw new InfoException("公众号" + officialAccountWithScore.getUserName() + "的关注未完成，请先完成");
                        }
                        Integer numberFlag = taskPool.getNeedNumber() - BigDecimal.valueOf(taskPool.getTotalScore()).divide(BigDecimal.valueOf(taskPool.getSingleScore()),1,BigDecimal.ROUND_HALF_UP).intValue();
                        if (numberFlag.intValue() != number.intValue()) {
                            logger.warn("发出警告!!!!关注公众号领取人数与米币消费数量不相等，公众号Id：{}，本次不相等数量缓存：{}，实际：{}", taskPool.getOfficialAccountsId(), number, numberFlag);
                        }
                        OfficeMember officeMember = new OfficeMember();
                        officeMember.setMemberId(memberId);
                        officeMember.setTaskPoolId(taskPool.getId());
                        officeMember.setOfficeAccountId(officialAccountWithScore.getId());
                        officeMember.setIsAccessNow(0);
                        String detail = "关注公众号" + officialAccountWithScore.getUserName() + "，领取" + taskPool.getFinishScore() + "米币";
                        if (score >= taskPool.getSingleScore()) {
                            Integer num = taskPoolMapper.updateTaskPoolWithScore(score.doubleValue(), taskPool.getId());
                            if (num>0){
                                memberTaskHistoryService.addMemberTaskToHistory(memberId, 1L, taskPool.getFinishScore(), 0, detail, null, officialAccountMsg.getOriginId());
                                officeMemberList.add(officeMember);
                                cacheService.setCacheByKey(key, memberCheck, SystemConfig.TASK_CACHE_TIME);
                                idList.add(officialAccountWithScore.getId());
                                cacheService.increCacheBykey(tableName, num.longValue());
                            }else {
                                continue;
                            }
                        } else {
                            taskPool.setTotalScore(0D);
                            taskPool.setIsPublishNow(0);
                            taskPool.setLimitDay(0L);
                            taskPoolMapper.updateByPrimaryKeySelective(taskPool);
                            memberScoreService.addMemberScore(officialAccountWithScore.getMemberId(), 6L, 1, score.doubleValue(), UUIDGenerator.generate());
                            JpushUtils.buildRequest("公众号" + officialAccountWithScore.getUserName() + "的关注任务领取完成，若用户未关注，米币将在凌晨4点返回到您的账户", officialAccountWithScore.getMemberId());
                        }
                    }
                }else{
                    taskPool.setTotalScore(0D);
                    taskPool.setIsPublishNow(0);
                    taskPool.setLimitDay(0L);
                    taskPoolMapper.updateByPrimaryKeySelective(taskPool);
                    memberScoreService.addMemberScore(officialAccountWithScore.getMemberId(), 6L, 1, score.doubleValue(), UUIDGenerator.generate());
                    JpushUtils.buildRequest("公众号" + officialAccountWithScore.getUserName() + "的关注任务领取完成，若用户未关注，米币将在凌晨4点返回到您的账户", officialAccountWithScore.getMemberId());
                }
            }
        }
        Long time = DateUtils.getUnixTimestamp();
        if (!officeMemberList.isEmpty()&&!idList.isEmpty()) {
            int number = officeMemberMapper.batchAddOfficeMember(officeMemberList, time);
            if (number>0){
                Map map=new HashMap();
                map.put("idList",idList);
                map.put("unionId",unionId);
                amqpTemplate.convertAndSend("addOfficialAccount."+time,JSON.toJSONString(map));
                return true;
            }
        }else {
            return false;
        }
        return false;
    }
    @Override
    @Transactional
    public Boolean pushAddFinished(Map<String, Object> params) {
        String openid = "";
        if (params.get("openid") != null) {
            openid = (String) params.get("openid").toString();
        }
        String nickname = "";
        if (params.get("nickname") != null) {
            nickname = params.get("nickname").toString();
        }
        String sexString = "";
        if (params.get("sex") != null) {
            sexString = params.get("sex").toString();
        }
        String originId = "";
        if (params.get("originId") != null) {
            originId = params.get("originId").toString();
        }
        Integer flag = Integer.valueOf(params.get("flag").toString());
        logger.info("openId:{},nickname:{},sex:{},originId:{},flag:{}", openid, nickname, sexString, originId, flag);
        if (flag == 0) {
            OfficialAccountWithScore officialAccountWithScore = officalAccountMapper.getOfficialAccountWithScoreById(originId);
            if (officialAccountWithScore != null) {
                OfficeMember officeMember = officeMemberMapper.getOfficeMemberByOpenId(openid);
                if (officeMember == null) {
                    logger.info("没有此用户记录");
                    return true;
                }
                //增加积分以及积分记录
                Long memberId = officeMember.getMemberId();
                int num = officeMemberMapper.deleteFollowAccountsMember(officeMember.getId());
                if (num > 0&&officeMember.getIsScoreAccess()!=2) {
                    if (DateUtils.getUnixTimestamp() - officeMember.getCreateTime() < 7 * 24 * 60 * 60) {
                        TaskPool taskPool=null;
                        if (officeMember.getTaskPoolId()!=null) {
                            taskPool = taskPoolMapper.selectByPrimaryKey(officeMember.getTaskPoolId());
                        }else {
                            taskPool = taskPoolMapper.getTaskPoolByOfficialId(officialAccountWithScore.getId(), null);
                        }
                        memberScoreService.addMemberScore(memberId, 7L, 1, -taskPool.getFinishScore(), UUIDGenerator.generate());
                        memberTaskHistoryService.addMemberTaskToHistory(memberId, 11L, -taskPool.getFinishScore(), 1, "七天之内取消关注公众号" + officialAccountWithScore.getUserName(), null, null);
                        memberScoreService.addMemberScore(officialAccountWithScore.getMemberId(), 9L, 1, taskPool.getSingleScore(), UUIDGenerator.generate());
                        memberTaskHistoryService.addMemberTaskToHistory(officialAccountWithScore.getMemberId(), 12L, taskPool.getSingleScore(), 1, "用户七天之内取消关注公众号" + officialAccountWithScore.getUserName() + ",米币退还给公众号商家", null, null);
                        logger.info("普通用户ID为{}的用户米币扣除成功，米币数为{}，商户用户ID为{}的用户米币返还成功，米币数为{}", memberId, -taskPool.getFinishScore(), officialAccountWithScore.getMemberId(), taskPool.getSingleScore());
                        return true;
                    }
                }
            } else {
                return true;
            }
        }
        if (flag == 1) {
            OfficialAccountWithScore officialAccountWithScore = officalAccountMapper.getOfficialAccountWithScoreById(originId);
            if (officialAccountWithScore != null) {
                String key = nickname + ":" + sexString + ":" + originId;
                logger.info("key is {}", key);
                MemberCheck memberCheck = cacheService.getCacheByKey(key, MemberCheck.class);
                logger.info("获取到的关注数据为:{}", JSON.toJSONString(memberCheck));
                if (memberCheck != null) {
                    Long officeAccountId = Long.valueOf(memberCheck.getOfficialAccountsId());
                    Long taskPoolId = Long.valueOf(memberCheck.getTaskPoolId());
                    TaskPool taskPool = taskPoolMapper.selectByPrimaryKey(taskPoolId);
                    Double score = taskPool.getSingleScore();
                    Long memberId = memberCheck.getMemberId();
                    if (taskPool == null) {
                        JpushUtils.buildRequest("您关注的公众号任务：" + officialAccountWithScore.getUserName() + "不存在", memberId);
                        return true;
                    }
                    //添加到公众号关注表中
                    OfficeMember officeMember = officeMemberMapper.getOfficeMember(memberId, officeAccountId);
                    logger.info("关注记录表:{}", JSON.toJSONString(officeMember));
                    if (officeMember == null) {
                        JpushUtils.buildRequest("您关注的公众号任务：" + officialAccountWithScore.getUserName() + "，不存在或者已经结束", memberId);
                        return true;
                    }
                    if (officeMember.getIsAccessNow() == 1) {
                        JpushUtils.buildRequest("您已关注过公众号：" + officialAccountWithScore.getUserName() + "，该任务失效", memberId);
                        return true;
                    }
                    officeMember.setIsAccessNow(1);
                    officeMember.setOpenId(openid);
                    officeMember.setAddRewarScore(BigDecimal.valueOf(taskPool.getFinishScore()));
                    officeMember.setFinishedTime(DateUtils.getUnixTimestamp());
                    if (taskPool.getRealityNumber() < taskPool.getNeedNumber()) {
                        //增加任务记录
                        logger.info("增加任务记录");
                        int number = memberTaskHistoryMapper.updateMemberTaskUnfinished(memberId, 0, originId);
                        //增加积分以及积分记录
                        logger.info("ID为：{}用户,增加积分以及积分记录：{}", memberId, taskPool.getFinishScore());
                        memberScoreService.addMemberScore(memberId, 11L, 1, taskPool.getFinishScore(), UUIDGenerator.generate());
                        cacheService.delKeyFromRedis(key);
                        officeMemberMapper.updateByPrimaryKeySelective(officeMember);
                        taskPool.setRealityNumber(taskPool.getRealityNumber()+1);
                        taskPoolMapper.updateByPrimaryKeySelective(taskPool);
                        return true;
                    } else {
                        //增加任务记录
                        logger.info("增加任务记录");
                        int number = memberTaskHistoryMapper.updateMemberTaskUnfinished(memberId, 0, originId);
                        JpushUtils.buildRequest("公众号" + officialAccountWithScore.getUserName() + "的关注任务已经完成啦~去完成其他任务吧", memberId);
                        logger.info("公众号：{}的关注任务已经完成，memberID：{}", officialAccountWithScore.getUserName(), memberId);
                        cacheService.delKeyFromRedis(key);
                        officeMember.setIsScoreAccess(2);
                        officeMemberMapper.updateByPrimaryKeySelective(officeMember);
                        return true;
                    }
                } else {
                    logger.info("获取待关注缓存信息失败，redis key is {}", key);
                    return true;
                }
            } else {
                logger.info("获取要关注的公众号失败");
                String key = nickname + ":" + sexString + ":" + originId;
                cacheService.delKeyFromRedis(key);
                return true;
            }
        }
        return true;
    }


    @Override
    @Transactional
    public boolean markAddRecord(Long memberId, OfficialAccountMsg officialAccountMsg) {
//        logger.info("wap用户ID：{}开始拉取公众号列表，参数为：{}", memberId, JSON.toJSONString(officialAccountMsg));
//        List<OfficeMember> officeMembers = officeMemberMapper.getOfficeMemberList(memberId);
//        if (!officeMembers.isEmpty()) {
//            String info = "您还有未关注公众号，请到公众号内完成\n未完成任务将会在1小时后删除\n若领任务之前已关注，请标注这些公众号";
//            throw new InfoException(info);
//        }
//        ThirdLogin thirdLogin = thirdLoginMapper.getUnionIdByMemberId(memberId, 1);
//        if (thirdLogin == null || StringUtil.isEmpty(thirdLogin.getUnionId())) {
//            throw new InfoException("用户唯一识别码为空");
//        }
//        if (officialAccountMsg == null) {
//            throw new InfoException("要关注的公号列表为空");
//        }
//        List<OfficeMember> officeMemberList = new ArrayList<>();
//        List<Long> idList = new ArrayList<>();
//        ThirdLoginDto thirdLoginDto = thirdLoginMapper.getThirdlogInDtoMemberId(memberId, 1);
//        String key = null;
//        try {
//            key = Base64Utils.encodeToString(thirdLoginDto.getNickname().getBytes("UTF-8")) + ":" + thirdLoginDto.getSex() + ":" + officialAccountMsg.getOriginId();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        logger.info("key is {}", key);
//        OfficialAccountWithScore officialAccountWithScore = officalAccountMapper.getOfficialAccountWithScoreById(officialAccountMsg.getOriginId(), 1);
//        MemberCheck memberCheck = new MemberCheck(memberId, officialAccountWithScore.getId());
//        MemberCheck valueTemp = cacheService.getCacheByKey(key, MemberCheck.class);
//        if (valueTemp != null) {
//            throw new InfoException("公众号" + officialAccountWithScore.getUserName() + "的关注未完成，请先完成");
//        }
//        cacheService.setCacheByKey(key, memberCheck, SystemConfig.TASK_CACHE_TIME);
//        OfficeMember officeMember = new OfficeMember();
//        officeMember.setMemberId(memberId);
//        officeMember.setOfficeAccountId(officialAccountWithScore.getId());
//        officeMember.setIsAccessNow(0);
//        officeMemberList.add(officeMember);
//        TaskPool taskPool = taskPoolMapper.getTaskPoolByOfficialId(officialAccountWithScore.getId(), 1);
//        //添加到待完成任务记录中
//        String detail = "关注公众号" + officialAccountWithScore.getUserName() + "，领取" + taskPool.getRate().multiply(BigDecimal.valueOf(officialAccountWithScore.getScore())).doubleValue() + "米币";
//        memberTaskHistoryService.addMemberTaskToHistory(memberId, 1L, taskPool.getRate().multiply(BigDecimal.valueOf(officialAccountWithScore.getScore())).doubleValue(), 0, detail, null, officialAccountMsg.getOriginId());
//        idList.add(officialAccountWithScore.getId());
//        Long time = DateUtils.getUnixTimestamp();
//        int number = officeMemberMapper.batchAddOfficeMember(officeMemberList, time);
//        return number > 0 ? true : false;
        return false;
    }

    @Override
    public List<OfficialAccountMsg> getOfficialAccountMsg(Long memberId, String unionId, Integer sourceType) {
        Member member = memberMapper.selectByPrimaryKey(memberId);
        if (member == null) {
            throw new InfoException("用户信息为空");
        }
        List<OfficialAccountMsg> officialAccountMsgs = officalAccountMapper.getOfficialAccountMsg(memberId, unionId, member.getSex(), member.getProvince(), member.getCity());
        if (officialAccountMsgs.isEmpty()) {
            return null;
        }
        return officialAccountMsgs;
    }

    @Override
    public List<OfficialAccount> getOfficialAccountList(Long memberId) {
        return officalAccountMapper.getOfficialAccountList(memberId);
    }

    @Override
    public boolean updateOfficialAccountList(Long accountId, Integer isOpen) {
        OfficialAccount officialAccount = officalAccountMapper.selectByPrimaryKey(accountId);
        officialAccount.setIsOpen(isOpen);
        return officalAccountMapper.updateByPrimaryKeySelective(officialAccount) == 1 ? true : false;
    }

    @Override
    public String addOfficialAccount(Long memberId, String addUrl, String remark) {
        Map<String, String> map = new HashMap<>();
        map.put("addUrl", addUrl);
        map.put("memberId", memberId.toString());
        map.put("remark", remark);
        try {
            String url = HttpRequestUtils.postString("http://www.weitaomi.com.cn/index.php/kfz/login/index", JSON.toJSONString(map));
            return url;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
