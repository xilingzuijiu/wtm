package com.weitaomi.application.model.dto;

import com.weitaomi.application.model.bean.Member;
import com.weitaomi.application.model.bean.ThirdLogin;

/**
 * Created by supumall on 2016/7/6.
 */
public class RegisterMsg {
    /**
     * 会员信息
     */
    private Member member;
    /**
     * 第三方登陆信息
     */
    private ThirdLogin thirdLogin;
    /**
     * 验证码
     */
    private String identifyCode;
    /**
     * 来源
     */
    private String source;

    /**
     * 邀请者的code
     */
    private String code;
    /**
     * 注册标识，0：本地注册，1：第三方注册
     */
    private Integer flag;

    /**
     * 获取会员信息
     * @return member 会员信息
     */
    public Member getMember() {
        return this.member;
    }

    /**
     * 设置会员信息
     * @param member 会员信息
     */
    public void setMember(Member member) {
        this.member = member;
    }

    /**
     * 获取验证码
     * @return identifyCode 验证码
     */
    public String getIdentifyCode() {
        return this.identifyCode;
    }

    /**
     * 设置验证码
     * @param identifyCode 验证码
     */
    public void setIdentifyCode(String identifyCode) {
        this.identifyCode = identifyCode;
    }

    /**
     * 获取来源
     * @return source 来源
     */
    public String getSource() {
        return this.source;
    }

    /**
     * 设置来源
     * @param source 来源
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * 获取第三方登陆信息
     * @return thirdLogin 第三方登陆信息
     */
    public ThirdLogin getThirdLogin() {
        return this.thirdLogin;
    }

    /**
     * 设置第三方登陆信息
     * @param thirdLogin 第三方登陆信息
     */
    public void setThirdLogin(ThirdLogin thirdLogin) {
        this.thirdLogin = thirdLogin;
    }

    /**
     * 获取注册标识，0：本地注册，1：第三方注册
     * @return flag 注册标识，0：本地注册，1：第三方注册
     */
    public Integer getFlag() {
        return this.flag;
    }

    /**
     * 设置注册标识，0：本地注册，1：第三方注册
     * @param flag 注册标识，0：本地注册，1：第三方注册
     */
    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    /**
     * 获取邀请者的code
     * @return code 邀请者的code
     */
    public String getCode() {
        return this.code;
    }

    /**
     * 设置邀请者的code
     * @param code 邀请者的code
     */
    public void setCode(String code) {
        this.code = code;
    }
}
