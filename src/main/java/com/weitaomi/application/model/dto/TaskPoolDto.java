package com.weitaomi.application.model.dto;

import javax.persistence.Column;

/**
 * Created by Administrator on 2016/9/6.
 */
public class TaskPoolDto {
    private Long taskId;
    /**
     * 任务类型 0：求粉，1：阅读
     */
    private Integer taskType;

    /**
     * 任务总支出米币
     */
    private Integer totalScore;
    /**
     * 单次奖励
     */
    private Integer singleScore;
    /**
     * 需求数量
     */
    private Integer needNumber;

    /**
     * 现在是否上架
     */
    private Integer isPublishNow;
    /**
     * 文章标题
     */
    private String articleTitle;
    /**
     * 请求时间
     */
    private Long createTime;

    /**
     * 获取任务类型 0：求粉，1：阅读
     * @return taskType 任务类型 0：求粉，1：阅读
     */
    public Integer getTaskType() {
        return this.taskType;
    }

    /**
     * 设置任务类型 0：求粉，1：阅读
     * @param taskType 任务类型 0：求粉，1：阅读
     */
    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    /**
     * 获取任务总支出米币
     * @return totalScore 任务总支出米币
     */
    public Integer getTotalScore() {
        return this.totalScore;
    }

    /**
     * 设置任务总支出米币
     * @param totalScore 任务总支出米币
     */
    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    /**
     * 获取单次奖励
     * @return singleScore 单次奖励
     */
    public Integer getSingleScore() {
        return this.singleScore;
    }

    /**
     * 设置单次奖励
     * @param singleScore 单次奖励
     */
    public void setSingleScore(Integer singleScore) {
        this.singleScore = singleScore;
    }

    /**
     * 获取现在是否上架
     * @return isPublishNow 现在是否上架
     */
    public Integer getIsPublishNow() {
        return this.isPublishNow;
    }

    /**
     * 设置现在是否上架
     * @param isPublishNow 现在是否上架
     */
    public void setIsPublishNow(Integer isPublishNow) {
        this.isPublishNow = isPublishNow;
    }

    /**
     * 获取文章标题
     * @return articleTitle 文章标题
     */
    public String getArticleTitle() {
        return this.articleTitle;
    }

    /**
     * 设置文章标题
     * @param articleTitle 文章标题
     */
    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    /**
     * 获取请求时间
     * @return createTime 请求时间
     */
    public Long getCreateTime() {
        return this.createTime;
    }

    /**
     * 设置请求时间
     * @param createTime 请求时间
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getTaskId() {
        return this.taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    /**
     * 获取需求数量
     * @return needNumber 需求数量
     */
    public Integer getNeedNumber() {
        return this.needNumber;
    }

    /**
     * 设置需求数量
     * @param needNumber 需求数量
     */
    public void setNeedNumber(Integer needNumber) {
        this.needNumber = needNumber;
    }
}
