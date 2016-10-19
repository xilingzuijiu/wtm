package com.weitaomi.application.controller;

import com.alibaba.fastjson.JSON;
import com.weitaomi.application.controller.baseController.BaseController;
import com.weitaomi.application.model.bean.MemberPayAccounts;
import com.weitaomi.application.model.bean.PaymentApprove;
import com.weitaomi.application.model.dto.RequestFrom;
import com.weitaomi.application.model.enums.PayType;
import com.weitaomi.application.service.interf.IPaymentService;
import com.weitaomi.systemconfig.alipay.AlipayConfig;
import com.weitaomi.systemconfig.dataFormat.AjaxResult;
import com.weitaomi.systemconfig.exception.BusinessException;
import com.weitaomi.systemconfig.exception.SystemException;
import com.weitaomi.systemconfig.util.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by supumall on 2016/7/22.
 */
@Controller
@RequestMapping("/app/admin/payment")
public class PaymentController extends BaseController{
    @Autowired
    private IPaymentService paymentService;
    @ResponseBody
    @RequestMapping(value = "/verifyBatchPayNotify", method = RequestMethod.POST)
    public void  verifyBatchPayNotify(HttpServletRequest request, HttpServletResponse response) throws SystemException,BusinessException{
        Map map=request.getParameterMap();
        String code=paymentService.verifyBatchPayNotify(map);
        try {
            response.getOutputStream().println(code);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @ResponseBody
    @RequestMapping(value = "/getPaymentParams", method = RequestMethod.POST)
    public AjaxResult getPaymentParams(HttpServletRequest request,@RequestBody Map<String,Object> params){
        Long memberId=this.getUserId(request);
        RequestFrom requestFrom=this.getRequestFrom(request);
        if (memberId==null){
            throw new BusinessException("用户ID为空");
        }
        params.put("memberId",memberId);
        if ((Integer)params.get("payType")==(PayType.WECHAT_APP.getValue())){
            params.put("spbill_create_ip", IpUtils.getIpAddr(request));
        }
        String paramString=paymentService.getPaymentParams(params);
        if ((Integer)params.get("payType")==(PayType.WECHAT_APP.getValue())){
            Map map= JSON.parseObject(paramString);
            return AjaxResult.getOK(map);
        }
        if ((Integer)params.get("payType")==(PayType.ALIPAY_APP.getValue())){
            return AjaxResult.getOK(paramString);
        }
        return AjaxResult.getOK();
    }

    @ResponseBody
    @RequestMapping(value = "/patchWechatCustomers", method = RequestMethod.POST)
    public AjaxResult patchWechatCustomers(@RequestBody List<PaymentApprove> approveList){
        paymentService.patchAliPayCustomers(approveList);
        return AjaxResult.getOK();
    }
    @ResponseBody
    @RequestMapping(value = "/generatorPayParams", method = RequestMethod.POST)
    public AjaxResult generatorPayParams(HttpServletRequest request,@RequestBody PaymentApprove approve){
        Long memberId=this.getUserId(request);
        return AjaxResult.getOK(paymentService.generatorPayParams(memberId,approve));
    }
    @ResponseBody
    @RequestMapping(value = "/getMemberWalletInfo", method = RequestMethod.POST)
    public AjaxResult getMemberWalletInfo(HttpServletRequest request, @RequestParam(required = false,defaultValue ="20") Integer pageSize, @RequestParam(required = false,defaultValue ="0")Integer pageIndex){
        Long memberId=this.getUserId(request);
        if (memberId==null){
            throw new BusinessException("用户ID为空");
        }
        return AjaxResult.getOK(paymentService.getMemberWalletInfo(memberId,pageSize,pageIndex));
    }
    @ResponseBody
    @RequestMapping(value = "/savePayAccounts", method = RequestMethod.POST)
    public AjaxResult savePayAccounts(HttpServletRequest request,Integer payType,String payAccount,String realName){
        Long memberId=this.getUserId(request);
        if (memberId==null){
            throw new BusinessException("用户ID为空");
        }
        return AjaxResult.getOK(paymentService.savePayAccounts(memberId,payType,payAccount,realName));
    }
}
