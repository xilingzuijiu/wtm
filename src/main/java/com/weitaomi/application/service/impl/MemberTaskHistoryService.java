package com.weitaomi.application.service.impl;

import com.github.pagehelper.PageInfo;
import com.weitaomi.application.model.bean.*;
import com.weitaomi.application.model.dto.MemberTaskDto;
import com.weitaomi.application.model.dto.MemberTaskWithDetail;
import com.weitaomi.application.model.mapper.MemberTaskHistoryDetailMapper;
import com.weitaomi.application.model.mapper.MemberTaskHistoryMapper;
import com.weitaomi.application.model.mapper.MemberTaskMapper;
import com.weitaomi.application.model.mapper.OfficeMemberMapper;
import com.weitaomi.application.service.interf.IMemberScoreService;
import com.weitaomi.application.service.interf.IMemberTaskHistoryService;
import com.weitaomi.systemconfig.exception.BusinessException;
import com.weitaomi.systemconfig.exception.InfoException;
import com.weitaomi.systemconfig.util.DateUtils;
import com.weitaomi.systemconfig.util.Page;
import com.weitaomi.systemconfig.util.UUIDGenerator;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/16.
 */
@Service
public class MemberTaskHistoryService  implements IMemberTaskHistoryService {
    private Logger logger= LoggerFactory.getLogger(MemberTaskHistoryService.class);
    @Autowired
    private MemberTaskHistoryMapper memberTaskHistoryMapper;
    @Autowired
    private MemberTaskMapper memberTaskMapper;
    @Autowired
    private MemberTaskHistoryDetailMapper memberTaskHistoryDetailMapper;
    @Autowired
    private IMemberScoreService memberScoreService;
    @Autowired
    private OfficeMemberMapper officeMemberMapper;
    @Override
    public Page<MemberTaskWithDetail> getMemberTaskInfo(Long memberId,Integer type,Integer pageSize,Integer pageIndex) {
        List<MemberTaskWithDetail> memberTaskHistoryDtoList=memberTaskHistoryMapper.getMemberTaskHistoryList(memberId,type,new RowBounds(pageIndex,pageSize));
        PageInfo<MemberTaskWithDetail> showDtoPage=new PageInfo<MemberTaskWithDetail>(memberTaskHistoryDtoList);
        return Page.trans(showDtoPage);
    }

    @Override
    public List<MemberTaskHistoryDetail> getMemberTaskInfoDetail(Long taskHistoryId) {

        List<MemberTaskHistoryDetail> memberTaskHistoryDetails=memberTaskHistoryDetailMapper.getMemberTaskHistoryDetialList(taskHistoryId);
        if (memberTaskHistoryDetails.isEmpty()){
            return null;
        }
        return memberTaskHistoryDetails;
    }

    @Override
    public List<MemberTaskDto> getMemberDailyTask(Long memberId) {
        List<MemberTask> finishedMemberTasks=memberTaskMapper.getMemberTaskFinished(memberId,DateUtils.getTodayZeroSeconds(),DateUtils.getTodayEndSeconds());
        List<MemberTask> memberTaskList=memberTaskMapper.getAllMemberTask();
        if (memberTaskList.isEmpty()){
            return null;
        }
        List<MemberTaskDto> memberTaskDtos=new ArrayList<MemberTaskDto>();
        if (finishedMemberTasks.isEmpty()){
            for (MemberTask memberTask:memberTaskList){
                MemberTaskDto memberTaskDto=new MemberTaskDto();
                memberTaskDto.setMemberTask(memberTask);
                memberTaskDto.setIsFinisherToday(0);
                memberTaskDtos.add(memberTaskDto);
            }
            return memberTaskDtos;
        }
        for (MemberTask memberTask:memberTaskList){
            Long taskId=memberTask.getId();
            boolean flag=false;
            for (MemberTask memberTask1:finishedMemberTasks){
                if (memberTask1.getId()==taskId){
                    flag=true;
                }
            }
            MemberTaskDto memberTaskDto=new MemberTaskDto();
            memberTaskDto.setMemberTask(memberTask);
            if (flag){
                memberTaskDto.setIsFinisherToday(1);
            } else{
                memberTaskDto.setIsFinisherToday(0);
            }
            memberTaskDtos.add(memberTaskDto);
        }
        return memberTaskDtos;
    }

