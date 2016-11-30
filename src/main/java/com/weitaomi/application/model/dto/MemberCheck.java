package com.weitaomi.application.model.dto;

/**
 * Created by Administrator on 2016/9/24.
 */
public class MemberCheck {
    private Long memberId;
    private Long officialAccountsId;
    private Long taskPoolId;
    public MemberCheck() {
    }

    public MemberCheck(Long memberId, Long officialAccountsId, Long taskPoolId) {
        this.memberId = memberId;
        this.officialAccountsId = officialAccountsId;
        this.taskPoolId = taskPoolId;
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

    public Long getTaskPoolId() {
        return this.taskPoolId;
    }

    public void setTaskPoolId(Long taskPoolId) {
        this.taskPoolId = taskPoolId;
    }
}
