package com.weitaomi.application.model.bean;

import com.weitaomi.application.model.BaseModel;

import javax.persistence.*;

@Table(name = "wtm_official_accounts")
public class OfficalAccount extends BaseModel{

    /**
     * 商家类型
     */
    @Column(name = "sellerTypeId")
    private Integer sellerTypeId;

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
     * 公众号等级
     */
    @Column(name = "level")
    private String level;
    /**
     * 用户ID
     */
    @Column(name = "memberId")
    private String memberId;
    /**
     * 公号原始ID
     */
    @Column(name = "originId")
    private String originId;
    /**
     * 公号appId
     */
    @Column(name = "appId")
    private String appId;
    /**
     * 公号appId
     */
    @Column(name = "appSecret")
    private String appSecret;
    /**
     * 公号名称
     */
    @Column(name = "userName")
    private String userName;
    /**
     * 公号token
     */
    @Column(name = "accessToken")
    private String accessToken;

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
     * 关注地址
     */
    @Column(name = "addUrl")
    private String addUrl;
    /**
     * 头像地址
     */
    @Column(name = "imageUrl")
    private String imageUrl;
    /**
     * 创建日期
     */
    @Column(name = "tokenUpdateTime")
    private Long tokenUpdateTime;
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

    /**
     * 获取商家类型
     * @return sellerTypeId 商家类型
     */
    public Integer getSellerTypeId() {
        return this.sellerTypeId;
    }

    /**
     * 设置商家类型
     * @param sellerTypeId 商家类型
     */
    public void setSellerTypeId(Integer sellerTypeId) {
        this.sellerTypeId = sellerTypeId;
    }

    /**
     * 获取用户ID
     * @return memberId 用户ID
     */
    public String getMemberId() {
        return this.memberId;
    }

    /**
     * 设置用户ID
     * @param memberId 用户ID
     */
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    /**
     * 获取公号原始ID
     * @return originId 公号原始ID
     */
    public String getOriginId() {
        return this.originId;
    }

    /**
     * 设置公号原始ID
     * @param originId 公号原始ID
     */
    public void setOriginId(String originId) {
        this.originId = originId;
    }

    /**
     * 获取关注地址
     * @return addUrl 关注地址
     */
    public String getAddUrl() {
        return this.addUrl;
    }

    /**
     * 设置关注地址
     * @param addUrl 关注地址
     */
    public void setAddUrl(String addUrl) {
        this.addUrl = addUrl;
    }

    /**
     * 获取头像地址
     * @return imageUrl 头像地址
     */
    public String getImageUrl() {
        return this.imageUrl;
    }

    /**
     * 设置头像地址
     * @param imageUrl 头像地址
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    /**
     * 获取公号appId
     * @return appId 公号appId
     */
    public String getAppId() {
        return this.appId;
    }

    /**
     * 设置公号appId
     * @param appId 公号appId
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * 获取公号appId
     * @return appSecret 公号appId
     */
    public String getAppSecret() {
        return this.appSecret;
    }

    /**
     * 设置公号appId
     * @param appSecret 公号appId
     */
    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    /**
     * 获取公号token
     * @return accessToken 公号token
     */
    public String getAccessToken() {
        return this.accessToken;
    }

    /**
     * 设置公号token
     * @param accessToken 公号token
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * 获取创建日期
     * @return tokenUpdateTime 创建日期
     */
    public Long getTokenUpdateTime() {
        return this.tokenUpdateTime;
    }

    /**
     * 设置创建日期
     * @param tokenUpdateTime 创建日期
     */
    public void setTokenUpdateTime(Long tokenUpdateTime) {
        this.tokenUpdateTime = tokenUpdateTime;
    }

    /**
     * 获取公众号等级
     * @return level 公众号等级
     */
    public String getLevel() {
        return this.level;
    }

    /**
     * 设置公众号等级
     * @param level 公众号等级
     */
    public void setLevel(String level) {
        this.level = level;
    }
}