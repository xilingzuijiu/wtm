package com.weitaomi.application.model.bean;

import com.weitaomi.application.model.BaseModel;

import javax.persistence.*;

@Table(name = "wtm_office_user")
public class OfficeUser extends BaseModel{

    /**
     * 公众号ID
     */
    @Column(name = "officeAccountId")
    private Long officeAccountId;

    /**
     * 用户唯一识别标识
     */
    @Column(name = "unionId")
    private String unionId;

    /**
     * 公号分配给用户的ID
     */
    @Column(name = "openId")
    private String openId;

    /**
     * 昵称
     */
    @Column(name = "nickname")
    private String nickname;

    /**
     * 创建日期
     */
    @Column(name = "createTime")
    private Long createTime;

    /**
     * 获取公众号ID
     *
     * @return officeAccountId - 公众号ID
     */
    public Long getOfficeAccountId() {
        return officeAccountId;
    }

    /**
     * 设置公众号ID
     *
     * @param officeAccountId 公众号ID
     */
    public void setOfficeAccountId(Long officeAccountId) {
        this.officeAccountId = officeAccountId;
    }

    /**
     * 获取用户唯一识别标识
     *
     * @return unionId - 用户唯一识别标识
     */
    public String getUnionId() {
        return unionId;
    }

    /**
     * 设置用户唯一识别标识
     *
     * @param unionId 用户唯一识别标识
     */
    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    /**
     * 获取公号分配给用户的ID
     *
     * @return openId - 公号分配给用户的ID
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * 设置公号分配给用户的ID
     *
     * @param openId 公号分配给用户的ID
     */
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    /**
     * 获取昵称
     *
     * @return nickname - 昵称
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 设置昵称
     *
     * @param nickname 昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 获取创建日期
     *
     * @return createTime - 创建日期
     */
    public Long getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建日期
     *
     * @param createTime 创建日期
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}