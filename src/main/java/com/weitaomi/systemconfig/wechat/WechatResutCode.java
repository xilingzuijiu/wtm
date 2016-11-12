package com.weitaomi.systemconfig.wechat;

/**
 * Created by Administrator on 2016/11/12.
 */
public enum WechatResutCode {
    NOAUTH("NOAUTH","没有权限"),AMOUNT_LIMIT("AMOUNT_LIMIT","付款金额不能小于最低限额"),PARAM_ERROR("PARAM_ERROR","参数错误"), OPENID_ERROR("OPENID_ERROR","Openid错误"),
    NOTENOUGH("NOTENOUGH","帐号余额不足"),SYSTEMERROR("SYSTEMERROR","系统繁忙，请稍后再试"),NAME_MISMATCH("NAME_MISMATCH","请求参数里填写了需要检验姓名，但是输入了错误的姓名"),
    SIGN_ERROR("SIGN_ERROR","签名错误"), XML_ERROR("XML_ERROR","Post内容出错"),FATAL_ERROR("FATAL_ERROR","两次请求参数不一致"),CA_ERROR("CA_ERROR","证书出错");
    private String id;
    private String value;
    WechatResutCode(String id, String value) {
        this.id = id;
        this.value = value;
    }
    public static WechatResutCode getValue(String id){
        for (WechatResutCode wechatResutCode : WechatResutCode.values()) {
            if (wechatResutCode.getId().equals(id)) {
                return wechatResutCode;
            }
        }
        return null;
    }
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
