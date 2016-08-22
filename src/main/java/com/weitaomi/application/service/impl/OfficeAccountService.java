package com.weitaomi.application.service.impl;

import com.alibaba.fastjson.JSON;
import com.weitaomi.application.model.bean.*;
import com.weitaomi.application.model.dto.*;
import com.weitaomi.application.model.mapper.*;
import com.weitaomi.application.service.interf.*;
import com.weitaomi.systemconfig.exception.BusinessException;
import com.weitaomi.systemconfig.exception.InfoException;
import com.weitaomi.systemconfig.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
    private TaskPoolMapper taskPoolMapper;
    @Autowired
    private OfficeMemberMapper officeMemberMapper;
    @Autowired
    private IMemberScoreFlowService memberScoreFlowService;
    @Autowired
    private IMemberScoreService memberScoreService;
    @Autowired
    private IMemberTaskHistoryService memberTaskHistoryService;
    @Override
    public List<UnFollowOfficicalAccountDto> getAccountsByMemberId(Long memberId){
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
        WtmOfficialMember wtmOfficialMember=wtmOfficialMemberMapper.getWtmOfficialMemberByUnionId(unionId);
        String nickName="";
        if (wtmOfficialMember!=null){
            nickName=wtmOfficialMember.getNickname();
        }
        List<OfficialAccountMsg> list=addOfficalAccountDto.getLinkList();
        if (list.isEmpty()){
            throw new BusinessException("要关注的公号列表为空");
        }
        List<Map<String,String>> mapList=new ArrayList<Map<String, String>>();
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("unionId",addOfficalAccountDto.getUnionId());
        for (OfficialAccountMsg officialAccountMsg:list){
            String key=addOfficalAccountDto.getUnionId()+":"+officialAccountMsg.getOriginId();
            OfficialAccount officalAccount=officalAccountMapper.getOfficalAccountByoriginId(officialAccountMsg.getOriginId());
            String value=officalAccount.getId().toString();
            cacheService.setCacheByKey(key,value,60*30);
            Map<String,String> map=new HashMap<String, String>();
            map.put("name",officialAccountMsg.getUsername());
            map.put("addUrl",officialAccountMsg.getAddUrl());
            mapList.add(map);
        }
        params.put("linkList",mapList);
        String param= JSON.toJSONString(params);
        String url= PropertiesUtil.getValue("server.officialAccount.receiveAddRequest.url");
        try {
            HttpRequestUtils.postStringEntity(url,param);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    @Transactional
    public Boolean pushAddFinished(Map<String,String> params) {
        String unionId=params.get("unionId");
        String originId=params.get("originId");
        String key=unionId+":"+originId;
        String value=cacheService.getCacheByKey(key,String.class);
        if (value!=null){
            Long id=Long.valueOf(value);
            TaskPool taskPool = taskPoolMapper.getTaskPoolByOfficialId(id);
            OfficialAccountWithScore officialAccountWithScore  =officalAccountMapper.getOfficialAccountWithScoreById(id);
            Long memberId=thirdLoginMapper.getMemberIdByUnionId(unionId);
            if (taskPool!=null&&officialAccountWithScore!=null&&taskPool.getTotalScore()>officialAccountWithScore.getScore()){
                //增加任务记录
                MemberTaskWithDetail memberTaskWithDetail=new MemberTaskWithDetail();
                taskPoolMapper.updateTaskPoolWithScore(officialAccountWithScore.getScore(),taskPool.getId());
                memberTaskWithDetail.setTaskId(1L);
                memberTaskWithDetail.setPointCount(officialAccountWithScore.getScore());
                memberTaskWithDetail.setIsFinished(1);
                MemberTask memberTask=memberTaskMapper.selectByPrimaryKey(1);
                memberTaskWithDetail.setMemberId(memberId);
                memberTaskWithDetail.setTaskName(memberTask.getTaskName());
                memberTaskWithDetail.setTaskDesc(memberTask.getTaskDesc());
                memberTaskWithDetail.setCreateTime(DateUtils.getUnixTimestamp());
                List<MemberTaskHistoryDetail> memberTaskHistoryDetailList=new ArrayList<MemberTaskHistoryDetail>();
                MemberTaskHistoryDetail memberTaskHistoryDetail=new MemberTaskHistoryDetail();
                memberTaskHistoryDetail.setTaskName(memberTask.getTaskName());
                memberTaskHistoryDetail.setTaskDesc(officialAccountWithScore.getUserName());
                memberTaskHistoryDetail.setPointCount(officialAccountWithScore.getScore());
                memberTaskHistoryDetail.setIsFinished(1);
                memberTaskHistoryDetail.setCreateTime(DateUtils.getUnixTimestamp());
                memberTaskHistoryDetailList.add(memberTaskHistoryDetail);
                memberTaskWithDetail.setMemberTaskHistoryDetailList(memberTaskHistoryDetailList);
                memberTaskHistoryService.addMemberTaskToHistory(memberTaskWithDetail);
                //添加到公众号关注表中
                OfficeMember officeMember=new OfficeMember();
                officeMember.setIsAccessNow(1);
                officeMember.setMemberId(memberId);
                officeMember.setOfficeAccountId(id);
                officeMember.setCreateTime(DateUtils.getUnixTimestamp());
                officeMemberMapper.insertSelective(officeMember);
                //增加积分以及积分记录
                memberScoreService.addMemberScore(memberId,3L,officialAccountWithScore.getScore(), UUIDGenerator.generate());
                cacheService.delKeyFromRedis(key);
            }else {
                throw new InfoException("任务不存在，或者任务已结束");
            }
        }
        return false;
    }

    @Override
    public List<OfficialAccountMsg> getOfficialAccountMsg(Long memberId) {
        List<OfficialAccountMsg> officialAccountMsgs=officalAccountMapper.getOfficialAccountMsg(memberId);
        if (officialAccountMsgs.isEmpty()){
            return null;
        }
        return officialAccountMsgs;
    }

}
