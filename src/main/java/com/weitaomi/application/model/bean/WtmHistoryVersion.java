package com.weitaomi.application.model.bean;

import com.weitaomi.application.model.BaseModel;

import javax.persistence.*;

@Table(name = "wtm_weitaomi_history_version")
public class WtmHistoryVersion extends BaseModel{

    /**
     * 平台
     */
    @Column(name = "platform")
    private String platform;

    /**
     * 版本号
     */
    @Column(name = "version")
    private String version;
    /**
     * 下载链接
     */
    @Column(name = "link")
    private String link;

    /**
     * 创建日期
     */
    @Column(name = "createTime")
    private Long createTime;

    /**
     * 获取平台
     *
     * @return platform - 平台
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * 设置平台
     *
     * @param platform 平台
     */
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    /**
     * 获取版本号
     *
     * @return version - 版本号
     */
    public String getVersion() {
        return version;
    }

    /**
     * 设置版本号
     *
     * @param version 版本号
     */
    public void setVersion(String version) {
        this.version = version;
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
     * 获取下载链接
     * @return link 下载链接
     */
    public String getLink() {
        return this.link;
    }

    /**
     * 设置下载链接
     * @param link 下载链接
     */
    public void setLink(String link) {
        this.link = link;
    }
}