package com.weitaomi.systemconfig.wechat;

/**
 * Created by Administrator on 2016/8/30.
 */
public class WechatBatchPayParams {
    /**
     * 公众账号appid
     */
    private String mch_appid;
    /**
     * 商户号
     */
    private String mchid;
    /**
     * 设备号
     */
    private String nonce_str;
    /**
     * 随机字符串
     */
    private String device_info;
    /**
     * 签名
     */
    private String sign;
    /**
     * 商户订单号
     */
    private String partner_trade_no;
    /**
     * 用户openid
     */
    private String openid;
    /**
     * 校验用户姓名选项
     */
    private String check_nam="NO_CHECK";
    /**
     * 收款用户姓名
     */
    private String re_user_name;
    /**
     * 金额
     */
    private String amount;
    /**
     * 企业付款描述信息
     */
    private String desc;
    /**
     * Ip地址
     */
    private String spbill_create_ip;

    /**
     * 获取公众账号appid
     * @return mch_appid 公众账号appid
     */
    public String getMch_appid() {
        return this.mch_appid;
    }

    /**
     * 设置公众账号appid
     * @param mch_appid 公众账号appid
     */
    public void setMch_appid(String mch_appid) {
        this.mch_appid = mch_appid;
    }

    /**
     * 获取商户号
     * @return mchid 商户号
     */
    public String getMchid() {
        return this.mchid;
    }

    /**
     * 设置商户号
     * @param mchid 商户号
     */
    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    /**
     * 获取设备号
     * @return nonce_str 设备号
     */
    public String getNonce_str() {
        return this.nonce_str;
    }

    /**
     * 设置设备号
     * @param nonce_str 设备号
     */
    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    /**
     * 获取随机字符串
     * @return device_info 随机字符串
     */
    public String getDevice_info() {
        return this.device_info;
    }

    /**
     * 设置随机字符串
     * @param device_info 随机字符串
     */
    public void setDevice_info(String device_info) {
        this.device_info = device_info;
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
     * 获取商户订单号
     * @return partner_trade_no 商户订单号
     */
    public String getPartner_trade_no() {
        return this.partner_trade_no;
    }

    /**
     * 设置商户订单号
     * @param partner_trade_no 商户订单号
     */
    public void setPartner_trade_no(String partner_trade_no) {
        this.partner_trade_no = partner_trade_no;
    }

    /**
     * 获取用户openid
     * @return openid 用户openid
     */
    public String getOpenid() {
        return this.openid;
    }

    /**
     * 设置用户openid
     * @param openid 用户openid
     */
    public void setOpenid(String openid) {
        this.openid = openid;
    }

    /**
     * 获取校验用户姓名选项
     * @return check_nam 校验用户姓名选项
     */
    public String getCheck_nam() {
        return this.check_nam;
    }

    /**
     * 设置校验用户姓名选项
     * @param check_nam 校验用户姓名选项
     */
    public void setCheck_nam(String check_nam) {
        this.check_nam = check_nam;
    }

    /**
     * 获取收款用户姓名
     * @return re_user_name 收款用户姓名
     */
    public String getRe_user_name() {
        return this.re_user_name;
    }

    /**
     * 设置收款用户姓名
     * @param re_user_name 收款用户姓名
     */
    public void setRe_user_name(String re_user_name) {
        this.re_user_name = re_user_name;
    }

    /**
     * 获取金额
     * @return amount 金额
     */
    public String getAmount() {
        return this.amount;
    }

    /**
     * 设置金额
     * @param amount 金额
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

    /**
     * 获取企业付款描述信息
     * @return desc 企业付款描述信息
     */
    public String getDesc() {
        return this.desc;
    }

    /**
     * 设置企业付款描述信息
     * @param desc 企业付款描述信息
     */
    public void setDesc(String desc) {
        this.desc = desc;
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
}
