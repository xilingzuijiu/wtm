/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package com.weitaomi.systemconfig.wechat;

import java.io.*;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import com.weitaomi.application.service.impl.LoadFileFactory;
import com.weitaomi.systemconfig.util.HttpRequestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * This example demonstrates how to create secure connections with a custom SSL
 * context.
 */
public class ClientCustomSSL {

    public static String connectKeyStore(String url,String xml,String path,int flag) throws Exception{
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        File file=LoadFileFactory.getFile(path);
        char[] arr=null;
        if (flag==0){
            arr=WechatConfig.MCHID.toCharArray();
        }
        if (flag==1){
            arr=WechatConfig.MCHID_OFFICIAL.toCharArray();
        }
        FileInputStream instream = new FileInputStream(file);
        try {
            keyStore.load(instream, arr);
        } finally {
            instream.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, arr)
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();

        StringEntity entityRequest = new StringEntity(xml,"utf-8");
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entityRequest);
//        httpPost.setHeader("Content-Type", "application/json");//; charset=utf-8
        HttpResponse response = httpclient.execute(httpPost);

        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            throw new RuntimeException("请求失败");
        }
        HttpEntity resEntity = response.getEntity();
        InputStream inputStream=resEntity.getContent();
        return HttpRequestUtils.readInstream(inputStream,"UTF-8");
        }


}
