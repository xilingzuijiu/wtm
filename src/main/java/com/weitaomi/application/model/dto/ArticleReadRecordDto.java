package com.weitaomi.application.model.dto;

import com.weitaomi.application.model.bean.Article;

/**
 * Created by supumall on 2016/7/8.
 */
public class ArticleReadRecordDto {
    /**
     * 文章标题
     */
     private String title;
    /**
     * 文章摘要
     */
     private String articleAbstract;
    /**
     * 文章ID
     */
     private Long articleId;
    /**
     * 奖励
     */
     private Double singleScore;
    /**
     * 文章地址
     */
     private String articleUrl;
    /**
     * 创建时间
     */
     private Long createTime;
    /**
     * 图片地址
     */
    private String imageUrl;

    /**
     * 获取文章标题
     * @return title 文章标题
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * 设置文章标题
     * @param title 文章标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取文章摘要
     * @return articleAbstract 文章摘要
     */
    public String getArticleAbstract() {
        return this.articleAbstract;
    }

    /**
     * 设置文章摘要
     * @param articleAbstract 文章摘要
     */
    public void setArticleAbstract(String articleAbstract) {
        this.articleAbstract = articleAbstract;
    }

    /**
     * 获取文章ID
     * @return articleId 文章ID
     */
    public Long getArticleId() {
        return this.articleId;
    }

    /**
     * 设置文章ID
     * @param articleId 文章ID
     */
    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    /**
     * 获取奖励
     * @return singleScore 奖励
     */
    public Double getSingleScore() {
        return this.singleScore;
    }

    /**
     * 设置奖励
     * @param singleScore 奖励
     */
    public void setSingleScore(Double singleScore) {
        this.singleScore = singleScore;
    }

    /**
     * 获取文章地址
     * @return articleUrl 文章地址
     */
    public String getArticleUrl() {
        return this.articleUrl;
    }

    /**
     * 设置文章地址
     * @param articleUrl 文章地址
     */
    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    /**
     * 获取创建时间
     * @return createTime 创建时间
     */
    public Long getCreateTime() {
        return this.createTime;
    }

    /**
     * 设置创建时间
     * @param createTime 创建时间
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取图片地址
     * @return imageUrl 图片地址
     */
    public String getImageUrl() {
        return this.imageUrl;
    }

    /**
     * 设置图片地址
     * @param imageUrl 图片地址
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
