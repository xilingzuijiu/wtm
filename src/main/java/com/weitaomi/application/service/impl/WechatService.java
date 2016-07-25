package com.weitaomi.application.service.impl;

import com.weitaomi.application.service.interf.IPayStrategyService;

import java.util.Map;

/**
 * Created by supumall on 2016/7/21.
 */
public class WechatService implements IPayStrategyService {
    @Override
    public String getPaymentParams(Map<String, Object> params) {
        return null;
    }

    @Override
    public String getBatchPayParams(Map<String, String> params) {
        return null;
    }
}
