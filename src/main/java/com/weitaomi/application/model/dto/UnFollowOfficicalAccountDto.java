package com.weitaomi.application.model.dto;

/**
 * Created by supumall on 2016/8/9.
 */
public class UnFollowOfficicalAccountDto {
    /**
     * 公号原始ID
     */
    private String originId;
    /**
     * 头像地址
     */
    private String imageUrl;
    /**
     * 关注地址
     */
    private String addUrl;
    /**
     * 公号名字
     */
    private String ofiicialAccountName;

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
     * 获取公号名字
     * @return ofiicialAccountName 公号名字
     */
    public String getOfiicialAccountName() {
        return this.ofiicialAccountName;
    }

    /**
     * 设置公号名字
     * @param ofiicialAccountName 公号名字
     */
    public void setOfiicialAccountName(String ofiicialAccountName) {
        this.ofiicialAccountName = ofiicialAccountName;
    }
}
