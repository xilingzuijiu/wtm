package com.weitaomi.application.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.weitaomi.application.model.bean.PaymentApprove;
import com.weitaomi.application.model.mapper.PaymentApproveMapper;
import com.weitaomi.application.service.interf.IPayStrategyContext;
import com.weitaomi.application.service.interf.IPayStrategyService;
import com.weitaomi.systemconfig.alipay.AlipaySubmit;
import com.weitaomi.systemconfig.util.*;
import com.weitaomi.systemconfig.wechat.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/30.
 */
@Service
public class WechatPayService implements IPayStrategyService {
    private PaymentApproveMapper paymentApproveMapper;
    @Override
    public String getPaymentParams(Map<String, Object> param) {
        Map<String,String> params=new HashMap<>();
        if ((int)param.get("sourceType")==0) {
            params.put("trade_type","APP");
            params.put("appid", WechatConfig.APP_ID);
            params.put("mch_id", WechatConfig.MCHID);
        }
        if ((int)param.get("sourceType")==1) {
            params.put("trade_type","JSAPI");
            params.put("openid",(String)param.get("openId"));
            params.put("appid", WechatConfig.MCH_APPID);
            params.put("mch_id", WechatConfig.MCHID_OFFICIAL);
        }
        params.put("nonce_str", UUIDGenerator.generate());
        params.put("body","微淘米会员充值");
        params.put("out_trade_no",param.get("out_trade_no").toString());
        params.put("notify_url",WechatConfig.NOTIFY_URL);
        String amountTemp=(String)param.get("amount");
        double amount=Double.valueOf(amountTemp) * 100;
        int balance=(int)amount;
        params.put("total_fee",String.valueOf(balance));
        params.put("spbill_create_ip",param.get("spbill_create_ip").toString());
        params=this.paraFilter(params);
        String pre_sign= StringUtil.formatParaMap(params);
        pre_sign=pre_sign+"&key="+((int)param.get("sourceType")==0?WechatConfig.API_KEY:WechatConfig.OFFICIAL_API_KEY);
        String sign= DigestUtils.md5Hex(pre_sign).toUpperCase();
        params.put("sign",sign);
        WechatPayParams wechatPayParams=JSONObject.parseObject(JSON.toJSONString(params),WechatPayParams.class);
        XStream xStream=new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        xStream.autodetectAnnotations(true);
        String requestParams=xStream.toXML(wechatPayParams);
        try {
            String value=HttpRequestUtils.postString(WechatConfig.PRE_PAY_URL,requestParams);
            if (!StringUtil.isEmpty(value)){
                WechatResultParams wechatResultParams= StreamUtils.toBean(value,WechatResultParams.class);
                if (wechatResultParams.getResult_code().equals(WechatConfig.SUCCESS)&&wechatResultParams.getReturn_code().equals(WechatConfig.SUCCESS)){
                    Map<String,String> parameters=new HashMap<>();
                    parameters.put("appid",wechatPayParams.getAppid());
                    parameters.put("partnerid",wechatPayParams.getMch_id());
                    parameters.put("prepayid",wechatResultParams.getPrepay_id());
                    parameters.put("package","Sign=WXPay");
                    String ran=UUIDGenerator.generate();
                    parameters.put("noncestr",ran);
                    Long time=DateUtils.getUnixTimestamp();
                    parameters.put("timestamp",time.toString());
                    String pre_sig= StringUtil.formatParaMap(parameters);
                    pre_sig=pre_sig+"&key="+WechatConfig.API_KEY;
                    parameters.put("sign",DigestUtils.md5Hex(pre_sig).toUpperCase());
                    return JSON.toJSONString(parameters);
                }
            }
            System.out.println(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getBatchPayParams(Map<String, String> params) {
        if (!params.isEmpty()) {
            for (Map.Entry<String, String> param : params.entrySet()) {


            }
        }
return null;
    }

    private Map<String, String> paraFilter(Map<String, String> sArray) {

        Map<String, String> result = new HashMap<String, String>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }

        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
                    || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.put(key, value);
        }

        return result;
    }


}
