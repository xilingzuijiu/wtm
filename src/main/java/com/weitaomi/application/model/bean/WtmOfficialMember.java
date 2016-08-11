package com.weitaomi.application.model.bean;

import com.weitaomi.application.model.BaseModel;

import javax.persistence.*;

@Table(name = "wtm_weitaomi_official_member")
public class WtmOfficialMember extends BaseModel{

    /**
     * 会员ID
     */
    @Column(name = "memberId")
    private Long memberId;

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
     * 关注时间
     */
    @Column(name = "followTime")
    private Long followTime;

    /**
     * 创建日期
     */
    @Column(name = "createTime")
    private Long createTime;

    /**
     * 获取会员ID
     *
     * @return memberId - 会员ID
     */
    public Long getMemberId() {
        return memberId;
    }

    /**
     * 设置会员ID
     *
     * @param memberId 会员ID
     */
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
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
     * 获取关注时间
     *
     * @return followTime - 关注时间
     */
    public Long getFollowTime() {
        return followTime;
    }

    /**
     * 设置关注时间
     *
     * @param followTime 关注时间
     */
    public void setFollowTime(Long followTime) {
        this.followTime = followTime;
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