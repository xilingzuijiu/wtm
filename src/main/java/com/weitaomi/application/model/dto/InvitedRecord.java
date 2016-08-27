package com.weitaomi.application.model.dto;

import java.math.BigDecimal;

/**
 * Created by supumall on 2016/7/15.
 */
public class InvitedRecord {
    private String imageUrl;
    private String memberName;
    private Long invitedTime;


    public String getMemberName() {
        return this.memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }


    public Long getInvitedTime() {
        return this.invitedTime;
    }

    public void setInvitedTime(Long invitedTime) {
        this.invitedTime = invitedTime;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
