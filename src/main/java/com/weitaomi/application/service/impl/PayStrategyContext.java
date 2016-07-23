package com.weitaomi.application.service.impl;

import com.weitaomi.application.service.interf.IPayStrategyContext;
import com.weitaomi.application.service.interf.IPayStrategyService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by supumall on 2016/7/21.
 */
@Service
public class PayStrategyContext implements IPayStrategyContext {
    private IPayStrategyService payStrategyService;
    @Override
    public String getPaymentParams(Map<String, Object> params) {
        payStrategyService = StrategyFactory.getFactory().getPayStrategy((Integer)params.get("payType"));
        return payStrategyService.getPaymentParams(params);
    }
    @Override
    public String getBatchPayParams(Map<String, String> params, Integer payType) {
        payStrategyService = StrategyFactory.getFactory().getPayStrategy(payType);
        return payStrategyService.getBatchPayParams(params);
    }
}
