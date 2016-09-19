package com.weitaomi.application.model.bean;

import com.weitaomi.application.model.BaseModel;

import javax.persistence.*;
import java.math.BigDecimal;

@Table(name = "wtm_member_score")
public class MemberScore extends BaseModel {

    /**
     * 用户ID
     */
    @Column(name = "memberId")
    private Long memberId;

    /**
     * 用户总积分
     */
    @Column(name = "memberScore")
    private BigDecimal memberScore;

    /**
     * 不可用积分
     */
    @Column(name = "inValidScore")
    private BigDecimal inValidScore;
    /**
     * 可用倍率
     */
    @Column(name = "rate")
    private BigDecimal rate;
    /**
     * 更新日期
     */
    @Column(name = "updateTime")
    private Long updateTime;
    /**
     * 创建日期
     */
    @Column(name = "createTime")
    private Long createTime;
    @Transient
    private Double avaliableScore;

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
    public BigDecimal getMemberScore() {
        return memberScore;
    }

    /**
     * 设置用户总积分
     *
     * @param memberScore 用户总积分
     */
    public void setMemberScore(BigDecimal memberScore) {
        this.memberScore = memberScore;
    }

    /**
     * 获取可用积分
     *
     * @return validScore - 可用积分
     */
    public BigDecimal getInValidScore() {
        return inValidScore;
    }

    /**
     * 设置可用积分
     *
     * @param inValidScore 可用积分
     */
    public void setInValidScore(BigDecimal inValidScore) {
        this.inValidScore = inValidScore;
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
     * 获取更新日期
     * @return updateTime 更新日期
     */
    public Long getUpdateTime() {
        return this.updateTime;
    }

    /**
     * 设置更新日期
     * @param updateTime 更新日期
     */
    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取可用倍率
     * @return rate 可用倍率
     */
    public BigDecimal getRate() {
        return this.rate;
    }

    /**
     * 设置可用倍率
     * @param rate 可用倍率
     */
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Double getAvaliableScore() {
        return this.avaliableScore;
    }

    public void setAvaliableScore(Double avaliableScore) {
        this.avaliableScore = avaliableScore;
    }
}