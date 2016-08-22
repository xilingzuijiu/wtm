package com.weitaomi.application.model.dto;

import com.weitaomi.application.model.bean.*;

import java.util.List;

/**
 * Created by Administrator on 2016/8/18.
 */
public class MemberInfoDto extends Member {
    /**
     * 用户积分
     */
    private Long memberScore;
    /**
     * 微信昵称
     */
    private ThirdLogin thirdLogin;
    /**
     * 支付方式列表
     */
    private List<MemberPayAccounts> payAccountsList;
    /**
     * 公众号
     */
    private List<OfficialAccount> officialAccountList;

    /**
     * 获取微信昵称
     * @return thirdLogin 微信昵称
     */
    public ThirdLogin getThirdLogin() {
        return this.thirdLogin;
    }

    /**
     * 设置微信昵称
     * @param thirdLogin 微信昵称
     */
    public void setThirdLogin(ThirdLogin thirdLogin) {
        this.thirdLogin = thirdLogin;
    }

    /**
     * 获取支付方式列表
     * @return payAccountsList 支付方式列表
     */
    public List<MemberPayAccounts> getPayAccountsList() {
        return this.payAccountsList;
    }

    /**
     * 设置支付方式列表
     * @param payAccountsList 支付方式列表
     */
    public void setPayAccountsList(List<MemberPayAccounts> payAccountsList) {
        this.payAccountsList = payAccountsList;
    }

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
     * 获取公众号
     * @return officialAccountList 公众号
     */
    public List<OfficialAccount> getOfficialAccountList() {
        return this.officialAccountList;
    }

    /**
     * 设置公众号
     * @param officialAccountList 公众号
     */
    public void setOfficialAccountList(List<OfficialAccount> officialAccountList) {
        this.officialAccountList = officialAccountList;
    }
}
