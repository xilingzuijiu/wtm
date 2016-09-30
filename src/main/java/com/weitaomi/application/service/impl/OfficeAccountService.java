package com.weitaomi.application.service.impl;

import com.alibaba.fastjson.JSON;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by supumall on 2016/8/9.
 */
@Service
public class OfficeAccountService implements IOfficeAccountService {
    private static Logger logger= LoggerFactory.getLogger(OfficeAccountService.class);
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
    @Override
    public List<OfficialAccountsDto> getAccountsByMemberId(Long memberId){
        return officalAccountMapper.getAccountsByMemberId(memberId,0L);
    }
    /**
     * 查看已关注公众号
     */
    @Override
    public List<MemberAccountLabel> getOfficialAccountMsgList(Long memberId){
        List<MemberAccountLabel> officialAccountNameList=officeMemberMapper.getOfficialAccountNameList(memberId);
        return officialAccountNameList;
    }
    /**
     *更新已关注公众号
     */
    @Override
    public Integer signOfficialAccountMsgList(Long memberId){
        List<MemberAccountLabel> officialAccountNameList=officeMemberMapper.getOfficialAccountNameList(memberId);
        Integer num=officeMemberMapper.updateOfficialMemberList(memberId);
        for (MemberAccountLabel memberAccountLabel: officialAccountNameList) {
            String key = memberAccountLabel.getNickName() + ":" + memberAccountLabel.getSex() + ":" + memberAccountLabel.getOriginId();
            int number = memberTaskHistoryMapper.deleteMemberTaskUnfinished(memberId, 0, memberAccountLabel.getOriginId());
            cacheService.delKeyFromRedis(key);
        }
        return num;
    }
    @Override
    @Transactional
    public boolean pushAddRequest(Long memberId,AddOfficalAccountDto addOfficalAccountDto) {
        List<OfficeMember> officeMembers=officeMemberMapper.getOfficeMemberList(memberId);
        if (!officeMembers.isEmpty()){
            String info="您还有未关注公众号，请到服务号内完成\n未完成任务将会在1小时后删除\n若领任务之前已关注，请标注这些公众号";
            throw new InfoException(info);
        }
        if (addOfficalAccountDto==null){
            throw new BusinessException("任务列表为空");
        }
        String unionId=addOfficalAccountDto.getUnionId();
        if (StringUtil.isEmpty(unionId)){
            throw new BusinessException("用户唯一识别码为空");
        }
        List<OfficialAccountMsg> list=addOfficalAccountDto.getLinkList();
        if (list.isEmpty()){
            throw new BusinessException("要关注的公号列表为空");
        }
        List<OfficeMember> officeMemberList=new ArrayList<>();
        List<Long> idList=new ArrayList<>();
        for (OfficialAccountMsg officialAccountMsg:list){
            ThirdLoginDto thirdLoginDto=thirdLoginMapper.getThirdlogInDtoMemberId(memberId);
            String key=thirdLoginDto.getNickname()+":"+thirdLoginDto.getSex()+":"+officialAccountMsg.getOriginId();
            logger.info("key is {}",key);
            OfficialAccountWithScore officialAccountWithScore=officalAccountMapper.getOfficialAccountWithScoreById(officialAccountMsg.getOriginId());
            MemberCheck memberCheck=new MemberCheck(memberId,officialAccountWithScore.getId());
            MemberCheck valueTemp = cacheService.getCacheByKey(key,MemberCheck.class);
            if (valueTemp!=null){
                throw new InfoException("公众号"+officialAccountWithScore.getUserName()+"的关注未完成，请先完成");
            }
            cacheService.setCacheByKey(key,memberCheck, SystemConfig.TASK_CACHE_TIME);
            OfficeMember officeMember=new OfficeMember();
            officeMember.setMemberId(memberId);
            officeMember.setOfficeAccountId(officialAccountWithScore.getId());
            officeMember.setIsAccessNow(0);
            officeMemberList.add(officeMember);
            TaskPool taskPool = taskPoolMapper.getTaskPoolByOfficialId(officialAccountWithScore.getId(), 1);
            //添加到待完成任务记录中
            String detail ="关注公众号"+officialAccountWithScore.getUserName()+"，领取"+officialAccountWithScore.getScore()+"米币";
            memberTaskHistoryService.addMemberTaskToHistory(memberId,1L,taskPool.getRate().multiply(BigDecimal.valueOf(officialAccountWithScore.getScore())).doubleValue(),0,detail,null,officialAccountMsg.getOriginId());
            idList.add(officialAccountWithScore.getId());
        }
        Long time =DateUtils.getUnixTimestamp();
        int number=officeMemberMapper.batchAddOfficeMember(officeMemberList,time);
        if (number>0) {
            String url = PropertiesUtil.getValue("server.officialAccount.receiveAddRequest.url");
            Map<String,Object>  map=new HashMap<>();
            map.put("unionId",unionId);
            map.put("officialAccountIdList",idList);
            map.put("flag","1");
            Integer accountAdsId=accountAdsMapper.getLatestAccountAdsId();
            if (accountAdsId!=null){
                map.put("accountAdsId",accountAdsId);
            }
            try {
                HttpRequestUtils.postStringEntity(url, JSON.toJSONString(map));
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    @Transactional
    public Boolean pushAddFinished(Map<String,String> params) {
        String openid=params.get("openid");
        String nickname=params.get("nickname");
        String sexString=params.get("sex");
        String originId=params.get("originId");
        Integer flag=Integer.valueOf(params.get("flag"));
//        JpushUtils.buildRequest("受到消息了"+unionId+"      "+originId);
        logger.info("openId:{},flag{}",openid,nickname);
        OfficialAccountWithScore officialAccountWithScore = officalAccountMapper.getOfficialAccountWithScoreById(originId);
        if (flag==0){
            OfficeMember officeMember=officeMemberMapper.getOfficeMemberByOpenId(openid);
            if (officeMember==null){
                throw new BusinessException("没有此用户记录");
            }
            if (DateUtils.getUnixTimestamp()-officeMember.getCreateTime()<7*24*60*60){
                //增加积分以及积分记录
                Long memberId=officeMember.getMemberId();
               int num =  officeMemberMapper.deleteFollowAccountsMember(officeMember.getId());
                if (num>0){
                    TaskPool taskPool = taskPoolMapper.getTaskPoolByOfficialId(officialAccountWithScore.getId(), 1);
                    memberScoreService.addMemberScore(memberId,7L, 1, -(taskPool.getRate().multiply(BigDecimal.valueOf(officialAccountWithScore.getScore()))).doubleValue(), UUIDGenerator.generate());
                }
            }
        }
        if (flag==1) {
            Integer sex = Integer.valueOf(sexString);
            String key = nickname + ":" + sex + ":" + originId;
            logger.info("key is {}",key);
            MemberCheck memberCheck = cacheService.getCacheByKey(key, MemberCheck.class);
            logger.info("获取到的关注数据为:{}",JSON.toJSONString(memberCheck));
            if (memberCheck != null) {
                Long officeAccountId = Long.valueOf(memberCheck.getOfficialAccountsId());
                TaskPool taskPool = taskPoolMapper.getTaskPoolByOfficialId(officeAccountId, 1);
                Long memberId = memberCheck.getMemberId();
                if (taskPool == null) {
                    JpushUtils.buildRequest("您关注的公众号任务：" + officialAccountWithScore.getUserName() + "已经结束", memberId);
                    return false;
                }
                Double score = taskPool.getTotalScore() - taskPool.getSingleScore();
                if (taskPool != null && officialAccountWithScore != null && score >= 0) {
                    //添加到公众号关注表中
                    OfficeMember officeMember = officeMemberMapper.getOfficeMember(memberId,officeAccountId);
                    logger.info("关注记录表:{}",JSON.toJSONString(officeMember));
                    if (officeMember == null) {
                        JpushUtils.buildRequest("您关注的公众号任务：" + officialAccountWithScore.getUserName() + "，不存在或者已经结束", memberId);
                        return false;
                    }
                    if (officeMember.getIsAccessNow() == 1) {
                        JpushUtils.buildRequest("您已关注过公众号：" + officialAccountWithScore.getUserName() + "，该任务失效", memberId);
                        return false;
                    }
                    officeMember.setIsAccessNow(1);
                    officeMember.setOpenId(openid);
                    officeMember.setFinishedTime(DateUtils.getUnixTimestamp());
                    int num = officeMemberMapper.updateByPrimaryKeySelective(officeMember);
                    if (num > 0) {
                        //任务池中的任务剩余积分更改
                        if (score >= taskPool.getSingleScore()) {
                            taskPoolMapper.updateTaskPoolWithScore(score.doubleValue(), taskPool.getId());
                        } else {
                            taskPool.setTotalScore(0D);
                            taskPool.setLimitDay(0L);
                            taskPool.setNeedNumber(0);
                            taskPool.setSingleScore(0D);
                            taskPool.setIsPublishNow(0);
                            taskPoolMapper.updateByPrimaryKeySelective(taskPool);
                            memberScoreService.addMemberScore(officialAccountWithScore.getMemberId(), 6L, 1, score.doubleValue(), UUIDGenerator.generate());
                        }
                        //增加任务记录
                        logger.info("增加任务记录");
                        int number = memberTaskHistoryMapper.updateMemberTaskUnfinished(memberId, 0, officialAccountWithScore.getOriginId());
                        //增加积分以及积分记录
                        logger.info("增加积分以及积分记录");
                        memberScoreService.addMemberScore(memberId, 3L, 1, (taskPool.getRate().multiply(BigDecimal.valueOf(officialAccountWithScore.getScore()))).doubleValue(), UUIDGenerator.generate());
                        cacheService.delKeyFromRedis(key);
                    }
                } else {
                    JpushUtils.buildRequest(JpushUtils.getJpushMessage(memberId, "任务不存在，或者任务已结束"));
                    throw new InfoException("任务不存在，或者任务已结束");
                }
            }
        }
        return false;
    }

    @Override
    public List<OfficialAccountMsg> getOfficialAccountMsg(Long memberId,String unionId) {
        Member member=memberMapper.selectByPrimaryKey(memberId);
        if (member==null){
            throw new InfoException("用户信息为空");
        }
        List<OfficialAccountMsg> officialAccountMsgs=officalAccountMapper.getOfficialAccountMsg(memberId,unionId,member.getSex(),member.getProvince(),member.getCity());
        if (officialAccountMsgs.isEmpty()){
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
        OfficialAccount officialAccount=officalAccountMapper.selectByPrimaryKey(accountId);
        officialAccount.setIsOpen(isOpen);
        return officalAccountMapper.updateByPrimaryKeySelective(officialAccount)==1?true:false;
    }
}
