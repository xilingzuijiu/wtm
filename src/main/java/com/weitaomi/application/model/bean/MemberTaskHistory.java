package com.weitaomi.application.model.bean;

import com.weitaomi.application.model.BaseModel;

import javax.persistence.*;

@Table(name = "wtm_member_task_history")
public class MemberTaskHistory extends BaseModel {
    /**
     * 用户ID
     */
    @Column(name = "memberId")
    private Long memberId;
    /**
     * 任务ID
     */
    @Column(name = "taskId")
    private Long taskId;
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
     * 是否已经完成
     */
    @Column(name = "isFinished")
    private Integer isFinished;

    /**
     * 创建日期
     */
    @Column(name = "createTime")
    private Long createTime;

    public void setId(Long id) {
        this.id = id;
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
     * 获取是否已经完成
     *
     * @return isFinished - 是否已经完成
     */
    public Integer getIsFinished() {
        return isFinished;
    }

    /**
     * 设置是否已经完成
     *
     * @param isFinished 是否已经完成
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


    /**
     * 获取用户ID
     * @return memberId 用户ID
     */
    public Long getMemberId() {
        return this.memberId;
    }

    /**
     * 设置用户ID
     * @param memberId 用户ID
     */
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    /**
     * 获取任务ID
     * @return taskId 任务ID
     */
    public Long getTaskId() {
        return this.taskId;
    }

    /**
     * 设置任务ID
     * @param taskId 任务ID
     */
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}