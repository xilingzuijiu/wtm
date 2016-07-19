package com.weitaomi.application.model.dto;

import com.weitaomi.application.model.bean.Article;

/**
 * Created by supumall on 2016/7/8.
 */
public class ArticleMemberRecordDto extends Article {
    private String MemberId;

    public String getMemberId() {
        return this.MemberId;
    }

    public void setMemberId(String MemberId) {
        this.MemberId = MemberId;
    }
}
