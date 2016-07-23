package com.weitaomi.application.service.interf;

import com.weitaomi.application.model.bean.PaymentApprove;

import java.util.List;
import java.util.Map;

/**
 * Created by supumall on 2016/7/22.
 */
public interface IPaymentService {
    String getPaymentParams(Map<String,Object> params);
    String verifyAlipayNotify(Map requestParams);
    String verifyBatchPayNotify(Map requestParams);
    void patchAliPayCustomers(List<PaymentApprove> approveList);
}
