package com.weitaomi.application.model.mapper;

import com.weitaomi.application.model.bean.PaymentApprove;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface PaymentApproveMapper extends IBaseMapper<PaymentApprove> {
    List<PaymentApprove> getPaymentApproveList(@Param("rowBounds") RowBounds rowBounds);
}