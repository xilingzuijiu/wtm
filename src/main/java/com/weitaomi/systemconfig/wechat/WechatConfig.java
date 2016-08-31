package com.weitaomi.systemconfig.wechat;

/**
 * Created by supumall on 2016/7/22.
 */
public class WechatConfig {
    //appid
    //请同时修改  androidmanifest.xml里面，.PayActivityd里的属性<data android:scheme="wxb4ba3c02aa476ea1"/>为新设置的appid
    public static final String APP_ID = "wx6c1a0b98c250315e";
    //公众号APPID
    public static final String  MCH_APPID="wx2282c57276f83fad";
    //商户号
    public static final String  MCHID="1368374602";
    //  API密钥，在商户平台设置
    public static final String API_KEY = "0510weitaomi1138131xxxx2303woyun";
    //预支付接口
    public static final String PRE_PAY_URL= "https://api.mch.weixin.qq.com/pay/unifiedorder";
    //微信回调地址
    public static final String NOTIFY_URL= "http://121.42.196.236:8018/weitaomi/app/admin/payment/verifyWechatNotify";
    public static final String SUCCESS= "SUCCESS";
    public static final String FAIL= "FAILURE";


}
