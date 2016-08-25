package com.weitaomi.application.model.bean;

import com.weitaomi.application.model.BaseModel;

import javax.persistence.*;

@Table(name = "wtm_official_accounts_level")
public class OfficialAccountsLevel extends BaseModel {

    /**
     * 关注奖励
     */
    @Column(name = "followReward")
    private Integer followReward;

    /**
     * 等级名称
     */
    @Column(name = "levelName")
    private String levelName;

    /**
     * 创建日期
     */
    @Column(name = "createTime")
    private Long createTime;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取关注奖励
     *
     * @return followReward - 关注奖励
     */
    public Integer getFollowReward() {
        return followReward;
    }

    /**
     * 设置关注奖励
     *
     * @param followReward 关注奖励
     */
    public void setFollowReward(Integer followReward) {
        this.followReward = followReward;
    }

    /**
     * 获取等级名称
     *
     * @return levelName - 等级名称
     */
    public String getLevelName() {
        return levelName;
    }

    /**
     * 设置等级名称
     *
     * @param levelName 等级名称
     */
    public void setLevelName(String levelName) {
        this.levelName = levelName;
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