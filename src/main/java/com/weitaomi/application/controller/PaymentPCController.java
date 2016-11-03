package com.weitaomi.application.controller;

import com.alibaba.fastjson.JSON;
import com.weitaomi.application.controller.baseController.BaseController;
import com.weitaomi.application.model.bean.PaymentApprove;
import com.weitaomi.application.model.dto.RequestFrom;
import com.weitaomi.application.model.enums.PayType;
import com.weitaomi.application.service.interf.IMemberScoreService;
import com.weitaomi.application.service.interf.IPaymentService;
import com.weitaomi.systemconfig.dataFormat.AjaxResult;
import com.weitaomi.systemconfig.exception.BusinessException;
import com.weitaomi.systemconfig.util.IpUtils;
import com.weitaomi.systemconfig.wechat.WechatConfig;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/11.
 */
@Controller
@RequestMapping("/pc/admin/payment")
public class PaymentPCController extends BaseController{
    @Autowired
    private IPaymentService paymentService;
    @Autowired
    private IMemberScoreService memberScoreService;
    @ResponseBody
    @RequestMapping(value = "/getMemberWalletInfo", method = RequestMethod.GET)
    public AjaxResult getMemberWalletInfo(HttpServletRequest request, @RequestParam(required = false,defaultValue ="20") Integer pageSize, @RequestParam(required = false,defaultValue ="0")Integer pageIndex){
        Long memberId=this.getUserId(request);
        if (memberId==null){
            throw new BusinessException("用户ID为空");
        }
        return AjaxResult.getOK(paymentService.getMemberWalletInfo(memberId,pageSize,pageIndex));
    }
    /**
     * 更新积分记录
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateMemberScore",method = RequestMethod.POST)
    AjaxResult updateMemberScore(HttpServletRequest request){
        Long memberId=this.getUserId(request);
        if (memberId==null){
            throw new BusinessException("用户ID为空");
        }
        return AjaxResult.getOK(memberScoreService.getMemberScoreById(memberId));
    }
    @ResponseBody
    @RequestMapping(value = "/generatorPayParams", method = RequestMethod.POST)
    public AjaxResult generatorPayParams(HttpServletRequest request,@RequestBody PaymentApprove approve){
        Long memberId=this.getUserId(request);
        return AjaxResult.getOK(paymentService.generatorPayParams(memberId,approve,1));
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
        String paramString=paymentService.getPaymentParams(params);
        if ((Integer)params.get("payType")==(PayType.WECHAT_WEB.getValue())){
            Map map= JSON.parseObject(paramString);
            String pre_sign="appId="+map.get("appid")+"&nonceStr="+map.get("noncestr")+"&package=prepay_id="+map.get("prepayid")+"&timeStamp="+map.get("timestamp")+"&key="+ WechatConfig.API_KEY;
            map.put("paySign",DigestUtils.md5Hex(pre_sign).toUpperCase());
            return AjaxResult.getOK(map);
        }
        return AjaxResult.getOK();
    }

    @ResponseBody
    @RequestMapping(value = "/getPCPaymentParams", method = RequestMethod.POST)
    public AjaxResult getPCPaymentParams(HttpServletRequest request,@RequestBody Map<String,Object> params){
        Long memberId=this.getUserId(request);
        RequestFrom requestFrom=this.getRequestFrom(request);
        if (memberId==null){
            throw new BusinessException("用户ID为空");
        }
        params.put("memberId",memberId);
        if ((Integer)params.get("payType")==(PayType.WECHAT_APP.getValue())||(Integer)params.get("payType")==(PayType.WECHAT_WEB.getValue())||(Integer)params.get("payType")==(PayType.WECHAT_PC.getValue())){
            params.put("spbill_create_ip", IpUtils.getIpAddr(request));
        }
        String paramString=paymentService.getPaymentParams(params);
        if ((Integer)params.get("payType")==(PayType.WECHAT_APP.getValue())||(Integer)params.get("payType")==(PayType.WECHAT_WEB.getValue())||(Integer)params.get("payType")==(PayType.WECHAT_PC.getValue())){
            Map map= JSON.parseObject(paramString);
            return AjaxResult.getOK(map);
        }
        if ((Integer)params.get("payType")==(PayType.ALIPAY_APP.getValue())){
            return AjaxResult.getOK(paramString);
        }
        return AjaxResult.getOK();
    }

    @ResponseBody
    @RequestMapping(value = "/patchAliPayCustomers", method = RequestMethod.POST)
    public AjaxResult patchAliPayCustomers(@RequestBody List<PaymentApprove> approveList){
        paymentService.patchAliPayCustomers(approveList);
        return AjaxResult.getOK();
    }
}
