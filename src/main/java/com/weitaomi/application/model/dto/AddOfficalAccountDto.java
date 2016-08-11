package com.weitaomi.application.model.dto;

import java.util.List;

/**
 * Created by supumall on 2016/8/9.
 */
public class AddOfficalAccountDto {
    /**
     * 用户授权App获得的openId
     */
    private String openId;
    /**
     * 用户标识
     */
    private String unionId;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 公众号列表
     */
    private List<OfficialAccountMsg> linkList;

    /**
     * 获取用户授权App获得的openId
     * @return openId 用户授权App获得的openId
     */
    public String getOpenId() {
        return this.openId;
    }

    /**
     * 设置用户授权App获得的openId
     * @param openId 用户授权App获得的openId
     */
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    /**
     * 获取用户标识
     * @return unionId 用户标识
     */
    public String getUnionId() {
        return this.unionId;
    }

    /**
     * 设置用户标识
     * @param unionId 用户标识
     */
    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    /**
     * 获取昵称
     * @return nickname 昵称
     */
    public String getNickname() {
        return this.nickname;
    }

    /**
     * 设置昵称
     * @param nickname 昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 获取公众号列表
     * @return linkList 公众号列表
     */
    public List<OfficialAccountMsg> getLinkList() {
        return this.linkList;
    }

    /**
     * 设置公众号列表
     * @param linkList 公众号列表
     */
    public void setLinkList(List<OfficialAccountMsg> linkList) {
        this.linkList = linkList;
    }
}
