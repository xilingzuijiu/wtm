package com.weitaomi.application.model.bean;

import com.weitaomi.application.model.BaseModel;

import javax.persistence.*;

@Table(name = "wtm_payment_history")
public class PaymentHistory extends BaseModel {
    /**
     * 支付单号
     */
    @Column(name = "payCode")
    private String payCode;

    /**
     * 用户ID
     */
    @Column(name = "memberId")
    private Long memberId;

    /**
     * 支付平台
     */
    @Column(name = "platform")
    private String platform;

    /**
     * 序列号
     */
    @Column(name = "serialNumber")
    private String serialNumber;

    /**
     * 批量付款批次号，payType=1时 使用
     */
    @Column(name = "batchPayNo")
    private String batchPayNo;

    /**
     * 请求参数
     */
    @Column(name = "params")
    private String params;

    /**
     * 是否支付成功
     */
    @Column(name = "isPaySuccess")
    private Integer isPaySuccess;
    /**
     * 交易类型，0：付款给商家，1：付款给用户
     */
    @Column(name = "payType")
    private Integer payType;

    /**
     * 创建日期
     */
    @Column(name = "createTime")
    private Long createTime;
    /**
     * 获取支付单号
     *
     * @return payCode - 支付单号
     */
    public String getPayCode() {
        return payCode;
    }

    /**
     * 设置支付单号
     *
     * @param payCode 支付单号
     */
    public void setPayCode(String payCode) {
        this.payCode = payCode;
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
     * 获取支付平台
     *
     * @return platform - 支付平台
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * 设置支付平台
     *
     * @param platform 支付平台
     */
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    /**
     * 获取请求参数
     *
     * @return params - 请求参数
     */
    public String getParams() {
        return params;
    }

    /**
     * 设置请求参数
     *
     * @param params 请求参数
     */
    public void setParams(String params) {
        this.params = params;
    }

    /**
     * 获取是否支付成功
     *
     * @return isPaySuccess - 是否支付成功
     */
    public Integer getIsPaySuccess() {
        return isPaySuccess;
    }

    /**
     * 设置是否支付成功
     *
     * @param isPaySuccess 是否支付成功
     */
    public void setIsPaySuccess(Integer isPaySuccess) {
        this.isPaySuccess = isPaySuccess;
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
     * 获取批量付款批次号，payType=1时 使用
     * @return batchPayNo 批量付款批次号，payType=1时 使用
     */
    public String getBatchPayNo() {
        return this.batchPayNo;
    }

    /**
     * 设置批量付款批次号，payType=1时 使用
     * @param batchPayNo 批量付款批次号，payType=1时 使用
     */
    public void setBatchPayNo(String batchPayNo) {
        this.batchPayNo = batchPayNo;
    }

    /**
     * 获取交易类型，0：付款给商家，1：付款给用户
     * @return payType 交易类型，0：付款给商家，1：付款给用户
     */
    public Integer getPayType() {
        return this.payType;
    }

    /**
     * 设置交易类型，0：付款给商家，1：付款给用户
     * @param payType 交易类型，0：付款给商家，1：付款给用户
     */
    public void setPayType(Integer payType) {
        this.payType = payType;
    }


    /**
     * 获取序列号
     * @return serialNumber 序列号
     */
    public String getSerialNumber() {
        return this.serialNumber;
    }

    /**
     * 设置序列号
     * @param serialNumber 序列号
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}