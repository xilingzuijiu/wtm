package com.weitaomi.application.model.dto;

/**
 * Created by supumall on 2016/8/9.
 */
public class OfficialAccountMsg {
    /**
     * 开放平台分配给公众号的appId
     */
    private String appId;
    /**
     * 公众号原始ID
     */
    private String originId;
    /**
     * 公号名
     */
    private String username;
    /**
     * 公号关注地址
     */
    private String addUrl;
    /**
     * 关注人数
     */
    private Integer followNumber;
    /**
     * 关注奖励
     */
    private Integer rewardCount;

    /**
     * 获取开放平台分配给公众号的appId
     * @return appId 开放平台分配给公众号的appId
     */
    public String getAppId() {
        return this.appId;
    }

    /**
     * 设置开放平台分配给公众号的appId
     * @param appId 开放平台分配给公众号的appId
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * 获取用户唯一标识
     * @return originId 用户唯一标识
     */
    public String getOriginId() {
        return this.originId;
    }

    /**
     * 设置用户唯一标识
     * @param originId 用户唯一标识
     */
    public void setOriginId(String originId) {
        this.originId = originId;
    }


    /**
     * 获取公号关注地址
     * @return addUrl 公号关注地址
     */
    public String getAddUrl() {
        return this.addUrl;
    }

    /**
     * 设置公号关注地址
     * @param addUrl 公号关注地址
     */
    public void setAddUrl(String addUrl) {
        this.addUrl = addUrl;
    }

    /**
     * 获取关注人数
     * @return followNumber 关注人数
     */
    public Integer getFollowNumber() {
        return this.followNumber;
    }

    /**
     * 设置关注人数
     * @param followNumber 关注人数
     */
    public void setFollowNumber(Integer followNumber) {
        this.followNumber = followNumber;
    }

    /**
     * 获取关注奖励
     * @return rewardCount 关注奖励
     */
    public Integer getRewardCount() {
        return this.rewardCount;
    }

    /**
     * 设置关注奖励
     * @param rewardCount 关注奖励
     */
    public void setRewardCount(Integer rewardCount) {
        this.rewardCount = rewardCount;
    }

    /**
     * 获取公号名
     * @return username 公号名
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * 设置公号名
     * @param username 公号名
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
