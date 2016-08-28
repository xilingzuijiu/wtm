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
    @Override
    public List<OfficialAccountsDto> getAccountsByMemberId(Long memberId){
        return officalAccountMapper.getAccountsByMemberId(memberId);
    }
    @Override
    @Transactional
    public boolean pushAddRequest(Long memberId,AddOfficalAccountDto addOfficalAccountDto) {
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
        for (OfficialAccountMsg officialAccountMsg:list){
            String key=addOfficalAccountDto.getUnionId()+":"+officialAccountMsg.getOriginId();
            OfficialAccountWithScore officialAccountWithScore=officalAccountMapper.getOfficialAccountWithScoreById(officialAccountMsg.getOriginId());
            String value=officialAccountWithScore.getId().toString();
            String valueTemp = cacheService.getCacheByKey(key,String.class);
//            if (valueTemp!=null){
//                throw new InfoException("公众号"+officialAccountWithScore.getUserName()+"的关注未完成，请先完成");
//            }
            cacheService.setCacheByKey(key,value, SystemConfig.TASK_CACHE_TIME);
            OfficeMember officeMember=new OfficeMember();
            officeMember.setMemberId(memberId);
            officeMember.setOfficeAccountId(officialAccountWithScore.getId());
            officeMember.setIsAccessNow(0);
            officeMemberList.add(officeMember);
            //添加到待完成任务记录中
            String detail ="关注公众号"+officialAccountWithScore.getUserName()+"，领取"+officialAccountWithScore.getScore()+"米币";
            memberTaskHistoryService.addMemberTaskToHistory(memberId,1L,officialAccountWithScore.getScore(),0,detail,null);
        }
        Long time =DateUtils.getUnixTimestamp();
        int number=officeMemberMapper.batchAddOfficeMember(officeMemberList,time);
        if (number>100) {
            String url = PropertiesUtil.getValue("server.officialAccount.receiveAddRequest.url");
            String messageUrl = PropertiesUtil.getValue("server.officialAccount.message.url");
            Map<String,String>  map=new HashMap<>();
            map.put("unionId",unionId);
            map.put("url",messageUrl + "?memberId=" + memberId + "&dateTime=" +time);
            map.put("flag","1");
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
        String originId=params.get("originId");
        JpushUtils.buildRequest("受到消息了"+unionId+"      "+originId);
        String key=unionId+":"+originId;
        String value=cacheService.getCacheByKey(key,String.class);
        if (value!=null){
            Long officeAccountId=Long.valueOf(value);
            TaskPool taskPool = taskPoolMapper.getTaskPoolByOfficialId(officeAccountId);
            OfficialAccountWithScore officialAccountWithScore  =officalAccountMapper.getOfficialAccountWithScoreById(originId);
            Long memberId=thirdLoginMapper.getMemberIdByUnionId(unionId);
            if (taskPool!=null&&officialAccountWithScore!=null&&taskPool.getTotalScore()>officialAccountWithScore.getScore()){
                //添加到公众号关注表中
                OfficeMember officeMember=officeMemberMapper.getOfficeMember(memberId,officeAccountId);
                if (officeMember==null){
                    JpushUtils.buildRequest(JpushUtils.getJpushMessage(memberId,"任务不存在，或者任务已结束"));
                    throw new InfoException("任务没有加入到列表中");
                }
                officeMember.setIsAccessNow(1);
                officeMember.setFinishedTime(DateUtils.getUnixTimestamp());
                int num= officeMemberMapper.updateByPrimaryKeySelective(officeMember);
                if (num>0) {
                    //任务池中的任务剩余积分更改
                    taskPoolMapper.updateTaskPoolWithScore(officialAccountWithScore.getScore(), taskPool.getId());
                    //增加任务记录
                    int number = memberTaskHistoryMapper.updateMemberTaskUnfinished(memberId,0,officialAccountWithScore.getOriginId());
                    //增加积分以及积分记录
                    memberScoreService.addMemberScore(memberId, 3L,1, BigDecimal.valueOf(officialAccountWithScore.getScore()).multiply(taskPool.getRate()).doubleValue(), UUIDGenerator.generate());
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

}
