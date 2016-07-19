package com.weitaomi.application.model.bean;

import com.weitaomi.application.model.BaseModel;

import javax.persistence.*;

@Table(name = "wtm_member_score")
public class MemberScore extends BaseModel{

    /**
     * 用户ID
     */
    @Column(name = "memberId")
    private Long memberId;

    /**
     * 用户总积分
     */
    @Column(name = "memberScore")
    private Integer memberScore;

    /**
     * 可用积分
     */
    @Column(name = "validScore")
    private Integer validScore;

    /**
     * 创建日期
     */
    @Column(name = "createTime")
    private Long createTime;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取用户ID
     *
     * @return memberId - 用户ID
     */
    public Long getMemberId() {
        return memberId;
    }

    /**
     * 设置用户ID
     *
     * @param memberId 用户ID
     */
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    /**
     * 获取用户总积分
     *
     * @return memberScore - 用户总积分
     */
    public Integer getMemberScore() {
        return memberScore;
    }

    /**
     * 设置用户总积分
     *
     * @param memberScore 用户总积分
     */
    public void setMemberScore(Integer memberScore) {
        this.memberScore = memberScore;
    }

    /**
     * 获取可用积分
     *
     * @return validScore - 可用积分
     */
    public Integer getValidScore() {
        return validScore;
    }

    /**
     * 设置可用积分
     *
     * @param validScore 可用积分
     */
    public void setValidScore(Integer validScore) {
        this.validScore = validScore;
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