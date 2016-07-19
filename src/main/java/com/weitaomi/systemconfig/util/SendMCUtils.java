package com.weitaomi.systemconfig.util;

import com.alibaba.fastjson.JSON;
import com.weitaomi.systemconfig.exception.BusinessException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class SendMCUtils {
    private static Logger logger = LoggerFactory.getLogger(SendMCUtils.class);


    /**
     * 短信发送接口，发送失败会抛出异常
     * @param phone 手机号
     * @param message 短信内容
     */
    public  static void sendMessage(String phone,String message)  {
        if(getMessageNumber()<=0){
            throw new BusinessException("发送短信账户余额不足，请尽快充值");
        }
        List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("User",PropertiesUtil.getValue("sms.user")));
        nameValuePairs.add(new BasicNameValuePair("Pass",PropertiesUtil.getValue("sms.password")));
        nameValuePairs.add(new BasicNameValuePair("Destinations",phone));
        nameValuePairs.add(new BasicNameValuePair("Content",message));
        logger.info("请求信息："+JSON.toJSONString(nameValuePairs));
        try {
            String msg=postNameValuePairs(PropertiesUtil.getValue("sms.sendUrl"),nameValuePairs);
            logger.info("查询结果:"+msg);
        } catch (IOException e) {
            logger.error("短信发送失败");
            throw new BusinessException("短信发送失败");
        }
    }

    /**
     * 短信发送接口，发送失败不会抛异常
     * @param phone 手机号
     * @param message 短信
     * @param type 类型（只要填了就可以）
     */
    public  static void sendMessage(String phone,String message,Integer type)  {
        if(ResourceUtil.getConfigByName("sms.enable").equals("0")){
            logger.info("已屏蔽短信：{}，内容：{}",phone,message);
            return;
        }
        if(getMessageNumber(1)<=0){
            logger.warn("商城发送短信账户余额不足，请尽快充值");
            return;
        }
        String messages= null;
        try {
            messages=StringUtil.toHexString(message+ResourceUtil.getConfigByName("sms.supumall.logo"));
        }catch (UnsupportedEncodingException e){
            logger.warn("短信内容不支持字符编码异常");
            return;
        }
        String param="un="+ResourceUtil.getConfigByName("sms.wubo.username")+"&pw="+ResourceUtil.getConfigByName("sms.wubo.password")+"&da="+phone+"&sm="+messages
                +"&dc=15&rd=1";
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(ResourceUtil.getConfigByName("sms.url.wubo.send") + "?" + param);
        HttpResponse response = null;
        String result=null;
        try {
            response = httpClient.execute(httpGet);
            result=EntityUtils.toString(response.getEntity());
            if(!result.contains("id")){
                logger.warn("发送短信失败,错误码:{},手机号：{},短信内容：{}",result,phone,message);
                //throw new BusinessException("发送短信失败,错误码:"+result);
                return;
            }
        } catch (IOException e) {
            logger.warn("短信发送失败，手机号：{},短信内容：{}",phone,message);
            //throw new BusinessException("短信发送失败");
            return;
        }
    }
    public  static Integer getMessageNumber(){
       List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("User",PropertiesUtil.getValue("sms.user")));
        nameValuePairs.add(new BasicNameValuePair("Pass",PropertiesUtil.getValue("sms.password")));

        Integer number=0;
        try {
            String msg=postNameValuePairs(PropertiesUtil.getValue("sms.queryUrl"),nameValuePairs);
            logger.info("查询结果:"+msg);
            Map<String,String> map= JSON.parseObject(msg,Map.class);
            if (map!=null){
                number=Integer.valueOf(map.get("SmsBalance"));
            }
        } catch (IOException e) {
            logger.error("短信发送失败");
            throw new BusinessException("短信发送失败");
        }
        return number;
    }

    /**
     * 不会抛异常的数据查询
     * @param type
     * @return
     */
    public  static Integer getMessageNumber(Integer type){
        String param="un="+ResourceUtil.getConfigByName("sms.wubo.username")+"&pw="+ResourceUtil.getConfigByName("sms.wubo.password");
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(ResourceUtil.getConfigByName("sms.url.wubo.number") + "?" + param);
        HttpResponse response = null;
        String result=null;
        try {
            response = httpClient.execute(httpGet);
            result=EntityUtils.toString(response.getEntity());
            if(result.contains("bl")){
                return  StringUtil.toInt(result.split("bl=")[1]);
            }else {
               // throw new BusinessException("查询短信余额错误，错误码:"+result);
                return 0;
            }

        } catch (IOException e) {
            logger.error("短信发送失败");
            //throw new BusinessException("短信发送失败");
            return 0;
        }
    }

    private static String postStringEntity(String url, String entityString) throws IOException {
        StringEntity entityRequest = new StringEntity(entityString,"utf-8");
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entityRequest);
        httpPost.setHeader("Content-Type", "application/json");//; charset=utf-8
        HttpClient httpClient = HttpClients.createDefault();
        // httpPost.setHeader("Content-Type", "text/xml;charset=GBK");

        HttpResponse response = httpClient.execute(httpPost);

        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            throw new RuntimeException("请求失败");
        }
        HttpEntity resEntity = response.getEntity();
        return (resEntity == null) ? null : EntityUtils.toString(resEntity, "UTF-8");
    }
    /**
     * 发送http请求
     * @param url
     * @param formparams
     * @return
     * @throws IOException
     */
    private static String postNameValuePairs(String url, List<NameValuePair> formparams) throws IOException {
        try {
            /*// 编码参数
            List<NameValuePair> formparams = new ArrayList<NameValuePair>(); // 请求参数
            for (NameValuePair p : params) {
                formparams.add(p);
            }*/
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams,
                    "UTF-8");
            // 创建POST请求
            HttpPost request = new HttpPost(url);
            request.setEntity(entity);
            // 发送请求
            HttpClient client = HttpClients.createDefault();
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new BusinessException("请求失败");
            }
            HttpEntity resEntity = response.getEntity();
            return (resEntity == null) ? null : EntityUtils.toString(resEntity, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        } catch (ClientProtocolException e) {
            return null;
        } catch (IOException e) {
            throw new BusinessException("连接失败", e);
        }

    }

}
