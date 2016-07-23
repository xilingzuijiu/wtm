package com.weitaomi.application.service.impl;

import com.weitaomi.application.model.bean.PaymentApprove;
import com.weitaomi.application.model.bean.PaymentHistory;
import com.weitaomi.application.model.enums.PayType;
import com.weitaomi.application.model.mapper.PaymentHistoryMapper;
import com.weitaomi.application.service.interf.ICacheService;
import com.weitaomi.application.service.interf.IPayStrategyContext;
import com.weitaomi.application.service.interf.IPaymentService;
import com.weitaomi.systemconfig.alipay.AlipayNotify;
import com.weitaomi.systemconfig.exception.BusinessException;
import com.weitaomi.systemconfig.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by supumall on 2016/7/22.
 */
@Service
public class PaymentService implements IPaymentService {
    private Logger logger= LoggerFactory.getLogger(PaymentService.class);
    @Autowired
    private ICacheService cacheService;
    @Autowired
    private PaymentHistoryMapper paymentHistoryMapper;
    @Autowired
    private IPayStrategyContext payStrategyContext;
    @Override
    public String getPaymentParams(Map<String,Object> params) {
        String payCode=this.getTradeNo();
        params.put("trade_no",payCode);
        String requestParam=payStrategyContext.getPaymentParams(params);
        PaymentHistory paymentHistory=new PaymentHistory();
        paymentHistory.setMemberId((Long)params.get("memberId"));
        paymentHistory.setPayCode(payCode);
        paymentHistory.setPlatform((String)params.get("payPlatform"));
        paymentHistory.setParams(requestParam);
        paymentHistory.setIsPaySuccess(0);
        paymentHistory.setPayType(0);
        paymentHistoryMapper.insertSelective(paymentHistory);
        if (!StringUtils.isEmpty(requestParam)){
            return requestParam;
        }
        return null;
    }

    @Override
    public String verifyAlipayNotify(Map requestParams) {
        Map params = new HashMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
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
        String out_trade_no = null;
        //支付宝交易号
        String trade_no = null;
        //交易状态
        String trade_status = null;
        try {
            out_trade_no = new String(params.get("out_trade_no").toString().getBytes("ISO-8859-1"), "UTF-8");
            trade_no = new String(params.get("trade_no").toString().getBytes("ISO-8859-1"), "UTF-8");
            trade_status = new String(params.get("trade_status").toString().getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//

        if (AlipayNotify.verify(params)) {//验证成功
            //请在这里加上商户的业务逻辑程序代码
            //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
            if (trade_status.equals("TRADE_FINISHED")) {

                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序
                //注意：即时到账接口不会返回这个状态
                //注意：
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            } else if (trade_status.equals("TRADE_SUCCESS")) {
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序
                //注意：
                //付款完成后，支付宝系统发送该交易状态通知
                //——请根据您的业务逻辑来编写程序（以上代码仅作参考）
                PaymentHistory paymentHistory=paymentHistoryMapper.getPaymentHistory(out_trade_no);
                if (paymentHistory!=null){
                    String sign=(String)requestParams.get("sign");
                    if (sign.equals(paymentHistory.getParams())) {
                        paymentHistory.setParams(sign);
                        paymentHistory.setIsPaySuccess(1);
                        paymentHistory.setCreateTime(DateUtils.getUnixTimestamp());
                        paymentHistoryMapper.updateByPrimaryKeySelective(paymentHistory);
                        return "success";
                    }
                    return "fail";
                }
                return "fail";
            }
        } else {//验证失败
            return "fail";
        }
        return "fail";
    }

    @Override
    public void patchAliPayCustomers(List<PaymentApprove> approveList) {
        if (approveList.isEmpty()){
            throw new BusinessException("付款账户列表为空");
        }
        Map<String,String> params=new HashMap<String, String>();
        BigDecimal totalAmount=BigDecimal.ZERO;
        StringBuffer detail_data=new StringBuffer();
        for(PaymentApprove approve:approveList){
            totalAmount=totalAmount.add(approve.getAmount());
            detail_data.append(this.getTradeNo());
            if (!StringUtils.isEmpty(approve.getAccountNumber())&&!StringUtils.isEmpty(approve.getAccountName())&&approve.getAmount()!=null){
                detail_data.append("^").append(approve.getAccountNumber()).append("^").append(approve.getAccountName()).append("^").append(approve.getAmount());
            }
            if (!StringUtils.isEmpty(approve.getRemark())){
                detail_data.append("^").append(approve.getRemark()).append("|");
            }
        }
        params.put("batch_num",String.valueOf(approveList.size()));
        params.put("batch_fee",totalAmount.toString());
        params.put("pay_date",DateUtils.formatYYYY());
        params.put("batch_no",DateUtils.formatYYYYMMddHHmmssSSS());
        params.put("detail_data",detail_data.substring(0,approveList.size()-1));
        String result=payStrategyContext.getBatchPayParams(params, PayType.ALIPAY_APP.getValue());
    }
    private String getTradeNo(){
        String key="wtm:orderCode:max";
        String payCode= cacheService.getCacheByKey(key,String.class);
        if (StringUtils.isEmpty(payCode)){
            payCode=paymentHistoryMapper.getMaxPayCode();
            if (StringUtils.isEmpty(payCode)){
                payCode="1000000000000000";
                Integer orderNumer=Integer.valueOf(payCode)+1;
                cacheService.setCacheByKey(key,orderNumer.toString(),null);
            }else {
                Integer orderNumer = Integer.valueOf(payCode) + 1;
                cacheService.setCacheByKey(key, orderNumer.toString(), null);
            }
        }else {
            Integer orderNumer=Integer.valueOf(payCode)+1;
            cacheService.setCacheByKey(key,orderNumer.toString(),null);
        }
        return payCode;
    }
}
