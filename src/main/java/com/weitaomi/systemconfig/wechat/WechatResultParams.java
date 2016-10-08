package com.weitaomi.systemconfig.wechat;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Created by Administrator on 2016/8/31.
 */
@XStreamAlias("xml")
public class WechatResultParams {
    @XStreamAlias("return_code")
    private String return_code;
    @XStreamAlias("return_msg")
    private String return_msg;
    @XStreamAlias("appid")
    private String appid;
    @XStreamAlias("mch_id")
    private String mch_id;
    @XStreamAlias("nonce_str")
    private String nonce_str;
    @XStreamAlias("sign")
    private String sign;
    @XStreamAlias("result_code")
    private String result_code;
    @XStreamAlias("prepay_id")
    private String prepay_id;
    @XStreamAlias("trade_type")
    private String trade_type;
    @XStreamAlias("err_code")
    private String err_code;
    @XStreamAlias("err_code_des")
    private String err_code_des;
    @XStreamOmitField
    private Long timestamp;


    public String getReturn_code() {
        return this.return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return this.return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    public String getAppid() {
        return this.appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return this.mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getNonce_str() {
        return this.nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return this.sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getResult_code() {
        return this.result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getPrepay_id() {
        return this.prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public String getTrade_type() {
        return this.trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getErr_code() {
        return this.err_code;
    }

    public void setErr_code(String err_code) {
        this.err_code = err_code;
    }

    public String getErr_code_des() {
        return this.err_code_des;
    }

    public void setErr_code_des(String err_code_des) {
        this.err_code_des = err_code_des;
    }
}
