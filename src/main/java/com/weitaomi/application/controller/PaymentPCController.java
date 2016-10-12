package com.weitaomi.application.controller;

import com.weitaomi.application.controller.baseController.BaseController;
import com.weitaomi.application.model.bean.PaymentApprove;
import com.weitaomi.application.service.interf.IMemberScoreService;
import com.weitaomi.application.service.interf.IPaymentService;
import com.weitaomi.systemconfig.dataFormat.AjaxResult;
import com.weitaomi.systemconfig.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
        return AjaxResult.getOK(paymentService.generatorPayParams(memberId,approve));
    }
}
