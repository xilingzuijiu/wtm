package com.weitaomi.application.model.dto;

/**
 * Created by Administrator on 2016/9/24.
 */
public class MemberCheck {
    private Long memberId;
    private Long officialAccountsId;

    public MemberCheck() {
    }

    public MemberCheck(Long memberId, Long officialAccountsId) {
        this.memberId = memberId;
        this.officialAccountsId = officialAccountsId;
    }

    public Long getOfficialAccountsId() {
        return this.officialAccountsId;
    }

    public void setOfficialAccountsId(Long officialAccountsId) {
        this.officialAccountsId = officialAccountsId;
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
