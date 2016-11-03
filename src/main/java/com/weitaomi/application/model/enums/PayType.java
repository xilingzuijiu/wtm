package com.weitaomi.application.model.enums;

/**
 * Created by supumall on 2016/7/21.
 */
public enum PayType {
    ALIPAY_APP(1,"支付宝手机APP支付"),
    ALIPAY_WEB(2,"支付宝网页支付"),
    WECHAT_APP(3,"微信app支付"),
    WECHAT_WEB(4,"微信Wap支付"),
    WECHAT_PC(5,"微信PC支付");
    private Integer value;
    private String desc;

    PayType(Integer value,String desc) {
        this.desc = desc;
        this.value = value;
    }
    public static PayType getValue(Integer value){
        for (PayType type : PayType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return null;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
