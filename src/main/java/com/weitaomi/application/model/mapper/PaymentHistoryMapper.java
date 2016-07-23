package com.weitaomi.application.model.mapper;

import com.weitaomi.application.model.bean.PaymentHistory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PaymentHistoryMapper extends IBaseMapper<PaymentHistory> {
    String getMaxPayCode();
    PaymentHistory getPaymentHistory(@Param("payCode") String payCode);
    Integer batchInsertPayHistory(@Param("paymentHistoryList") List<PaymentHistory> paymentHistoryList);
    Integer batchUpdatePayHistory(@Param("batchNo")String batchNo, @Param("createTime") Long createTime);
    Integer batchUpdatePayHistoryByTradeNo(@Param("batchNo")List<String> batchNo, @Param("createTime") Long createTime);
}