    @Override
    public boolean addMemberTaskToHistory(MemberTaskWithDetail memberTaskWithDetail) {
        if (memberTaskWithDetail==null){
            throw new BusinessException("任务内容为空");
        }
        MemberTaskHistory memberTaskHistory=new MemberTaskHistory();
        if (memberTaskWithDetail.getTaskName()!=null){
            memberTaskHistory.setTaskName(memberTaskWithDetail.getTaskName());
        }
        if (memberTaskWithDetail.getTaskDesc()!=null){
            memberTaskHistory.setTaskDesc(memberTaskWithDetail.getTaskDesc());
        }
        if (memberTaskWithDetail.getTaskId()!=null){
            memberTaskHistory.setTaskId(memberTaskWithDetail.getTaskId());
        }
        if (memberTaskWithDetail.getPointCount()!=null){
            memberTaskHistory.setPointCount(memberTaskWithDetail.getPointCount());
        }
        if (memberTaskWithDetail.getCreateTime()!=null){
            memberTaskHistory.setCreateTime(DateUtils.getUnixTimestamp());
        }
        if (memberTaskWithDetail.getMemberId()!=null){
            memberTaskHistory.setMemberId(memberTaskWithDetail.getMemberId());
        }
        if (memberTaskWithDetail.getIsFinished()!=null){
            memberTaskHistory.setIsFinished(memberTaskWithDetail.getIsFinished());
        }
        memberTaskHistoryMapper.insertSelective(memberTaskHistory);
        if (!memberTaskWithDetail.getMemberTaskHistoryDetailList().isEmpty()){
            memberTaskHistoryDetailMapper.insertIntoDetail(memberTaskWithDetail.getMemberTaskHistoryDetailList(),memberTaskHistory.getId(),DateUtils.getUnixTimestamp());
        }
        return false;
    }

    @Override
    public boolean updateMemberTaskToHistory(Long memberTaskId) {
        return false;
    }

    @Override
    @Transactional
    public MemberScore addDailyTask(Long memberId, Long typeId) {
        List<MemberTaskHistory> memberTaskHistoryList=memberTaskMapper.getIsMemberTaskFinished(memberId,typeId,DateUtils.getTodayZeroSeconds(),DateUtils.getTodayEndSeconds());
        if (!memberTaskHistoryList.isEmpty()){
            throw new InfoException("该任务今天已完成");
        }
        MemberTask memberTask=memberTaskMapper.selectByPrimaryKey(typeId);
        //增加记录
        MemberTaskWithDetail memberTaskWithDetail=new MemberTaskWithDetail();
        memberTaskWithDetail.setTaskId(typeId);
        memberTaskWithDetail.setPointCount(memberTask.getPointCount());
        memberTaskWithDetail.setIsFinished(1);
        memberTaskWithDetail.setMemberId(memberId);
        memberTaskWithDetail.setTaskName(memberTask.getTaskName());
        memberTaskWithDetail.setTaskDesc(memberTask.getTaskDesc());
        memberTaskWithDetail.setCreateTime(DateUtils.getUnixTimestamp());
        List<MemberTaskHistoryDetail> memberTaskHistoryDetailList=new ArrayList<MemberTaskHistoryDetail>();
        MemberTaskHistoryDetail memberTaskHistoryDetail=new MemberTaskHistoryDetail();
        memberTaskHistoryDetail.setTaskName(memberTask.getTaskName());
        memberTaskHistoryDetail.setTaskDesc(memberTask.getTaskDesc());
        memberTaskHistoryDetail.setPointCount(memberTask.getPointCount());
        memberTaskHistoryDetail.setIsFinished(1);
        memberTaskHistoryDetail.setCreateTime(DateUtils.getUnixTimestamp());
        memberTaskHistoryDetailList.add(memberTaskHistoryDetail);
        memberTaskWithDetail.setMemberTaskHistoryDetailList(memberTaskHistoryDetailList);
        this.addMemberTaskToHistory(memberTaskWithDetail);
        MemberScore memberScore=memberScoreService.addMemberScore(memberId,3L,Long.valueOf(memberTask.getPointCount()), UUIDGenerator.generate());
        if (memberScore!=null){
            return memberScore;
        }
        return null;
    }

    @Override
    public void deleteUnFinishedTask() {
        logger.info("定时任务启动");
        int number1=officeMemberMapper.deleteOverTimeUnfollowedAccounts();
        logger.info("删除未关注公众号"+number1+"条");
        int number2=memberTaskHistoryMapper.deleteUnfinishedTask();
        logger.info("删除未完成任务"+number2+"条");
        int number3=memberTaskHistoryMapper.deleteUnfinishedTaskDetail();
        logger.info("删除未完成任务详情"+number3+"条");
    }
}
