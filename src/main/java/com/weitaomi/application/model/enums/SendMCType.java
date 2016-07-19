package com.weitaomi.application.model.enums;

/**
 * 发送带模板格式的短信类型
 */
public enum SendMCType {

    /**
     * 注册
     */
    Register("Register"),


    /**
     * 验证手机
     */
    Validate("Validate"),

    /**
     * 找回密码
     */
    ReSetPassword("ReSetPassword"),

    /**
     * 修改密码
     */
    UpdatePassword("UpdatePassword"),

    /**
     * 下单成功
     */
    OrderCreate("OrderCreate"),

    /**
     * 支付成功
     */
    OrderPaySuccess("OrderPaySuccess"),

    /**
     * 正在配送
     */
    OrderDelivery("OrderDelivery"),

    /**
     * 签收成功
     */
    OrderSign("OrderSign"),

    /**
     * 申请退货
     */
    GoodsExchangeApply("GoodsExchangeApply"),
    /**
     * 申请退货
     */
    GoodsExchangeApplyFengChi("GoodsExchangeApplyFengChi"),

    /**
     * 退款已处理
     */
    BackMoneySuccess("BackMoneySuccess"),

    /**
     * 积分变动(购买商品）
     */
    PointsChangeBuy("PointsChangeBuy"),

    /**
     * 积分变动(评论）
     */
    PointsChangeComment("PointsChangeComment"),

    /**
     * 积分变动(兑换商品）
     */
    PointsChangeExChange("PointsChangeExChange"),

    /**
     * 优惠卷领取
     */
    TicketGet("TicketGet"),

    /**
     * 降价通知
     */
    PriceNotice("PriceNotice"),

    /**
     * 到货通知
     */
    ArrivalNotice("ArrivalNotice"),

    /**
     * 生日祝福赠劵积分
     */
    BirthdayGift("BirthdayGift"),

    /**
     * 积分兑换成功通知(育儿服务)
     */
    PointsExchangeSuccess("PointsExchangeSuccess"),

    /**
     * 亲子活动报名成功通知
     */
    FamilyActivityApply("FamilyActivityApply"),

    /**
     * 澳洲馆
     */
    PaySuccess_AU("PaySuccess_AU"),

    /**
     * 美国馆，德国馆
     */
    PaySuccess_US("PaySuccess_US"),

    /**
     * 重置绑定手机号
     */
    reSetCellPhone("reSetCellPhone"),
    /**
     * 用户调研信息
     */
    userInvestigateInfo("userInvestigateInfo"),

    /**
     * 大礼包申请通过通知
     */
    MotherGiftNotice("MotherGiftNotice");


    /**
     * The Type.
     */
    private String type;

    private SendMCType(String _type){
        type = _type;
    }

    public String getType() {
        return type;
    }

    public static SendMCType getByType(String type){
        for(SendMCType temp : SendMCType.values()){
            if(temp.getType().equals( type)){
                return temp;
            }
        }
        return null;
    }
}
