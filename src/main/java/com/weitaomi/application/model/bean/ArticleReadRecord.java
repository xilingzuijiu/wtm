package com.weitaomi.application.model.bean;

import com.weitaomi.application.model.BaseModel;

import javax.persistence.*;

@Table(name = "wtm_article_read_record")
public class ArticleReadRecord extends BaseModel{

    /**
     * 用户ID
     */
    @Column(name = "memberId")
    private Long memberId;

    /**
     * 文章ID
     */
    @Column(name = "articleId")
    private Long articleId;

    /**
     * 类型 0：阅读 1：点赞
     */
    @Column(name = "type")
    private Integer type=0;

    /**
     * 创建日期
     */
    @Column(name = "createTime")
    private Long createTime;


    /**
     * 获取用户ID
     *
     * @return memberId - 用户ID
     */
    public Long getMemberId() {
        return memberId;
    }

    /**
     * 设置用户ID
     *
     * @param memberId 用户ID
     */
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    /**
     * 获取文章ID
     *
     * @return articleId - 文章ID
     */
    public Long getArticleId() {
        return articleId;
    }

    /**
     * 设置文章ID
     *
     * @param articleId 文章ID
     */
    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    /**
     * 获取类型 0：阅读 1：点赞
     *
     * @return type - 类型 0：阅读 1：点赞
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置类型 0：阅读 1：点赞
     *
     * @param type 类型 0：阅读 1：点赞
     */
    public void setType(Integer type) {
        this.type = type;
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