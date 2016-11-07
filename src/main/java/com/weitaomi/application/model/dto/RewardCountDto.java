package com.weitaomi.application.model.dto;

/**
 * Created by Administrator on 2016/11/7.
 */
public class RewardCountDto {
    /**
     * 任务标题
     */
    private String title;
    /**
     * 任务详情
     */
    private String desc;
    /**
     * 被邀请人ID
     */
    private Long memberId;
    /**
     * 邀请人ID
     */
    private Long parentId;
    /**
     * 用户名
     */
    private String memberName;
    /**
     * 奖励米币累计
     */
    private Double totalFlowScore;

    /**
     * 获取任务标题
     * @return title 任务标题
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * 设置任务标题
     * @param title 任务标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取任务详情
     * @return desc 任务详情
     */
    public String getDesc() {
        return this.desc;
    }

    /**
     * 设置任务详情
     * @param desc 任务详情
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * 获取被邀请人ID
     * @return memberId 被邀请人ID
     */
    public Long getMemberId() {
        return this.memberId;
    }

    /**
     * 设置被邀请人ID
     * @param memberId 被邀请人ID
     */
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    /**
     * 获取邀请人ID
     * @return parentId 邀请人ID
     */
    public Long getParentId() {
        return this.parentId;
    }

    /**
     * 设置邀请人ID
     * @param parentId 邀请人ID
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取用户名
     * @return memberName 用户名
     */
    public String getMemberName() {
        return this.memberName;
    }

    /**
     * 设置用户名
     * @param memberName 用户名
     */
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    /**
     * 获取奖励米币累计
     * @return totalFlowScore 奖励米币累计
     */
    public Double getTotalFlowScore() {
        return this.totalFlowScore;
    }

    /**
     * 设置奖励米币累计
     * @param totalFlowScore 奖励米币累计
     */
    public void setTotalFlowScore(Double totalFlowScore) {
        this.totalFlowScore = totalFlowScore;
    }
}
