package com.weitaomi.application.service.interf;

import com.weitaomi.application.model.bean.MemberPayAccounts;
import com.weitaomi.application.model.bean.MemberScore;
import com.weitaomi.application.model.bean.PaymentApprove;
import com.weitaomi.application.model.dto.MemberScoreFlowDto;
import com.weitaomi.application.model.dto.MyWalletDto;
import com.weitaomi.application.model.dto.RequireFollowerParamsDto;
import com.weitaomi.systemconfig.util.Page;

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
    MemberScore generatorPayParams(Long memberId, PaymentApprove approve);

    /**
     * 获取钱包信息
     * @param memberId
     * @return
     */
    public Page<MemberScoreFlowDto> getMemberWalletInfo(Long memberId, Integer paygeSize, Integer pageIndex);

    /**
     * 上传或者更新支付账号信息
     * @param memberId
     * @param
     * @return
     */
    Boolean savePayAccounts(Long memberId,Integer payType,String payAccount,String realName);
}
