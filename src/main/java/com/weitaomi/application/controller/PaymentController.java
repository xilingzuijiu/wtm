package com.weitaomi.application.controller;

import com.weitaomi.application.controller.baseController.BaseController;
import com.weitaomi.application.model.bean.PaymentApprove;
import com.weitaomi.application.service.interf.IPaymentService;
import com.weitaomi.systemconfig.dataFormat.AjaxResult;
import com.weitaomi.systemconfig.exception.BusinessException;
import com.weitaomi.systemconfig.exception.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @RequestMapping(value = "/verifyAlipayNotify", method = RequestMethod.POST)
    public void  verifyAlipayNotify(HttpServletRequest request, HttpServletResponse response) throws SystemException,BusinessException{
        Map map=request.getParameterMap();
        String code=paymentService.verifyAlipayNotify(map);
        try {
            response.getOutputStream().println(code);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
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
        if (memberId==null){
            throw new BusinessException("用户ID为空");
        }
        params.put("memberId",memberId);
        return AjaxResult.getOK(paymentService.getPaymentParams(params));
    }
    @ResponseBody
    @RequestMapping(value = "/patchAliPayCustomers", method = RequestMethod.POST)
    public AjaxResult patchAliPayCustomers(@RequestBody List<PaymentApprove> approveList){
        paymentService.patchAliPayCustomers(approveList);
        return AjaxResult.getOK();
    }
    @ResponseBody
    @RequestMapping(value = "/getMemberWalletInfo", method = RequestMethod.POST)
    public AjaxResult getMemberWalletInfo(HttpServletRequest request){
        Long memberId=this.getUserId(request);
        if (memberId==null){
            throw new BusinessException("用户ID为空");
        }
        return AjaxResult.getOK(paymentService.getMemberWalletInfo(memberId));
    }
}
