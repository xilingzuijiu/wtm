package com.weitaomi.systemconfig.util;

import org.apache.commons.lang.StringUtils;
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
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.LinkedList;
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
//            System.out.println("============>"+response.getEntity().toString());
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

    public static String get(String url, NameValuePair... params) throws IOException {
        try {
            String parameters="";
            for (NameValuePair p : params) {
                parameters += p.getName()+"="+p.getValue()+"&";
            }
            parameters=parameters.substring(0,parameters.lastIndexOf("&"));
            // 创建POST请求
            HttpGet request = new HttpGet(url+"?"+parameters);
            // 发送请求
            HttpClient client = HttpClients.createDefault();

            HttpResponse response = client.execute(request);
//            System.out.println("============>"+response.getEntity().toString());
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                throw new RuntimeException("请求失败");
            }
            HttpEntity resEntity = response.getEntity();
            return readInstream(resEntity.getContent(),"UTF-8");
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
        HttpResponse response = httpClient.execute(httpPost);

        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            throw new RuntimeException("请求失败");
        }
        HttpEntity resEntity = response.getEntity();
        InputStream inputStream=resEntity.getContent();
        return readInstream(inputStream,"UTF-8");
    }

    public static String postString(String url, String entityString) throws IOException {
        StringEntity entityRequest = new StringEntity(entityString,"utf-8");
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entityRequest);
//        httpPost.setHeader("Content-Type", "application/json");//; charset=utf-8
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(httpPost);

        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            throw new RuntimeException("请求失败");
        }
        HttpEntity resEntity = response.getEntity();
        InputStream inputStream=resEntity.getContent();
        return readInstream(inputStream,"UTF-8");
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

    /**
     * 处理响应报文
     * @param in
     * @param encode
     * @return
     * @throws IOException
     */
    public static String readInstream(InputStream in, String encode) throws IOException {
        if (StringUtils.isBlank(encode))
            encode = "utf-8";
        List<Byte> byteList = new LinkedList<Byte>();
        try(ReadableByteChannel channel = Channels.newChannel(in)) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(9600);
            while (channel.read(byteBuffer) != -1){
                byteBuffer.flip();
                while (byteBuffer.hasRemaining()){
                    byteList.add(byteBuffer.get());
                }
                byteBuffer.clear();
            }
        }
        Byte[] bytes = byteList.toArray(new Byte[byteList.size()]);
        byte[] bytes1 = new byte[bytes.length];
        for (int i=0;i<bytes.length;i++){
            bytes1[i] = bytes[i];
        }
        return new String(bytes1,encode);
    }
}
