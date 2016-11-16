package com.weitaomi.systemconfig.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

public class IpUtils {
     public static String getIpAddr(HttpServletRequest request) {
            String ip = request.getHeader("X-Forwarded-For");  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("Proxy-Client-IP");  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("WL-Proxy-Client-IP");  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("HTTP_CLIENT_IP");  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getRemoteAddr();  
            }  
            return ip;  
        }
    public static Map<String,String> getAddressByIp(String ip){
        String ipSearchUrl="http://int.dpool.sina.com.cn/iplookup/iplookup.php";
        NameValuePair[] nameValuePairs=new NameValuePair[2];
        nameValuePairs[0]=new BasicNameValuePair("format","json");
        nameValuePairs[1]=new BasicNameValuePair("ip",ip);
        String result="";
        try {
             result=HttpRequestUtils.get(ipSearchUrl,nameValuePairs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (Map) JSONObject.parse(result);
    }
}