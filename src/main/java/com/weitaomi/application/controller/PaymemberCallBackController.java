package com.weitaomi.application.controller;

import com.alibaba.fastjson.JSON;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.Dom4JDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.weitaomi.application.service.interf.IPaymentService;
import com.weitaomi.systemconfig.alipay.AlipayNotification;
import com.weitaomi.systemconfig.alipay.AlipayNotify;
import com.weitaomi.systemconfig.alipay.RequestUtils;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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
    public void  verifyAlipayNotify(HttpServletRequest request,HttpServletResponse response) throws SystemException,BusinessException {
        Map<String, String> underScoreKeyMap = request.getParameterMap();
        Map<String, String> camelCaseKeyMap = RequestUtils.convertKeyToCamelCase(underScoreKeyMap);

        //首先验证调用是否来自支付宝
        boolean verifyResult = AlipayNotify.verify(underScoreKeyMap);

        try {

            String jsonString = JSON.toJSONString(camelCaseKeyMap);
            AlipayNotification notice = JSON.parseObject(jsonString, AlipayNotification.class);
            notice.setVerifyResult(verifyResult);

            String resultResponse = "success";
            PrintWriter printWriter = null;
            try {
                printWriter = response.getWriter();

                //do business
                if(verifyResult){
                    paymentService.verifyAlipayNotify(underScoreKeyMap);
                }
                //fail due to verification error
                else{
                    resultResponse = "fail";
                }

            } catch (Exception e) {
                logger.error("alipay notify error :", e);
                resultResponse = "fail";
                printWriter.close();
            }
            if (printWriter != null) {
                printWriter.print(resultResponse);
            }

        } catch (Exception e1) {

            e1.printStackTrace();
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
        logger.info("反馈给微信的接过为："+code);
        try {
            response.getOutputStream().println(code);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return code;
    }
}
