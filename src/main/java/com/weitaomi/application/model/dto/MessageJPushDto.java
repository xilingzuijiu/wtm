package com.weitaomi.application.model.dto;

/**
 * Created by Administrator on 2016/8/24.
 */
public class MessageJPushDto {
    private Long memberId;
    private String message;

    public Long getMemberId() {
        return this.memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
