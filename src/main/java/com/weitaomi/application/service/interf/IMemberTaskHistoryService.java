package com.weitaomi.application.service.interf;

import com.weitaomi.application.model.bean.MemberTask;
import com.weitaomi.application.model.bean.MemberTaskHistoryDetail;
import com.weitaomi.application.model.dto.MemberTaskDto;
import com.weitaomi.application.model.dto.MemberTaskHistoryDto;
import com.weitaomi.systemconfig.util.Page;

import java.util.List;

/**
 * Created by Administrator on 2016/8/16.
 */
public interface IMemberTaskHistoryService {
    /**
     * 获取用户的任务列表
     *
     * type :0 未完成，1已完成
     *
     * @param memberId
     * @return
     */
    public Page<MemberTaskHistoryDto> getMemberTaskInfo(Long memberId, Integer type, Integer pageSize, Integer pageIndex);

    /**
     * 任务ID
     * @param taskHistoryId
     * @return
     */
    public List<MemberTaskHistoryDetail> getMemberTaskInfoDetail(Long taskHistoryId);

    /**
     * 获取每日奖励任务
     * @param memberId
     * @return
     */
    public List<MemberTaskDto> getMemberDailyTask(Long memberId);
}
