package com.weitaomi.application.model.bean;

import com.weitaomi.application.model.BaseModel;

import javax.persistence.*;

@Table(name = "wtm_member_pay_accounts")
public class MemberPayAccounts extends BaseModel {

    /**
     * 会员ID
     */
    @Column(name = "memberId")
    private Long memberId;

    /**
     * 支付类型 0：微信支付，1支付宝支付
     */
    @Column(name = "payType")
    private Integer payType;

    /**
     * 支付账号
     */
    @Column(name = "payAccount")
    private String payAccount;

    /**
     * 真实姓名
     */
    @Column(name = "realName")
    private String realName;

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
     * 获取会员ID
     *
     * @return memberId - 会员ID
     */
    public Long getMemberId() {
        return memberId;
    }

    /**
     * 设置会员ID
     *
     * @param memberId 会员ID
     */
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    /**
     * 获取支付类型 0：微信支付，1支付宝支付
     *
     * @return payType - 支付类型 0：微信支付，1支付宝支付
     */
    public Integer getPayType() {
        return payType;
    }

    /**
     * 设置支付类型 0：微信支付，1支付宝支付
     *
     * @param payType 支付类型 0：微信支付，1支付宝支付
     */
    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    /**
     * 获取支付账号
     *
     * @return payAccount - 支付账号
     */
    public String getPayAccount() {
        return payAccount;
    }

    /**
     * 设置支付账号
     *
     * @param payAccount 支付账号
     */
    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    /**
     * 获取真实姓名
     *
     * @return realName - 真实姓名
     */
    public String getRealName() {
        return realName;
    }

    /**
     * 设置真实姓名
     *
     * @param realName 真实姓名
     */
    public void setRealName(String realName) {
        this.realName = realName;
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