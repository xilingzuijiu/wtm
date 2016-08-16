package com.weitaomi.application.service.impl;

import com.github.pagehelper.PageInfo;
import com.weitaomi.application.model.bean.Member;
import com.weitaomi.application.model.bean.MemberTask;
import com.weitaomi.application.model.bean.MemberTaskHistoryDetail;
import com.weitaomi.application.model.dto.ArticleShowDto;
import com.weitaomi.application.model.dto.MemberTaskDto;
import com.weitaomi.application.model.dto.MemberTaskHistoryDto;
import com.weitaomi.application.model.mapper.MemberTaskHistoryDetailMapper;
import com.weitaomi.application.model.mapper.MemberTaskHistoryMapper;
import com.weitaomi.application.model.mapper.MemberTaskMapper;
import com.weitaomi.application.service.interf.IMemberTaskHistoryService;
import com.weitaomi.systemconfig.util.DateUtils;
import com.weitaomi.systemconfig.util.Page;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/16.
 */
@Service
public class MemberTaskHistoryService implements IMemberTaskHistoryService {
    @Autowired
    private MemberTaskHistoryMapper memberTaskHistoryMapper;
    @Autowired
    private MemberTaskMapper memberTaskMapper;
    @Autowired
    private MemberTaskHistoryDetailMapper memberTaskHistoryDetailMapper;
    @Override
    public Page<MemberTaskHistoryDto> getMemberTaskInfo(Long memberId,Integer type,Integer pageSize,Integer pageIndex) {
        List<MemberTaskHistoryDto> memberTaskHistoryDtoList=memberTaskHistoryMapper.getMemberTaskHistoryList(memberId,type,new RowBounds(pageIndex,pageSize));
        PageInfo<MemberTaskHistoryDto> showDtoPage=new PageInfo<MemberTaskHistoryDto>(memberTaskHistoryDtoList);
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
}
