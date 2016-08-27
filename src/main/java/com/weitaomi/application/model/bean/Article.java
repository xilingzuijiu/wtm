package com.weitaomi.application.model.bean;

import com.weitaomi.application.model.BaseModel;

import javax.persistence.*;

@Table(name = "wtm_article")
public class Article extends BaseModel {

    /**
     * 商家ID
     */
    @Column(name = "officialAccountId")
    private Long officialAccountId;

    /**
     * 文章地址
     */
    @Column(name = "url")
    private String url;

    /**
     * 文章地址
     */
    @Column(name = "imageUrl")
    private String imageUrl;
    /**
     * 文章标题
     */
    @Column(name = "title")
    private String title;

    /**
     * 文章摘要
     */
    @Column(name = "articleAbstract")
    private String articleAbstract;

    /**
     * 总阅读数
     */
    @Column(name = "readNumber")
    private Integer readNumber;

    /**
     * 点赞数
     */
    @Column(name = "agreeNumber")
    private Integer agreeNumber;

    /**
     * 通过该平台点赞增长数
     */
    @Column(name = "agreeIncreaseNumber")
    private Integer agreeIncreaseNumber;

    /**
     * 通过该平台阅读增长数
     */
    @Column(name = "readIncreaseNumber")
    private Integer readIncreaseNumber;

    /**
     * 有效阅读数
     */
    @Column(name = "validNumber")
    private Integer validNumber;

    /**
     * 是否置顶 0：不置顶，1：置顶
     */
    @Column(name = "isTop")
    private Integer isTop;

    /**
     * 创建日期
     */
    @Column(name = "createTime")
    private Long createTime;

    /**
     * 获取商家ID
     *
     * @return userId - 商家ID
     */
    public Long getOfficialAccountId() {
        return officialAccountId;
    }

    /**
     * 设置商家ID
     *
     * @param officialAccountId 商家ID
     */
    public void setOfficialAccountId(Long officialAccountId) {
        this.officialAccountId = officialAccountId;
    }

    /**
     * 获取文章地址
     *
     * @return url - 文章地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置文章地址
     *
     * @param url 文章地址
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取文章标题
     *
     * @return title - 文章标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置文章标题
     *
     * @param title 文章标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取文章摘要
     *
     * @return articleAbstract - 文章摘要
     */
    public String getArticleAbstract() {
        return articleAbstract;
    }

    /**
     * 设置文章摘要
     *
     * @param articleAbstract 文章摘要
     */
    public void setArticleAbstract(String articleAbstract) {
        this.articleAbstract = articleAbstract;
    }

    /**
     * 获取总阅读数
     *
     * @return readNumber - 总阅读数
     */
    public Integer getReadNumber() {
        return readNumber;
    }

    /**
     * 设置总阅读数
     *
     * @param readNumber 总阅读数
     */
    public void setReadNumber(Integer readNumber) {
        this.readNumber = readNumber;
    }

    /**
     * 获取点赞数
     *
     * @return agreeNumber - 点赞数
     */
    public Integer getAgreeNumber() {
        return agreeNumber;
    }

    /**
     * 设置点赞数
     *
     * @param agreeNumber 点赞数
     */
    public void setAgreeNumber(Integer agreeNumber) {
        this.agreeNumber = agreeNumber;
    }

    /**
     * 获取通过该平台点赞增长数
     *
     * @return agreeIncreaseNumber - 通过该平台点赞增长数
     */
    public Integer getAgreeIncreaseNumber() {
        return agreeIncreaseNumber;
    }

    /**
     * 设置通过该平台点赞增长数
     *
     * @param agreeIncreaseNumber 通过该平台点赞增长数
     */
    public void setAgreeIncreaseNumber(Integer agreeIncreaseNumber) {
        this.agreeIncreaseNumber = agreeIncreaseNumber;
    }

    /**
     * 获取通过该平台阅读增长数
     *
     * @return readIncreaseNumber - 通过该平台阅读增长数
     */
    public Integer getReadIncreaseNumber() {
        return readIncreaseNumber;
    }

    /**
     * 设置通过该平台阅读增长数
     *
     * @param readIncreaseNumber 通过该平台阅读增长数
     */
    public void setReadIncreaseNumber(Integer readIncreaseNumber) {
        this.readIncreaseNumber = readIncreaseNumber;
    }

    /**
     * 获取有效阅读数
     *
     * @return validNumber - 有效阅读数
     */
    public Integer getValidNumber() {
        return validNumber;
    }

    /**
     * 设置有效阅读数
     *
     * @param validNumber 有效阅读数
     */
    public void setValidNumber(Integer validNumber) {
        this.validNumber = validNumber;
    }

    /**
     * 获取是否置顶 0：不置顶，1：置顶
     *
     * @return isTop - 是否置顶 0：不置顶，1：置顶
     */
    public Integer getIsTop() {
        return isTop;
    }

    /**
     * 设置是否置顶 0：不置顶，1：置顶
     *
     * @param isTop 是否置顶 0：不置顶，1：置顶
     */
    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
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
     * 获取文章地址
     * @return imageUrl 文章地址
     */
    public String getImageUrl() {
        return this.imageUrl;
    }

    /**
     * 设置文章地址
     * @param imageUrl 文章地址
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}