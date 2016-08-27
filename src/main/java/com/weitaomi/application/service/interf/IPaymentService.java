package com.weitaomi.application.service.interf;

import com.weitaomi.application.model.bean.MemberPayAccounts;
import com.weitaomi.application.model.bean.PaymentApprove;
import com.weitaomi.application.model.dto.MyWalletDto;
import com.weitaomi.application.model.dto.RequireFollowerParamsDto;

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
    boolean generatorPayParams(Long memberId,PaymentApprove approve);
    public MyWalletDto getMemberWalletInfo(Long memberId);

    /**
     * 上传或者更新支付账号信息
     * @param memberId
     * @param memberPayAccounts
     * @return
     */
    Boolean savePayAccounts(Long memberId, MemberPayAccounts memberPayAccounts);
}
