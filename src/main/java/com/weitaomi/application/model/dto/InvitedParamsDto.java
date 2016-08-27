package com.weitaomi.application.model.dto;

import java.util.List;

/**
 * Created by Administrator on 2016/8/21.
 */
public class InvitedParamsDto {
    /**
     * 邀请奖励
     */
    private Long invitedScore;
    /**
     * 邀请总奖励
     */
    private Long totalScore;


    /**
     * 获取邀请奖励
     * @return invitedScore 邀请奖励
     */
    public Long getInvitedScore() {
        return this.invitedScore;
    }

    /**
     * 设置邀请奖励
     * @param invitedScore 邀请奖励
     */
    public void setInvitedScore(Long invitedScore) {
        this.invitedScore = invitedScore;
    }

    /**
     * 获取邀请总奖励
     * @return totalScore 邀请总奖励
     */
    public Long getTotalScore() {
        return this.totalScore;
    }

    /**
     * 设置邀请总奖励
     * @param totalScore 邀请总奖励
     */
    public void setTotalScore(Long totalScore) {
        this.totalScore = totalScore;
    }
}
