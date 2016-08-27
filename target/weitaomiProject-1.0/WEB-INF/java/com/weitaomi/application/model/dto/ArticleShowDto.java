package com.weitaomi.application.model.dto;

import com.weitaomi.application.model.bean.Article;
import com.weitaomi.application.model.bean.OfficialAccount;

/**
 * Created by supumall on 2016/7/7.
 */
public class ArticleShowDto extends Article{
    /**
     * 用户信息
     */
    private String  username;
    /**
     * 是否已经阅读过 0:未阅读，1：已阅读
     */
    private Integer isReadBefore;
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
     * @return username 用户信息
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * 设置用户信息
     * @param username 用户信息
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
