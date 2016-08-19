package com.weitaomi.application.model.dto;

import com.weitaomi.application.model.bean.MemberTaskHistory;
import com.weitaomi.application.model.bean.MemberTaskHistoryDetail;

import java.util.List;

/**
 * Created by Administrator on 2016/8/17.
 */
public class MemberTaskWithDetail extends MemberTaskHistory{
    /**
     * 任务详情
     */
    private List<MemberTaskHistoryDetail> memberTaskHistoryDetailList;


    /**
     * 获取任务详情
     * @return memberTaskHistoryDetailList 任务详情
     */
    public List<MemberTaskHistoryDetail> getMemberTaskHistoryDetailList() {
        return this.memberTaskHistoryDetailList;
    }

    /**
     * 设置任务详情
     * @param memberTaskHistoryDetailList 任务详情
     */
    public void setMemberTaskHistoryDetailList(List<MemberTaskHistoryDetail> memberTaskHistoryDetailList) {
        this.memberTaskHistoryDetailList = memberTaskHistoryDetailList;
    }
}
