package com.weitaomi.application.service.interf;

import java.util.Map;

/**
 * Created by supumall on 2016/7/21.
 */
public interface IPayStrategyContext {
    public String getPaymentParams(Map<String, Object> params);

    String getBatchPayParams(Map<String, String> paramsm, Integer payType);
}
