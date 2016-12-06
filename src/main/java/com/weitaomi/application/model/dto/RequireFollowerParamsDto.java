package com.weitaomi.application.model.dto;

import java.util.List;

/**
 * Created by Administrator on 2016/8/23.
 */
public class RequireFollowerParamsDto {
    /**
     * 用户公众号列表
     */
    private List<OfficialAccountsDto> officialAccountList;
    /**
     * 地址列表
     */
    private List<Address> addressList;
    /**
     * 关注奖励
     */
    private Double followScore;
    /**
     * 阅读奖励
     */
    private Double readScore;
    /**
     * 获取用户公众号列表
     * @return officialAccountList 用户公众号列表
     */
    public List<OfficialAccountsDto> getOfficialAccountList() {
        return this.officialAccountList;
    }

    /**
     * 设置用户公众号列表
     * @param officialAccountList 用户公众号列表
     */
    public void setOfficialAccountList(List<OfficialAccountsDto> officialAccountList) {
        this.officialAccountList = officialAccountList;
    }

    /**
     * 获取地址列表
     * @return addressList 地址列表
     */
    public List<Address> getAddressList() {
        return this.addressList;
    }

    /**
     * 设置地址列表
     * @param addressList 地址列表
     */
    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }

    /**
     * 获取关注奖励
     * @return followScore 关注奖励
     */
    public Double getFollowScore() {
        return this.followScore;
    }

    /**
     * 设置关注奖励
     * @param followScore 关注奖励
     */
    public void setFollowScore(Double followScore) {
        this.followScore = followScore;
    }

    /**
     * 获取阅读奖励
     * @return readScore 阅读奖励
     */
    public Double getReadScore() {
        return this.readScore;
    }

    /**
     * 设置阅读奖励
     * @param readScore 阅读奖励
     */
    public void setReadScore(Double readScore) {
        this.readScore = readScore;
    }
}


