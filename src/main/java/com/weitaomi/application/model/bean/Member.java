package com.weitaomi.application.model.bean;

import com.weitaomi.application.model.BaseModel;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "wtm_member")
public class Member extends BaseModel  implements Serializable {

    /**
     * 会员名称
     */
    @Column(name = "memberName")
    private String memberName;

    /**
     * 会员昵称
     */
    @Column(name = "nickName")
    private String nickName;

    /**
     * 电话
     */
    @Column(name = "telephone")
    private String telephone;

    /**
     * 加密盐
     */
    @Column(name = "salt")
    private String salt;

    /**
     * 密码
     */
    @Column(name = "password")
    private String password;

    /**
     * 会员生日
     */
    @Column(name = "birth")
    private Long birth;
    /**
     * 会员生日
     */
    @Column(name = "sex")
    private Integer sex;

    /**
     * 会员邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 邀请码
     */
    @Column(name = "invitedCode")
    private String invitedCode;
    /**
     * 来源
     */
    @Column(name = "source")
    private String source;

    /**
     * 头像url
     */
    @Column(name = "imageUrl")
    private String imageUrl;

    /**
     * 创建日期
     */
    @Column(name = "createTime")
    private Long createTime;

    /**
     * 获取会员名称
     *
     * @return memberName - 会员名称
     */
    public String getMemberName() {
        return memberName;
    }

    /**
     * 设置会员名称
     *
     * @param memberName 会员名称
     */
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    /**
     * 获取会员昵称
     *
     * @return nickName - 会员昵称
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * 设置会员昵称
     *
     * @param nickName 会员昵称
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * 获取电话
     *
     * @return telephone - 电话
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * 设置电话
     *
     * @param telephone 电话
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * 获取加密盐
     *
     * @return salt - 加密盐
     */
    public String getSalt() {
        return salt;
    }

    /**
     * 设置加密盐
     *
     * @param salt 加密盐
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }


    /**
     * 获取会员生日
     *
     * @return birth - 会员生日
     */
    public Long getBirth() {
        return birth;
    }

    /**
     * 设置会员生日
     *
     * @param birth 会员生日
     */
    public void setBirth(Long birth) {
        this.birth = birth;
    }

    /**
     * 获取会员邮箱
     *
     * @return email - 会员邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置会员邮箱
     *
     * @param email 会员邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取邀请码
     *
     * @return invitedCode - 邀请码
     */
    public String getInvitedCode() {
        return invitedCode;
    }

    /**
     * 设置邀请码
     *
     * @param invitedCode 邀请码
     */
    public void setInvitedCode(String invitedCode) {
        this.invitedCode = invitedCode;
    }

    /**
     * 获取头像url
     *
     * @return imageUrl - 头像url
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * 设置头像url
     *
     * @param imageUrl 头像url
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    /**
     * 获取会员生日
     * @return sex 会员生日
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * 设置会员生日
     * @param sex 会员生日
     */
    public void setSex(Integer sex) {
        this.sex = sex;
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
}