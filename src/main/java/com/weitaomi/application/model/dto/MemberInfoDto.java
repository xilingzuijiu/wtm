package com.weitaomi.application.model.dto;

import com.weitaomi.application.model.bean.Member;
import com.weitaomi.application.model.bean.OfficialAccount;

/**
 * Created by Administrator on 2016/8/18.
 */
public class MemberInfoDto extends Member {
    /**
     * 用户积分
     */
    private Long memberScore;
    /**
     * 微信openId
     */
    private String  openId;
    /**
     * 公众号
     */
    private OfficialAccount officialAccount;

    /**
     * 获取用户积分
     * @return memberScore 用户积分
     */
    public Long getMemberScore() {
        return this.memberScore;
    }

    /**
     * 设置用户积分
     * @param memberScore 用户积分
     */
    public void setMemberScore(Long memberScore) {
        this.memberScore = memberScore;
    }

    /**
     * 获取微信openId
     * @return openId 微信openId
     */
    public String getOpenId() {
        return this.openId;
    }

    /**
     * 设置微信openId
     * @param openId 微信openId
     */
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    /**
     * 获取公众号
     * @return officialAccount 公众号
     */
    public OfficialAccount getOfficialAccount() {
        return this.officialAccount;
    }

    /**
     * 设置公众号
     * @param officialAccount 公众号
     */
    public void setOfficialAccount(OfficialAccount officialAccount) {
        this.officialAccount = officialAccount;
    }
}
