package com.weitaomi.systemconfig.util;

import com.weitaomi.application.model.enums.SendMCType;
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
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * 发送短信工具类
 * Created by Liujishuai on 2015/10/23.
 */
public class SendMCUtils {
    private static Logger logger = LoggerFactory.getLogger(SendMCUtils.class);

//    /**
//     * 发送短信接口
//     * @param phone 手机号，多个用,分开，最多100个
//     * @param message 消息内容
//     * @throws IOException
//     */
//    public static void sendMessage(String phone,String message) throws BusinessException {
//        if(StringUtil.isEmpty(phone)){
//            throw new BusinessException("电话不能为空");
//        }
//        try {
//            Integer count=StringUtil.splitString(phone,",").length;
//            Integer valuableNum=getValuableNumber(ResourceUtil.getConfigByName("sms.username"),ResourceUtil.getConfigByName("sms.password"));
//            if(count>valuableNum){
//                logger.info("商城发送短信账号剩余短信量：{}，不足以发送当前数量的短信。",valuableNum);
//                throw new BusinessException("账户余额不支持发送该数量的短信，请尽快充值");
//            }
//            String uuid=UUIDGenerator.generate().toString();
//            List<NameValuePair> list=new ArrayList<>();
//            list.add(new BasicNameValuePair("userId",ResourceUtil.getConfigByName("sms.username")));
//            list.add(new BasicNameValuePair("password",ResourceUtil.getConfigByName("sms.password")));
//            list.add(new BasicNameValuePair("pszMobis",phone));
//            list.add(new BasicNameValuePair("imobiCount",count.toString()));
//            list.add(new BasicNameValuePair("pszMsg",message));
//            list.add(new BasicNameValuePair("pszSubPort","*"));
//            list.add(new BasicNameValuePair("MsgId",uuid));
//            list.add(new BasicNameValuePair("",""));
//            String url = ResourceUtil.getConfigByName("sms.url.send");
//
//            String sk= post(url,list);
//            XStream xStream=new XStream();
//            String ss=xStream.fromXML(sk).toString();
//            if(ss.equals("-1")){
//                logger.info("发送短信参数有空");
//                throw new BusinessException("发送短信失败，参数有空");
//            }else if(ss.equals("-12")){
//                logger.info("发送短信有异常电话号码");
//                throw new BusinessException("发送短信失败，电话有异常");
//            }else if(ss.equals("-14")){
//                logger.info("发送短信数量不能超过100个");
//                throw new BusinessException("发送短信失败，超过最大数量");
//            }else if(ss.equals("-999")){
//                logger.info("发送短信web服务器内部错误");
//                throw new BusinessException("发送短信失败，短信服务器错误");
//            }
//        }catch (IOException e){
//            throw new BusinessException("发送短信失败，短信服务器错误");
//        }
//
//    }
//    /**
//     * 发送短信接口
//     * @param phone 手机号，多个用,分开，最多100个
//     * @param message 消息内容
//     * @throws IOException
//     */
//    public static void sendMessage(String phone,String message,Integer type) throws BusinessException {
//        if(ResourceUtil.getConfigByName("sms.enable").equals("0")){
//            logger.info("已屏蔽短信：{}，内容：{}",phone,message);
//            return;
//        }
//        if(StringUtil.isEmpty(phone)){
//            logger.info("电话不能为空");
//            return;
//        }
//        Integer count=StringUtil.splitString(phone,",").length;
//        Integer valuableNum=getValuableNumber(ResourceUtil.getConfigByName("sms.username"),ResourceUtil.getConfigByName("sms.password"));
//        if(count>valuableNum){
//            logger.info("商城发送短信账号剩余短信量：{}，不足以发送当前数量的短信。",valuableNum);
//           return;
//        }
//        String uuid=UUIDGenerator.generate().toString();
//        List<NameValuePair> list=new ArrayList<>();
//        list.add(new BasicNameValuePair("userId",ResourceUtil.getConfigByName("sms.username")));
//        list.add(new BasicNameValuePair("password",ResourceUtil.getConfigByName("sms.password")));
//        list.add(new BasicNameValuePair("pszMobis",phone));
//        list.add(new BasicNameValuePair("imobiCount",count.toString()));
//        list.add(new BasicNameValuePair("pszMsg",message));
//        list.add(new BasicNameValuePair("pszSubPort","*"));
//        list.add(new BasicNameValuePair("MsgId",uuid));
//        list.add(new BasicNameValuePair("",""));
//        String url = ResourceUtil.getConfigByName("sms.url.send");
//        try {
//            String sk= post(url,list);
//            XStream xStream=new XStream();
//            String ss=xStream.fromXML(sk).toString();
//            if(ss.equals("-1")){
//                logger.info("发送短信参数有空");
//                return;
//            }else if(ss.equals("-12")){
//                logger.info("发送短信有异常电话号码");
//                return;
//            }else if(ss.equals("-14")){
//                logger.info("发送短信数量不能超过100个");
//                return;
//            }else if(ss.equals("-999")){
//                logger.info("发送短信web服务器内部错误");
//                return;
//            }
//        }catch (IOException e){
//            logger.info("短信发送失败");
//        }
//
//    }
//    /**
//     * 获取该账号可以使用的短信条数
//     * @param userId 账号id
//     * @param password 密码
//     * @return
//     */
//   public static Integer getValuableNumber(String userId,String password) throws BusinessException {
//       List<NameValuePair> list=new ArrayList<>();
//       list.add(new BasicNameValuePair("userId",userId));
//       list.add(new BasicNameValuePair("password",password));
//       String sk=null;
//       try {
//           sk=post(ResourceUtil.getConfigByName("sms.url.number"),list);
//       } catch (Exception e) {
//          throw new BusinessException("短信发送失败");
//       }
//       XStream xStream=new XStream();
//       String ss=xStream.fromXML(sk).toString();
//       return StringUtil.toInt(ss);
//   }

    /**
     * 发送http请求
     * @param url
     * @param formparams
     * @return
     * @throws IOException
     */
    public static String post(String url, List<NameValuePair> formparams) throws IOException {
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

    /**
     * 短信发送接口，发送失败会抛出异常
     * @param phone 手机号
     * @param message 短信内容
     */
    public  static void sendMessage(String phone,String message)  {
        if(getMessageNumber()<=0){
            throw new BusinessException("商城发送短信账户余额不足，请尽快充值");
        }
        String messages= null;
                try {
                    messages=StringUtil.toHexString(message+ResourceUtil.getConfigByName("sms.supumall.logo"));
                }catch (UnsupportedEncodingException e){
                    throw new BusinessException("短信内容不支持字符编码异常");
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
                logger.error("发送短信失败,错误码:{}",result);
                throw new BusinessException("发送短信失败,错误码:"+result);
            }
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
                throw new BusinessException("查询短信余额错误，错误码:"+result);
            }

        } catch (IOException e) {
            logger.error("短信发送失败");
            throw new BusinessException("短信发送失败");
        }
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

    /**
     * 按照固定模板的短信发送
     * @param phone
     * @param sendMCType
     * @param map
     * @throws Exception
     */
    public static void sendMessageWithStyle(String phone, SendMCType sendMCType, Map<String,Object> map) throws BusinessException {

    }
}
