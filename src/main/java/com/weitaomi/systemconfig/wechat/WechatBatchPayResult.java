package com.weitaomi.systemconfig.wechat;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by Administrator on 2016/9/1.
 */
@XStreamAlias("xml")
public class WechatBatchPayResult {
    private String return_code;
    private String return_msg;
    private String mch_appid;
    private String mchid;
    private String nonce_str;
    private String result_code;
    private String partner_trade_no;
    private String payment_no;
    private String payment_time;

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

    public String getMch_appid() {
        return this.mch_appid;
    }

    public void setMch_appid(String mch_appid) {
        this.mch_appid = mch_appid;
    }

    public String getMchid() {
        return this.mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    public String getNonce_str() {
        return this.nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getResult_code() {
        return this.result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getPartner_trade_no() {
        return this.partner_trade_no;
    }

    public void setPartner_trade_no(String partner_trade_no) {
        this.partner_trade_no = partner_trade_no;
    }

    public String getPayment_no() {
        return this.payment_no;
    }

    public void setPayment_no(String payment_no) {
        this.payment_no = payment_no;
    }

    public String getPayment_time() {
        return this.payment_time;
    }

    public void setPayment_time(String payment_time) {
        this.payment_time = payment_time;
    }
}
