package com.weitaomi.application.model.dto;

import com.weitaomi.application.model.bean.Article;

/**
 * Created by supumall on 2016/7/8.
 */
public class ArticleDto extends Article {
    /**
     * 公众号ID
     */
    private Long accountId;
    /**
     * 公众号名称
     */
    private String userName;

    /**
     * 获取后台用户ID
     * @return accountId 后台用户ID
     */
    public Long getAccountId() {
        return this.accountId;
    }

    /**
     * 设置后台用户ID
     * @param accountId 后台用户ID
     */
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    /**
     * 获取文章所属机构名称
     * @return userName 文章所属机构名称
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * 设置文章所属机构名称
     * @param userName 文章所属机构名称
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
