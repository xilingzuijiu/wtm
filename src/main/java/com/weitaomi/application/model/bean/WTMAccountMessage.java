package com.weitaomi.application.model.bean;

import com.weitaomi.application.model.BaseModel;

import java.util.Date;
import javax.persistence.*;

@Table(name = "wtm_weitaomi_accounts_message")
public class WTMAccountMessage extends BaseModel{
    @Column(name = "expires_in")
    private Integer expires_in;
    @Column(name = "token_time")
    private Date token_time;

    /**
     * 原始ID
     */
    @Column(name = "ysid")
    private String ysid;

    /**
     * AppID
     */
    @Column(name = "appid")
    private String appid;
    @Column(name = "appSecret")
    private String appSecret;
    @Column(name = "token")
    private String token;

    @Column(name = "EncodingAESKey")
    private String encodingAESKey;
    @Column(name = "access_token")
    private String access_token;



    /**
     * @return expires_in
     */
    public Integer getExpires_in() {
        return expires_in;
    }

    /**
     * @param expires_in
     */
    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
    }

    /**
     * @return token_time
     */
    public Date getToken_time() {
        return token_time;
    }

    /**
     * @param token_time
     */
    public void setToken_time(Date token_time) {
        this.token_time = token_time;
    }

    /**
     * 获取原始ID
     *
     * @return ysid - 原始ID
     */
    public String getYsid() {
        return ysid;
    }

    /**
     * 设置原始ID
     *
     * @param ysid 原始ID
     */
    public void setYsid(String ysid) {
        this.ysid = ysid;
    }

    /**
     * 获取AppID
     *
     * @return appid - AppID
     */
    public String getAppid() {
        return appid;
    }

    /**
     * 设置AppID
     *
     * @param appid AppID
     */
    public void setAppid(String appid) {
        this.appid = appid;
    }

    /**
     * @return appSecret
     */
    public String getAppSecret() {
        return appSecret;
    }

    /**
     * @param appSecret
     */
    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    /**
     * @return token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return EncodingAESKey
     */
    public String getEncodingAESKey() {
        return encodingAESKey;
    }

    /**
     * @param encodingAESKey
     */
    public void setEncodingAESKey(String encodingAESKey) {
        this.encodingAESKey = encodingAESKey;
    }

    /**
     * @return access_token
     */
    public String getAccess_token() {
        return access_token;
    }

    /**
     * @param access_token
     */
    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}