package com.weitaomi.application.model.dto;

import com.weitaomi.application.model.bean.MemberTaskHistory;
import com.weitaomi.application.model.bean.MemberTaskHistoryDetail;

import java.util.List;

/**
 * Created by supumall on 2016/7/16.
 */
public class MemberTaskHistoryDto extends MemberTaskHistory {
    /**
     * 任务记录详情
     */
    private List<MemberTaskHistoryDetail> memberTaskHistoryDetailList;

    /**
     * 获取任务记录详情
     * @return memberTaskHistoryDetailList 任务记录详情
     */
    public List<MemberTaskHistoryDetail> getMemberTaskHistoryDetailList() {
        return this.memberTaskHistoryDetailList;
    }

    /**
     * 设置任务记录详情
     * @param memberTaskHistoryDetailList 任务记录详情
     */
    public void setMemberTaskHistoryDetailList(List<MemberTaskHistoryDetail> memberTaskHistoryDetailList) {
        this.memberTaskHistoryDetailList = memberTaskHistoryDetailList;
    }
}
