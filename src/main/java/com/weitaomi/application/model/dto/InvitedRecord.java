package com.weitaomi.application.model.dto;

import java.math.BigDecimal;

/**
 * Created by supumall on 2016/7/15.
 */
public class InvitedRecord {
    private String memberId;
    private String memberName;
    private BigDecimal memberScore;
    private String invitedTime;

    public String getMemberId() {
        return this.memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return this.memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public BigDecimal getMemberScore() {
        return this.memberScore;
    }

    public void setMemberScore(BigDecimal memberScore) {
        this.memberScore = memberScore;
    }

    public String getInvitedTime() {
        return this.invitedTime;
    }

    public void setInvitedTime(String invitedTime) {
        this.invitedTime = invitedTime;
    }
}
