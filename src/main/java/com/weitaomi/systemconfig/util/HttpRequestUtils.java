package com.weitaomi.systemconfig.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class HttpRequestUtils {
    public static String post(String url, NameValuePair... params) throws IOException {
        try {
            // 编码参数
            List<NameValuePair> formparams = new ArrayList<NameValuePair>(); // 请求参数
            for (NameValuePair p : params) {
                formparams.add(p);
            }

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams,
                    "UTF-8");
            entity.setContentType("application/x-www-form-urlencoded");
            // 创建POST请求
            HttpPost request = new HttpPost(url);

            request.setHeader("Content-Type","application/x-www-form-urlencoded");
            request.setEntity(entity);
            // 发送请求
            HttpClient client = HttpClients.createDefault();

            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new RuntimeException("请求失败");
            }
            HttpEntity resEntity = response.getEntity();
            return (resEntity == null) ? null : EntityUtils.toString(resEntity, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        } catch (ClientProtocolException e) {
            return null;
        } catch (IOException e) {
            throw new RuntimeException("连接失败", e);
        }

    }




    public static String postStringEntity(String url, String entityString) throws IOException {
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
    public static String cancelPost(String url, NameValuePair... params) throws IOException {
        try {
            // 编码参数
            List<NameValuePair> formparams = new ArrayList<NameValuePair>(); // 请求参数
            for (NameValuePair p : params) {
                formparams.add(p);
            }

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams,
                    "UTF-8");
            entity.setContentType("application/x-www-form-urlencoded");
            // 创建POST请求
            HttpPost request = new HttpPost(url);

            request.setHeader("Content-Type","application/x-www-form-urlencoded");
            request.setHeader("userId","332");
            request.setEntity(entity);
            // 发送请求
            HttpClient client = HttpClients.createDefault();

            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new RuntimeException("请求失败");
            }
            HttpEntity resEntity = response.getEntity();
            return (resEntity == null) ? null : EntityUtils.toString(resEntity, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        } catch (ClientProtocolException e) {
            return null;
        } catch (IOException e) {
            throw new RuntimeException("连接失败", e);
        }

    }
}
