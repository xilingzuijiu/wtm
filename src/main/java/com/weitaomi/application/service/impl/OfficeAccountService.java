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
        return officalAccountMapper.getAccountsByMemberId(memberId);
    }
    @Override
    @Transactional
    public boolean pushAddRequest(Long memberId,AddOfficalAccountDto addOfficalAccountDto) {
        List<OfficeMember> officeMembers=officeMemberMapper.getOfficeMemberList(memberId);
        if (!officeMembers.isEmpty()){
            String info="您还有未完成的任务，请先到微淘米APP服务号内完成，\n未完成任务将会在24小时内删除，请尽快完成,\n如有事宜，请联系客服~";
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
            String key=addOfficalAccountDto.getUnionId()+":"+officialAccountMsg.getOriginId();
            OfficialAccountWithScore officialAccountWithScore=officalAccountMapper.getOfficialAccountWithScoreById(officialAccountMsg.getOriginId());
            String value=officialAccountWithScore.getId().toString();
            String valueTemp = cacheService.getCacheByKey(key,String.class);
            if (valueTemp!=null){
                throw new InfoException("公众号"+officialAccountWithScore.getUserName()+"的关注未完成，请先完成");
            }
            cacheService.setCacheByKey(key,value, SystemConfig.TASK_CACHE_TIME);
            OfficeMember officeMember=new OfficeMember();
            officeMember.setMemberId(memberId);
            officeMember.setOfficeAccountId(officialAccountWithScore.getId());
            officeMember.setIsAccessNow(0);
            officeMemberList.add(officeMember);
            //添加到待完成任务记录中
            String detail ="关注公众号"+officialAccountWithScore.getUserName()+"，领取"+officialAccountWithScore.getScore()+"米币";
            memberTaskHistoryService.addMemberTaskToHistory(memberId,1L,officialAccountWithScore.getScore(),0,detail,null);
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
        String unionId=params.get("unionId");
        String appid=params.get("appId");
        String originId=officalAccountMapper.getOriginIdByAppId(appid);
//        JpushUtils.buildRequest("受到消息了"+unionId+"      "+originId);
        String key=unionId+":"+originId;
        String value=cacheService.getCacheByKey(key,String.class);
        if (value!=null){
            Long officeAccountId=Long.valueOf(value);
            TaskPool taskPool = taskPoolMapper.getTaskPoolByOfficialId(officeAccountId,1);
            OfficialAccountWithScore officialAccountWithScore  =officalAccountMapper.getOfficialAccountWithScoreById(originId);
            Long memberId=thirdLoginMapper.getMemberIdByUnionId(unionId);
            if (taskPool==null){
                JpushUtils.buildRequest("您关注的公众号任务："+officialAccountWithScore.getUserName()+"已经结束",memberId);
                return false;
            }
            Integer score=taskPool.getTotalScore()-taskPool.getSingleScore();
            if (taskPool!=null&&officialAccountWithScore!=null&&score>=0){
                //添加到公众号关注表中
                OfficeMember officeMember=officeMemberMapper.getOfficeMember(memberId,officeAccountId);
                if (officeMember==null){
                    JpushUtils.buildRequest("您关注的公众号任务："+officialAccountWithScore.getUserName()+"，不存在或者已经结束",memberId);
                    return false;
                }
                if (officeMember.getIsAccessNow()==1){
                    JpushUtils.buildRequest("您已关注过公众号："+officialAccountWithScore.getUserName()+"，该任务失效",memberId);
                    return false;
                }
                officeMember.setIsAccessNow(1);
                officeMember.setFinishedTime(DateUtils.getUnixTimestamp());
                int num= officeMemberMapper.updateByPrimaryKeySelective(officeMember);
                if (num>0) {
                    //任务池中的任务剩余积分更改
                    if (score>=taskPool.getSingleScore()){
                        taskPoolMapper.updateTaskPoolWithScore(score, taskPool.getId());
                    } else {
                        taskPool.setTotalScore(0);
                        taskPool.setLimitDay(0L);
                        taskPool.setNeedNumber(0);
                        taskPool.setSingleScore(0);
                        taskPool.setIsPublishNow(0);
                        taskPoolMapper.updateByPrimaryKeySelective(taskPool);
                        memberScoreService.addMemberScore(officialAccountWithScore.getMemberId(), 6L,1,score.doubleValue(), UUIDGenerator.generate());
                    }
                    //增加任务记录
                    int number = memberTaskHistoryMapper.updateMemberTaskUnfinished(memberId,0,officialAccountWithScore.getOriginId());
                    //增加积分以及积分记录
                    memberScoreService.addMemberScore(memberId, 3L,1,officialAccountWithScore.getScore().doubleValue(), UUIDGenerator.generate());
                    cacheService.delKeyFromRedis(key);
                }
            }else {
                JpushUtils.buildRequest(JpushUtils.getJpushMessage(memberId,"任务不存在，或者任务已结束"));
                throw new InfoException("任务不存在，或者任务已结束");
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
