package com.weitaomi.application.service.impl;

import com.weitaomi.application.service.interf.IPayStrategyService;
import com.weitaomi.systemconfig.alipay.AlipayConfig;
import com.weitaomi.systemconfig.alipay.AlipaySubmit;
import com.weitaomi.systemconfig.exception.BusinessException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by supumall on 2016/7/21.
 */
@Service
public class AlipayService implements IPayStrategyService {

    @Override
    public String getPaymentParams(Map<String, Object> params) {
        Map<String,String> parameters=new HashMap<String, String>();
        parameters.put("partner", AlipayConfig.partner);
        parameters.put("seller_id",AlipayConfig.seller_id);
        parameters.put("out_trade_no",(String)params.get("trade_no"));
        parameters.put("subject","微淘米在线充值");
        parameters.put("body","微淘米在线充值");
        Double amount=Double.valueOf((String)params.get("amount"));
        parameters.put("total_fee",amount.toString());
        parameters.put("notify_url",AlipayConfig.notify_url);
        parameters.put("service",AlipayConfig.service);
        parameters.put("payment_type","1");
        parameters.put("_input_charset",AlipayConfig.input_charset);
        parameters.put("it_b_pay","15m");
        String requestParam= AlipaySubmit.buildRequestParams(parameters);
        if (!StringUtils.isEmpty(requestParam)){
            return requestParam;
        }
        return null;
    }

    @Override
    public String getBatchPayParams(Map<String, String> params) {
        if (params.isEmpty()){
            throw new BusinessException("支付业务参数为空");
        }
        params.put("service",AlipayConfig.batchPayservice);
        params.put("partner",AlipayConfig.partner);
        params.put("_input_charset",AlipayConfig.input_charset);
        params.put("notify_url",AlipayConfig.batchPay_notify_url);
        params.put("account_name",AlipayConfig.seller_name);
        params.put("email",AlipayConfig.seller_id);
        try {
            String result=AlipaySubmit.buildRequest("","",params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
