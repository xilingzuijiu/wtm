package com.weitaomi.application.service.impl;

import com.weitaomi.application.model.enums.PayType;
import com.weitaomi.application.service.interf.IPayStrategyService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by supumall on 2016/7/21.
 */
public class StrategyFactory {
    private static StrategyFactory factory=new StrategyFactory();
    private static Map<Integer, IPayStrategyService> map = new HashMap<Integer, IPayStrategyService>();
    static {
        map.put(PayType.ALIPAY_APP.getValue(),new AlipayService());
        map.put(PayType.WECHAT_APP.getValue(),new WechatPayService());
    }

    public static StrategyFactory getFactory() {
        return factory;
    }
    public IPayStrategyService getPayStrategy(Integer type){
        return map.get(type);
    }
}
