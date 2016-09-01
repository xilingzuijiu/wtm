package com.weitaomi.application.controller;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.Dom4JDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.weitaomi.application.service.interf.IPaymentService;
import com.weitaomi.systemconfig.exception.BusinessException;
import com.weitaomi.systemconfig.exception.SystemException;
import com.weitaomi.systemconfig.util.HttpRequestUtils;
import com.weitaomi.systemconfig.util.StreamUtils;
import com.weitaomi.systemconfig.wechat.WechatBatchPayParams;
import com.weitaomi.systemconfig.wechat.WechatNotifyParams;
import org.dom4j.io.OutputFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/31.
 */
@Controller
@RequestMapping("/pc/admin/paymemberCallBack")
public class PaymemberCallBackController {
    private Logger logger= LoggerFactory.getLogger(PaymemberCallBackController.class);
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
    public String  verifyWechatNotify(HttpServletRequest request,HttpServletResponse response) throws SystemException,BusinessException{
        String params="";
        try {
            InputStream inputStream=request.getInputStream();
            params= HttpRequestUtils.readInstream(inputStream,"UTF-8");
            logger.info("beginning...{}",params);
        } catch (IOException e) {
            e.printStackTrace();
        }
        WechatNotifyParams wechatNotifyParams= StreamUtils.toBean(params,WechatNotifyParams.class);
        String code=paymentService.verifyWechatNotify(wechatNotifyParams);
        try {
            response.getOutputStream().print(code);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return code;
    }
}
