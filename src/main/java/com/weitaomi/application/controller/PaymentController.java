package com.weitaomi.application.controller;

import com.weitaomi.application.controller.baseController.BaseController;
import com.weitaomi.application.service.interf.IPaymentService;
import com.weitaomi.systemconfig.exception.BusinessException;
import com.weitaomi.systemconfig.exception.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by supumall on 2016/7/22.
 */
@Controller
@RequestMapping(value = "/app/admin/payment")
public class PaymentController extends BaseController{
    @Autowired
    private IPaymentService paymentService;
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
}
