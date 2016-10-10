package com.weitaomi.application.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weitaomi.application.controller.baseController.BaseController;
import com.weitaomi.application.model.bean.ThirdLogin;
import com.weitaomi.application.model.dto.MemberInfoDto;
import com.weitaomi.application.model.mapper.ThirdLoginMapper;
import com.weitaomi.application.service.interf.IMemberService;
import com.weitaomi.systemconfig.util.HttpRequestUtils;
import com.weitaomi.systemconfig.wechat.WechatConfig;
import org.apache.commons.codec.Encoder;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/19.
 */
@Controller
@RequestMapping("/")
public class PageController extends BaseController{
    @Autowired
    private ThirdLoginMapper thirdLoginMapper;
    @RequestMapping("")
    public String indexPage(){
        return "wtmpc/index.html";
    }
    @Autowired
    private IMemberService memberService;
    /**
     * 微信授权回调
     * @return
     */
    @RequestMapping(value = "/wxRegisterOAuth", method = RequestMethod.GET)
    public ModelAndView wxRegisterOAuth(HttpServletRequest request, HttpServletResponse response) {
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
//            request.setAttribute("nickname",userInfoParams.get("nickname"));
//            request.setAttribute("openid",userInfoParams.get("openid"));
//            request.setAttribute("sex",userInfoParams.get("sex"));
//            request.setAttribute("city",userInfoParams.get("city"));
//            request.setAttribute("province",userInfoParams.get("province"));
//            request.setAttribute("country",userInfoParams.get("country"));
//            request.setAttribute("unionid",userInfoParams.get("unionid"));
//            response.setContentType("text/html");
//            response.setCharacterEncoding("UTF-8");
            Long memberId=thirdLoginMapper.getMemberIdByUnionId(userInfoParams.get("unionid"));
            if (memberId==null){
                ModelAndView modelAndView=new ModelAndView("wap/register.jsp");
                modelAndView.addAllObjects(userInfoParams);
//                request.getRequestDispatcher("/frontPage/wap/register.jsp").forward(request,response);
                return modelAndView;
            }
            if (memberId!=null){
                MemberInfoDto memberInfoDto = memberService.thirdPlatLogin(userInfoParams.get("unionid"),0);
                ModelAndView modelAndView=new ModelAndView("/wap/index.jsp");
                Cookie idCookie=new Cookie("memberId",memberInfoDto.getId().toString());
                idCookie.setMaxAge(30*24*60*60);
                Cookie memberName=new Cookie("memberName", URLEncoder.encode(memberInfoDto.getMemberName()));
                memberName.setMaxAge(30*24*60*60);
                Cookie passWord=new Cookie("password",memberInfoDto.getPassword());
                passWord.setMaxAge(30*24*60*60);
                String imageUrl = memberInfoDto.getImageUrl();
                if (!imageUrl.startsWith("http")){
                    imageUrl ="http://weitaomi.b0.upaiyun.com"+imageUrl;
                }
                Cookie image=new Cookie("imageUrl",imageUrl);
                image.setMaxAge(30*24*60*60);
                Cookie telephone=new Cookie("telephone", memberInfoDto.getTelephone().toString());
                telephone.setMaxAge(30*24*60*60);
                Cookie officialAccountList=new Cookie("officialAccountList", JSON.toJSONString(memberInfoDto.getOfficialAccountList()));
                officialAccountList.setMaxAge(30*24*60*60);
                Cookie thirdLogin=new Cookie("thirdLogin",JSON.toJSONString(memberInfoDto.getThirdLogin()));
                thirdLogin.setMaxAge(30*24*60*60);
                Cookie payList=new Cookie("payList",JSON.toJSONString(memberInfoDto.getPayAccountsList()));
                payList.setMaxAge(30*24*60*60);
                response.addCookie(idCookie);
                response.addCookie(memberName);
                response.addCookie(passWord);
                response.addCookie(image);
                response.addCookie(telephone);
                response.addCookie(officialAccountList);
                response.addCookie(thirdLogin);
                response.addCookie(payList);
                return modelAndView;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
