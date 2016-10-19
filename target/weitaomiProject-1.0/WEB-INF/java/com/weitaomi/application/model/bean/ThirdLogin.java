package com.weitaomi.application.model.bean;

import com.weitaomi.application.model.BaseModel;

import javax.persistence.*;

@Table(name = "wtm_third_login")
public class ThirdLogin extends BaseModel {
    /**
     * 开放平台ID
     */
    @Column(name = "openId")
    private String openId;

    /**
     * 用户唯一识别ID
     */
    @Column(name = "unionId")
    private String unionId;

    /**
     * 会员ID
     */
    @Column(name = "memberId")
    private Long memberId;

    /**
     * 类型 0：微信，1：支付宝，2：QQ
     */
    @Column(name = "type")
    private Integer type;
    /**
     * 类型 0：微信，1：支付宝，2：QQ
     */
    @Column(name = "sourceType")
    private Integer sourceType;

    /**
     * 昵称
     */
    @Column(name = "nickname")
    private String nickname;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 创建日期
     */
    @Column(name = "createTime")
    private Long createTime;
    @Transient
    private String imageFiles;
    @Transient
    private Integer sex;

    /**
     * 获取开放平台ID
     *
     * @return openId - 开放平台ID
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * 设置开放平台ID
     *
     * @param openId 开放平台ID
     */
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    /**
     * 获取用户唯一识别ID
     *
     * @return unionId - 用户唯一识别ID
     */
    public String getUnionId() {
        return unionId;
    }

    /**
     * 设置用户唯一识别ID
     *
     * @param unionId 用户唯一识别ID
     */
    public void setUnionId(String unionId) {
        this.unionId = unionId;
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
     * 获取类型 0：微信，1：支付宝，2：QQ
     *
     * @return type - 类型 0：微信，1：支付宝，2：QQ
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置类型 0：微信，1：支付宝，2：QQ
     *
     * @param type 类型 0：微信，1：支付宝，2：QQ
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取昵称
     *
     * @return nickname - 昵称
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 设置昵称
     *
     * @param nickname 昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
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

    public String getImageFiles() {
        return this.imageFiles;
    }

    public void setImageFiles(String imageFiles) {
        this.imageFiles = imageFiles;
    }

    public Integer getSex() {
        return this.sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    /**
     * 获取类型 0：微信，1：支付宝，2：QQ
     * @return sourceType 类型 0：微信，1：支付宝，2：QQ
     */
    public Integer getSourceType() {
        return this.sourceType;
    }

    /**
     * 设置类型 0：微信，1：支付宝，2：QQ
     * @param sourceType 类型 0：微信，1：支付宝，2：QQ
     */
    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }
}