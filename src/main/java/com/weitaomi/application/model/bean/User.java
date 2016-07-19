package com.weitaomi.application.model.bean;

import com.weitaomi.application.model.BaseModel;

import javax.persistence.*;

@Table(name = "wtm_user")
public class User extends BaseModel{

    /**
     * 商家类型
     */
    @Column(name = "typeCode")
    private Integer typeCode;

    /**
     * 是否开通 0：未开通，1：开通
     */
    @Column(name = "isOpen")
    private Integer isOpen;

    /**
     * 商家开通授权ID
     */
    @Column(name = "openId")
    private String openId;

    /**
     * 商家名称
     */
    @Column(name = "userName")
    private String userName;

    /**
     * 粉丝数量
     */
    @Column(name = "accountNumber")
    private Integer accountNumber;

    /**
     * 备注信息
     */
    @Column(name = "remark")
    private String remark;

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
     * 获取商家类型
     *
     * @return typeCode - 商家类型
     */
    public Integer getTypeCode() {
        return typeCode;
    }

    /**
     * 设置商家类型
     *
     * @param typeCode 商家类型
     */
    public void setTypeCode(Integer typeCode) {
        this.typeCode = typeCode;
    }

    /**
     * 获取是否开通 0：未开通，1：开通
     *
     * @return isOpen - 是否开通 0：未开通，1：开通
     */
    public Integer getIsOpen() {
        return isOpen;
    }

    /**
     * 设置是否开通 0：未开通，1：开通
     *
     * @param isOpen 是否开通 0：未开通，1：开通
     */
    public void setIsOpen(Integer isOpen) {
        this.isOpen = isOpen;
    }

    /**
     * 获取商家开通授权ID
     *
     * @return openId - 商家开通授权ID
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * 设置商家开通授权ID
     *
     * @param openId 商家开通授权ID
     */
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    /**
     * 获取商家名称
     *
     * @return userName - 商家名称
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置商家名称
     *
     * @param userName 商家名称
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取粉丝数量
     *
     * @return accountNumber - 粉丝数量
     */
    public Integer getAccountNumber() {
        return accountNumber;
    }

    /**
     * 设置粉丝数量
     *
     * @param accountNumber 粉丝数量
     */
    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * 获取备注信息
     *
     * @return remark - 备注信息
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注信息
     *
     * @param remark 备注信息
     */
    public void setRemark(String remark) {
        this.remark = remark;
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