package com.weitaomi.application.controller;

import com.alibaba.fastjson.JSON;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.Dom4JDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.weitaomi.application.service.interf.IPaymentService;
import com.weitaomi.systemconfig.alipay.AlipayNotification;
import com.weitaomi.systemconfig.alipay.AlipayNotify;
import com.weitaomi.systemconfig.alipay.HttpRequest;
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
import java.util.HashMap;
import java.util.Iterator;
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
    @RequestMapping(value = "/verifyAlipayNotify", method = RequestMethod.POST)
    public void  verifyAlipayNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //获取支付宝POST过来反馈信息
        logger.info("支付回调开始");
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
        //商户订单号

        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

        //支付宝交易号

        String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

        //交易状态
        String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//

        if(AlipayNotify.verify(params)){//验证成功
            //////////////////////////////////////////////////////////////////////////////////////////
            //请在这里加上商户的业务逻辑程序代码

            //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

            if(trade_status.equals("TRADE_FINISHED")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                //如果有做过处理，不执行商户的业务程序
                logger.info("验证签名成功");
                //注意：
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            } else if (trade_status.equals("TRADE_SUCCESS")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                //如果有做过处理，不执行商户的业务程序
                logger.info("验证签名失败");
                //注意：
                //付款完成后，支付宝系统发送该交易状态通知
            }

            //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

            response.getOutputStream().print("success");	//请不要修改或删除

            //////////////////////////////////////////////////////////////////////////////////////////
        }else{//验证失败
            response.getOutputStream().print("fail");
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
