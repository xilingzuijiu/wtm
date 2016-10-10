package com.weitaomi.application.controller;

import com.alibaba.fastjson.JSONObject;
import com.weitaomi.application.controller.baseController.BaseController;
import com.weitaomi.systemconfig.util.HttpRequestUtils;
import com.weitaomi.systemconfig.wechat.WechatConfig;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/19.
 */
@Controller
@RequestMapping("/")
public class PageController extends BaseController{
    @RequestMapping("")
    public String indexPage(){
        return "wtmpc/index.html";
    }
    /**
     * 微信授权回调
     * @return
     */
    @RequestMapping(value = "/wxRegisterOAuth", method = RequestMethod.GET)
    public void wxRegisterOAuth(HttpServletRequest request, HttpServletResponse response) {
        NameValuePair[] nameValue = new NameValuePair[4];
        nameValue[0]=new BasicNameValuePair("appid", WechatConfig.MCH_APPID);
        nameValue[1]=new BasicNameValuePair("secret", WechatConfig.wxAppSecret);
        nameValue[2]=new BasicNameValuePair("code", request.getParameter("code"));
        nameValue[3]=new BasicNameValuePair("grant_type","authorization_code");
        try {
            String params= HttpRequestUtils.get("https://api.weixin.qq.com/sns/oauth2/access_token",nameValue);
            Map<String,String> hashMap= (Map<String, String>) JSONObject.parse(params);
            NameValuePair[] userInfoRequestParams = new NameValuePair[3];
            userInfoRequestParams[0]=new BasicNameValuePair("access_token",hashMap.get("access_token"));
            userInfoRequestParams[1]=new BasicNameValuePair("openid",hashMap.get("openid"));
            userInfoRequestParams[2]=new BasicNameValuePair("lang","zh_CN");
            String userInfo=HttpRequestUtils.get("https://api.weixin.qq.com/sns/userinfo",userInfoRequestParams);
            Map<String,String> userInfoParams= (Map<String, String>) JSONObject.parse(userInfo);
            request.setAttribute("nickname",userInfoParams.get("nickname"));
            request.setAttribute("openid",userInfoParams.get("openid"));
            request.setAttribute("sex",userInfoParams.get("sex"));
            request.setAttribute("city",userInfoParams.get("city"));
            request.setAttribute("province",userInfoParams.get("province"));
            request.setAttribute("country",userInfoParams.get("country"));
            request.setAttribute("unionid",userInfoParams.get("unionid"));
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            request.getRequestDispatcher("/frontPage/wap/register.jsp").forward(request,response);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }
}
