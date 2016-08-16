package com.weitaomi.application.model.bean;

import com.weitaomi.application.model.BaseModel;

import javax.persistence.*;

@Table(name = "wtm_member_task")
public class MemberTask extends BaseModel{
    /**
     * 奖励积分
     */
    @Column(name = "count")
    private Long count;

    /**
     * 任务名称
     */
    @Column(name = "taskName")
    private String taskName;
    /**
     * 是否为每日任务
     */
    @Column(name = "isDailyTask")
    private Integer isDailyTask;
    /**
     * 任务描述
     */
    @Column(name = "taskDesc")
    private String taskDesc;

    /**
     * 创建日期
     */
    @Column(name = "createTime")
    private Long createTime;
    /**
     * 获取奖励积分
     *
     * @return count - 奖励积分
     */
    public Long getCount() {
        return count;
    }

    /**
     * 设置奖励积分
     *
     * @param count 奖励积分
     */
    public void setCount(Long count) {
        this.count = count;
    }

    /**
     * 获取任务名称
     *
     * @return taskName - 任务名称
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * 设置任务名称
     *
     * @param taskName 任务名称
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * 获取任务描述
     *
     * @return taskDesc - 任务描述
     */
    public String getTaskDesc() {
        return taskDesc;
    }

    /**
     * 设置任务描述
     *
     * @param taskDesc 任务描述
     */
    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    /**
     * 获取创建日期
     *
     * @return createTime - 创建日期
     */
    public Long getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建日期
     *
     * @param createTime 创建日期
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取是否为每日任务
     * @return isDailyTask 是否为每日任务
     */
    public Integer getIsDailyTask() {
        return this.isDailyTask;
    }

    /**
     * 设置是否为每日任务
     * @param isDailyTask 是否为每日任务
     */
    public void setIsDailyTask(Integer isDailyTask) {
        this.isDailyTask = isDailyTask;
    }
}