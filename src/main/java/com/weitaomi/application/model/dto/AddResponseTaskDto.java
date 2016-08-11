package com.weitaomi.application.model.dto;

/**
 * Created by supumall on 2016/8/11.
 */
public class AddResponseTaskDto {
    /**
     * 公号原始ID
     */
    private String originId;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 关注时间
     */
    private String time;

    /**
     * 获取公号原始ID
     * @return originId 公号原始ID
     */
    public String getOriginId() {
        return this.originId;
    }

    /**
     * 设置公号原始ID
     * @param originId 公号原始ID
     */
    public void setOriginId(String originId) {
        this.originId = originId;
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
     * 获取关注时间
     * @return time 关注时间
     */
    public String getTime() {
        return this.time;
    }

    /**
     * 设置关注时间
     * @param time 关注时间
     */
    public void setTime(String time) {
        this.time = time;
    }
}
