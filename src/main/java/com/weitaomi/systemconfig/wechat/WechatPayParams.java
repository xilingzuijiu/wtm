package com.weitaomi.systemconfig.wechat;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by Administrator on 2016/8/30.
 */
@XStreamAlias("xml")
public class WechatPayParams {
    /**
     * 公众账号appid
     */
    @XStreamAlias("appid")
    private String appid;
    /**
     * 商户号
     */
    @XStreamAlias("mch_id")
    private String mch_id;
    /**
     * 商品描述
     */
    @XStreamAlias("body")
    private String body;
    /**
     * 商户订单号
     */
    @XStreamAlias("out_trade_no")
    private String out_trade_no;
    /**
     * 签名
     */
    @XStreamAlias("sign")
    private String sign;
    /**
     * 随机字符串
     */
    @XStreamAlias("nonce_str")
    private String nonce_str;
    /**
     * 总金额
     */
    @XStreamAlias("total_fee")
    private String total_fee;
    /**
     * 通知地址
     */
    @XStreamAlias("notify_url")
    private String notify_url;

    /**
     * 交易类型
     */
    @XStreamAlias("trade_type")
    private String trade_type;
    /**
     * Ip地址
     */
    @XStreamAlias("spbill_create_ip")
    private String spbill_create_ip;

    /**
     * 获取公众账号appid
     * @return appid 公众账号appid
     */
    public String getAppid() {
        return this.appid;
    }

    /**
     * 设置公众账号appid
     * @param appid 公众账号appid
     */
    public void setAppid(String appid) {
        this.appid = appid;
    }

    /**
     * 获取商品描述
     * @return body 商品描述
     */
    public String getBody() {
        return this.body;
    }

    /**
     * 设置商品描述
     * @param body 商品描述
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * 获取商户订单号
     * @return out_trade_no 商户订单号
     */
    public String getOut_trade_no() {
        return this.out_trade_no;
    }

    /**
     * 设置商户订单号
     * @param out_trade_no 商户订单号
     */
    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    /**
     * 获取签名
     * @return sign 签名
     */
    public String getSign() {
        return this.sign;
    }

    /**
     * 设置签名
     * @param sign 签名
     */
    public void setSign(String sign) {
        this.sign = sign;
    }

    /**
     * 获取总金额
     * @return total_fee 总金额
     */
    public String getTotal_fee() {
        return this.total_fee;
    }

    /**
     * 设置总金额
     * @param total_fee 总金额
     */
    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    /**
     * 获取通知地址
     * @return notify_url 通知地址
     */
    public String getNotify_url() {
        return this.notify_url;
    }

    /**
     * 设置通知地址
     * @param notify_url 通知地址
     */
    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    /**
     * 获取交易类型
     * @return trade_type 交易类型
     */
    public String getTrade_type() {
        return this.trade_type;
    }

    /**
     * 设置交易类型
     * @param trade_type 交易类型
     */
    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    /**
     * 获取Ip地址
     * @return spbill_create_ip Ip地址
     */
    public String getSpbill_create_ip() {
        return this.spbill_create_ip;
    }

    /**
     * 设置Ip地址
     * @param spbill_create_ip Ip地址
     */
    public void setSpbill_create_ip(String spbill_create_ip) {
        this.spbill_create_ip = spbill_create_ip;
    }

    /**
     * 获取随机字符串
     * @return nonce_str 随机字符串
     */
    public String getNonce_str() {
        return this.nonce_str;
    }

    /**
     * 设置随机字符串
     * @param nonce_str 随机字符串
     */
    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    /**
     * 获取商户号
     * @return mch_id 商户号
     */
    public String getMch_id() {
        return this.mch_id;
    }

    /**
     * 设置商户号
     * @param mch_id 商户号
     */
    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }
}
