package com.weitaomi.application.controller;

import com.weitaomi.application.service.interf.IPaymentService;
import com.weitaomi.systemconfig.exception.BusinessException;
import com.weitaomi.systemconfig.exception.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/31.
 */
@Controller
@RequestMapping("/app/admin/paymemberCallBack")
public class PaymemberCallBackController {
    @Autowired
    private IPaymentService paymentService;
    @ResponseBody
    @RequestMapping(value = "/verifyAlipayNotify", method = RequestMethod.POST)
    public void  verifyAlipayNotify(HttpServletRequest request, HttpServletResponse response) throws SystemException,BusinessException {
        Map map=request.getParameterMap();
        String code=paymentService.verifyAlipayNotify(map);
        try {
            response.getOutputStream().println(code);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @ResponseBody
    @RequestMapping(value = "/verifyWechatNotify", method = RequestMethod.POST)
    public void  verifyWechatNotify(HttpServletRequest request, HttpServletResponse response) throws SystemException,BusinessException{
        System.out.println("beginning...");
        Map map=request.getParameterMap();
        String code=paymentService.verifyWechatNotify(map);
        try {
            response.getOutputStream().println(code);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
