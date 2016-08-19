package com.weitaomi.application.model.bean;

import com.weitaomi.application.model.BaseModel;

import javax.persistence.*;

@Table(name = "wtm_task_pool")
public class TaskPool extends BaseModel{

    /**
     * 任务类型 0：求粉，1：阅读
     */
    @Column(name = "taskType")
    private Integer taskType;
    /**
     * 公众号ID
     */
    @Column(name = "officeAccountId")
    private Long officeAccountId;
    /**
     * 文章Id
     */
    @Column(name = "articleId")
    private Long articleId;
    /**
     * 任务总支出米币
     */
    @Column(name = "totalScore")
    private Integer totalScore;

    /**
     * 需求数量
     */
    @Column(name = "needNumber")
    private Integer needNumber;

    /**
     * 创建日期
     */
    @Column(name = "createTime")
    private Long createTime;

    /**
     * 获取任务类型 0：求粉，1：阅读
     *
     * @return taskType - 任务类型 0：求粉，1：阅读
     */
    public Integer getTaskType() {
        return taskType;
    }

    /**
     * 设置任务类型 0：求粉，1：阅读
     *
     * @param taskType 任务类型 0：求粉，1：阅读
     */
    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    /**
     * 获取任务总支出米币
     *
     * @return totalScore - 任务总支出米币
     */
    public Integer getTotalScore() {
        return totalScore;
    }

    /**
     * 设置任务总支出米币
     *
     * @param totalScore 任务总支出米币
     */
    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    /**
     * 获取需求数量
     *
     * @return needNumber - 需求数量
     */
    public Integer getNeedNumber() {
        return needNumber;
    }

    /**
     * 设置需求数量
     *
     * @param needNumber 需求数量
     */
    public void setNeedNumber(Integer needNumber) {
        this.needNumber = needNumber;
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
     * 获取公众号ID
     * @return officeAccountId 公众号ID
     */
    public Long getOfficeAccountId() {
        return this.officeAccountId;
    }

    /**
     * 设置公众号ID
     * @param officeAccountId 公众号ID
     */
    public void setOfficeAccountId(Long officeAccountId) {
        this.officeAccountId = officeAccountId;
    }

    /**
     * 获取文章Id
     * @return articleId 文章Id
     */
    public Long getArticleId() {
        return this.articleId;
    }

    /**
     * 设置文章Id
     * @param articleId 文章Id
     */
    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }
}