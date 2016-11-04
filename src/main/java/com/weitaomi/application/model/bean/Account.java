package com.weitaomi.application.model.bean;

import com.weitaomi.application.model.BaseModel;

import javax.persistence.*;

@Table(name = "wtm_account")
public class Account extends BaseModel{

    /**
     * 真实姓名
     */
    @Column(name = "realName")
    private String realName;

    /**
     * 密码ַ
     */
    @Column(name = "password")
    private String password;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Long createTime;

    /**
     * 获取真实姓名
     *
     * @return realName - 真实姓名
     */
    public String getRealName() {
        return realName;
    }

    /**
     * 设置真实姓名
     *
     * @param realName 真实姓名
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * 获取密码ַ
     *
     * @return password - 密码ַ
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码ַ
     *
     * @param password 密码ַ
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取创建时间
     *
     * @return createTime - 创建时间
     */
    public Long getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}