package com.weitaomi.application.model.mapper;

import com.weitaomi.application.model.bean.PaymentHistory;
import org.apache.ibatis.annotations.Param;

public interface PaymentHistoryMapper extends IBaseMapper<PaymentHistory> {
    String getMaxPayCode();
    PaymentHistory getPaymentHistory(@Param("payCode") String payCode);
}