package com.weitaomi.application.model.bean;

import com.weitaomi.application.model.BaseModel;

import javax.persistence.*;

@Table(name = "wtm_accounts_ads")
public class AccountAds extends BaseModel{
    /**
     * 标题
     */
    @Column(name = "title")
    private String title;

    /**
     * 简介
     */
    @Column(name = "description")
    private String description;

    /**
     * 图片地址
     */
    @Column(name = "imageUrl")
    private String imageUrl;

    /**
     * 文章地址
     */
    @Column(name = "articleUrl")
    private String articleUrl;

    /**
     * 创建时间
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
     * 获取标题
     *
     * @return title - 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取简介
     *
     * @return description - 简介
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置简介
     *
     * @param description 简介
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取图片地址
     *
     * @return imageUrl - 图片地址
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * 设置图片地址
     *
     * @param imageUrl 图片地址
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * 获取文章地址
     *
     * @return articleUrl - 文章地址
     */
    public String getArticleUrl() {
        return articleUrl;
    }

    /**
     * 设置文章地址
     *
     * @param articleUrl 文章地址
     */
    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    /**
     * 获取创建时间
     *
     * @return createTime - 创建时间
     */
    public Long getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}