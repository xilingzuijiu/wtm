package com.weitaomi.application.model.dto;

import com.weitaomi.application.model.bean.OfficialAccount;

/**
 * Created by Administrator on 2016/8/19.
 */
public class OfficialAccountWithScore{
    private String userName;
    private Long id;
    private Long memberId;
    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
