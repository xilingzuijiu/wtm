package com.weitaomi.application.model.dto;

import com.weitaomi.application.model.bean.MemberTask;

/**
 * Created by Administrator on 2016/8/16.
 */
public class MemberTaskDto{
    private MemberTask memberTask;
    /**
     * 今日是否完成
     */
    private Integer isFinisherToday;

    /**
     * 获取今日是否完成
     * @return isFinisherToday 今日是否完成
     */
    public Integer getIsFinisherToday() {
        return this.isFinisherToday;
    }

    /**
     * 设置今日是否完成
     * @param isFinisherToday 今日是否完成
     */
    public void setIsFinisherToday(Integer isFinisherToday) {
        this.isFinisherToday = isFinisherToday;
    }

    public MemberTask getMemberTask() {
        return this.memberTask;
    }

    public void setMemberTask(MemberTask memberTask) {
        this.memberTask = memberTask;
    }
}
