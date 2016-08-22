package com.weitaomi.application.model.bean;

import com.weitaomi.application.model.BaseModel;

import javax.persistence.*;

@Table(name = "wtm_member_task_history_detail")
public class  MemberTaskHistoryDetail extends BaseModel {
    /**
     * 历史ID
     */
    @Column(name = "taskHistoryId")
    private Long taskHistoryId;

    /**
     * 奖励积分
     */
    @Column(name = "pointCount")
    private Long pointCount;

    /**
     * 任务名称
     */
    @Column(name = "taskName")
    private String taskName;

    /**
     * 任务描述
     */
    @Column(name = "taskDesc")
    private String taskDesc;

    /**
     * 是否已经完成 0未完成1已完成
     */
    @Column(name = "isFinished")
    private Integer isFinished;

    /**
     * 创建日期
     */
    @Column(name = "createTime")
    private Long createTime;

    /**
     * 获取历史ID
     *
     * @return taskHistoryId - 历史ID
     */
    public Long getTaskHistoryId() {
        return taskHistoryId;
    }

    /**
     * 设置历史ID
     *
     * @param taskHistoryId 历史ID
     */
    public void setTaskHistoryId(Long taskHistoryId) {
        this.taskHistoryId = taskHistoryId;
    }

    /**
     * 获取奖励积分
     *
     * @return count - 奖励积分
     */
    public Long getPointCount() {
        return pointCount;
    }

    /**
     * 设置奖励积分
     *
     * @param pointCount 奖励积分
     */
    public void setPointCount(Long pointCount) {
        this.pointCount = pointCount;
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
     * 获取是否已经完成 0未完成1已完成
     *
     * @return isFinished - 是否已经完成 0未完成1已完成
     */
    public Integer getIsFinished() {
        return isFinished;
    }

    /**
     * 设置是否已经完成 0未完成1已完成
     *
     * @param isFinished 是否已经完成 0未完成1已完成
     */
    public void setIsFinished(Integer isFinished) {
        this.isFinished = isFinished;
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
}