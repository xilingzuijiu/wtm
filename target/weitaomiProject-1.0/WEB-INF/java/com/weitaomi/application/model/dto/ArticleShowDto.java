package com.weitaomi.application.model.dto;

import com.weitaomi.application.model.bean.Article;
import com.weitaomi.application.model.bean.OfficalAccount;

/**
 * Created by supumall on 2016/7/7.
 */
public class ArticleShowDto {
    /**
     * 文章信息
     */
    private Article article;
    /**
     * 用户信息
     */
    private OfficalAccount user;
    /**
     * 是否已经阅读过 0:未阅读，1：已阅读
     */
    private Integer isReadBefore;

    /**
     * 获取文章信息
     * @return article 文章信息
     */
    public Article getArticle() {
        return this.article;
    }

    /**
     * 设置文章信息
     * @param article 文章信息
     */
    public void setArticle(Article article) {
        this.article = article;
    }
    /**
     * 获取是否已经阅读过
     * @return isReadBefore 是否已经阅读过
     */
    public Integer getIsReadBefore() {
        return this.isReadBefore;
    }

    /**
     * 设置是否已经阅读过
     * @param isReadBefore 是否已经阅读过
     */
    public void setIsReadBefore(Integer isReadBefore) {
        this.isReadBefore = isReadBefore;
    }

    /**
     * 获取用户信息
     * @return user 用户信息
     */
    public OfficalAccount getUser() {
        return this.user;
    }

    /**
     * 设置用户信息
     * @param user 用户信息
     */
    public void setUser(OfficalAccount user) {
        this.user = user;
    }
}
