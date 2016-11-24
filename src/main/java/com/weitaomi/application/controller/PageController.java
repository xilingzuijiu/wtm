package com.weitaomi.application.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weitaomi.application.controller.baseController.BaseController;
import com.weitaomi.application.model.bean.Member;
import com.weitaomi.application.model.bean.ThirdLogin;
import com.weitaomi.application.model.dto.ArticleReadRecordDto;
import com.weitaomi.application.model.dto.MemberInfoDto;
import com.weitaomi.application.model.dto.RequestFrom;
import com.weitaomi.application.model.enums.PayType;
import com.weitaomi.application.model.mapper.ThirdLoginMapper;
import com.weitaomi.application.model.mapper.WTMAccountMessageMapper;
import com.weitaomi.application.service.interf.IArticleService;
import com.weitaomi.application.service.interf.IMemberService;
import com.weitaomi.application.service.interf.IPaymentService;
import com.weitaomi.systemconfig.dataFormat.AjaxResult;
import com.weitaomi.systemconfig.exception.BusinessException;
import com.weitaomi.systemconfig.util.*;
import com.weitaomi.systemconfig.wechat.WechatConfig;
import org.apache.commons.codec.Encoder;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/19.
 */
@Controller
@RequestMapping("/")
public class PageController extends BaseController {
    @Autowired
    private ThirdLoginMapper thirdLoginMapper;
    @Autowired
    private WTMAccountMessageMapper messageMapper;
    @RequestMapping("")
    public String indexPage() {
        return "wtmpc/index.html";
    }

    @Autowired
    private IMemberService memberService;
    @Autowired
    private IPaymentService paymentService;
    /**
     * 微信授权回调
     *
     * @return
     */
    @RequestMapping(value = "/wxRegisterOAuth", method = RequestMethod.GET)
    public ModelAndView wxRegisterOAuth(HttpServletRequest request, HttpServletResponse response) {
        NameValuePair[] nameValue = new NameValuePair[4];
        nameValue[0] = new BasicNameValuePair("appid", WechatConfig.MCH_APPID);
        nameValue[1] = new BasicNameValuePair("secret", WechatConfig.wxAppSecret);
        nameValue[2] = new BasicNameValuePair("code", request.getParameter("code"));
        nameValue[3] = new BasicNameValuePair("grant_type", "authorization_code");
        try {
            String params = HttpRequestUtils.get("https://api.weixin.qq.com/sns/oauth2/access_token", nameValue);
            Map<String, String> hashMap = (Map<String, String>) JSONObject.parse(params);
            NameValuePair[] userInfoRequestParams = new NameValuePair[3];
            userInfoRequestParams[0] = new BasicNameValuePair("access_token", hashMap.get("access_token"));
            userInfoRequestParams[1] = new BasicNameValuePair("openid", hashMap.get("openid"));
            userInfoRequestParams[2] = new BasicNameValuePair("lang", "zh_CN");
            String userInfo = HttpRequestUtils.get("https://api.weixin.qq.com/sns/userinfo", userInfoRequestParams);
            Map<String, String> userInfoParams = (Map<String, String>) JSONObject.parse(userInfo);
            List<Long> memberId = thirdLoginMapper.getMemberIdByUnionId(userInfoParams.get("unionid"),null);
            if (memberId.isEmpty()) {
                ModelAndView modelAndView = new ModelAndView("wap/register.jsp");
                modelAndView.addAllObjects(userInfoParams);
//                request.getRequestDispatcher("/frontPage/wap/register.jsp").forward(request,response);
                return modelAndView;
            }
            if (!memberId.isEmpty()) {
                MemberInfoDto memberInfoDto = memberService.thirdPlatLogin(userInfoParams.get("unionid"),userInfoParams.get("openid"), 0,1);
                ModelAndView modelAndView = new ModelAndView("/wap/index.html");
                Cookie idCookie = new Cookie("memberId", memberInfoDto.getId().toString());
                idCookie.setMaxAge(30 * 24 * 60 * 60);
                Cookie sex = new Cookie("sex", memberInfoDto.getSex().toString());
                sex.setMaxAge(30 * 24 * 60 * 60);
                Cookie birth = new Cookie("birth", memberInfoDto.getBirth().toString());
                birth.setMaxAge(30 * 24 * 60 * 60);
                Cookie memberName = new Cookie("memberName", URLEncoder.encode(memberInfoDto.getMemberName()));
                memberName.setMaxAge(30 * 24 * 60 * 60);
                Cookie passWord = new Cookie("password", memberInfoDto.getPassword());
                passWord.setMaxAge(30 * 24 * 60 * 60);
                String imageUrl = memberInfoDto.getImageUrl();
                if (!imageUrl.startsWith("http")) {
                    imageUrl = "http://weitaomi.b0.upaiyun.com" + imageUrl;
                }
                Cookie image = new Cookie("imageUrl", imageUrl);
                image.setMaxAge(30 * 24 * 60 * 60);
                Cookie telephone = new Cookie("telephone", memberInfoDto.getTelephone().toString());
                telephone.setMaxAge(30 * 24 * 60 * 60);
                if (!memberInfoDto.getOfficialAccountList().isEmpty()) {
                    Cookie officialAccountList = new Cookie("officialAccountList", "1");
                    officialAccountList.setMaxAge(30 * 24 * 60 * 60);
                    response.addCookie(officialAccountList);
                }
                Cookie thirdLogin = new Cookie("thirdLogin", URLEncoder.encode(JSON.toJSONString(memberInfoDto.getThirdLogin()), "UTF-8"));
                thirdLogin.setMaxAge(30 * 24 * 60 * 60);
                Cookie payList = new Cookie("payList", URLEncoder.encode(JSON.toJSONString(memberInfoDto.getPayAccountsList()), "UTF-8"));
                payList.setMaxAge(30 * 24 * 60 * 60);
                response.addCookie(idCookie);
                response.addCookie(sex);
                response.addCookie(birth);
                response.addCookie(memberName);
                response.addCookie(passWord);
                response.addCookie(image);
                response.addCookie(telephone);
                response.addCookie(thirdLogin);
                response.addCookie(payList);
                return modelAndView;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 微信授权回调
     *
     * @return
     */
    @RequestMapping(value = "/blindWXOAuth", method = RequestMethod.GET)
    public ModelAndView blindWXOAuth(HttpServletRequest request, HttpServletResponse response) {
        NameValuePair[] nameValue = new NameValuePair[4];
        nameValue[0] = new BasicNameValuePair("appid", WechatConfig.MCH_APPID);
        nameValue[1] = new BasicNameValuePair("secret", WechatConfig.wxAppSecret);
        nameValue[2] = new BasicNameValuePair("code", request.getParameter("code"));
        nameValue[3] = new BasicNameValuePair("grant_type", "authorization_code");
        try {
            String params = HttpRequestUtils.get("https://api.weixin.qq.com/sns/oauth2/access_token", nameValue);
            Map<String, String> hashMap = (Map<String, String>) JSONObject.parse(params);
            NameValuePair[] userInfoRequestParams = new NameValuePair[3];
            userInfoRequestParams[0] = new BasicNameValuePair("access_token", hashMap.get("access_token"));
            userInfoRequestParams[1] = new BasicNameValuePair("openid", hashMap.get("openid"));
            userInfoRequestParams[2] = new BasicNameValuePair("lang", "zh_CN");
            String userInfo = HttpRequestUtils.get("https://api.weixin.qq.com/sns/userinfo", userInfoRequestParams);
            Map<String, Object> userInfoParams = (Map<String, Object>) JSONObject.parse(userInfo);
            List<Long> memberIdTemp = thirdLoginMapper.getMemberIdByUnionId((String)userInfoParams.get("unionid"),1);
            System.out.println(JSON.toJSONString(userInfo));
            if (memberIdTemp.isEmpty()) {
                Long memberId=Long.valueOf(request.getParameter("memberId"));
                ThirdLogin thirdLogin=new ThirdLogin();
                thirdLogin.setOpenId((String)userInfoParams.get("openid"));
                thirdLogin.setUnionId((String)userInfoParams.get("unionid"));
                thirdLogin.setSourceType(1);
                String sexString=userInfoParams.get("sex").toString();
                if (!StringUtil.isEmpty(sexString)){
                    thirdLogin.setSex(Integer.valueOf(sexString));
                }
                thirdLogin.setType(0);
                thirdLogin.setNickname((String)userInfoParams.get("nickname"));
                thirdLogin.setImageFiles((String)userInfoParams.get("headimgurl"));
                thirdLogin.setSourceType(1);
                boolean isSuccess= memberService.bindThirdPlat(memberId, thirdLogin,1);
                if (isSuccess) {
                    Member member=memberMapper.selectByPrimaryKey(memberId);
                    Cookie sex = new Cookie("sex", member.getSex().toString());
                    sex.setMaxAge(30 * 24 * 60 * 60);
                    Cookie memberName = new Cookie("memberName", URLEncoder.encode(member.getMemberName()));
                    memberName.setMaxAge(30 * 24 * 60 * 60);
                    Cookie birth = new Cookie("birth", member.getBirth().toString());
                    birth.setMaxAge(30 * 24 * 60 * 60);
                    Cookie passWord = new Cookie("password", member.getPassword());
                    passWord.setMaxAge(30 * 24 * 60 * 60);
                    String imageUrl = member.getImageUrl();
                    if (!imageUrl.startsWith("http")) {
                        imageUrl = "http://weitaomi.b0.upaiyun.com" + imageUrl;
                    }
                    Cookie image = new Cookie("imageUrl", imageUrl);
                    image.setMaxAge(30 * 24 * 60 * 60);
                    Cookie telephone = new Cookie("telephone", member.getTelephone().toString());
                    telephone.setMaxAge(30 * 24 * 60 * 60);
                    Cookie wxInfo = new Cookie("thirdLogin", URLEncoder.encode(JSON.toJSONString(thirdLogin), "UTF-8"));
                    wxInfo.setMaxAge(30 * 24 * 60 * 60);
                    Cookie access_token = new Cookie("access_token", hashMap.get("access_token"));
                    access_token.setMaxAge(2 * 60 * 60);
                    Cookie refresh_token = new Cookie("refresh_token", hashMap.get("refresh_token"));
                    refresh_token.setMaxAge(30 * 24 * 60 * 60);
                    Cookie member1 = new Cookie("memberId",memberId.toString());
                    member1.setMaxAge(30 * 24 * 60 * 60);
                    response.addCookie(wxInfo);
                    response.addCookie(member1);
                    response.addCookie(sex);
                    response.addCookie(memberName);
                    response.addCookie(birth);
                    response.addCookie(image);
                    response.addCookie(telephone);
                    response.addCookie(access_token);
                    response.addCookie(refresh_token);
                    ModelAndView modelAndView = new ModelAndView("wap/mycenter.html");
//                request.getRequestDispatcher("/frontPage/wap/register.jsp").forward(request,response);
                    return modelAndView;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 签名
     *
     * @return
     */
    @RequestMapping(value = "/pay", method = RequestMethod.GET)
    public ModelAndView signature(HttpServletRequest request, HttpServletResponse response,Long memberId,Double amount,Integer payType) {
        try {
            Long timestamp = DateUtils.getUnixTimestamp();
            String accessToken = "";
            if (DateUtils.getUnixTimestamp()-WechatConfig.token_updatetime>7200||StringUtil.isEmpty(accessToken)) {
                NameValuePair[] nameValue = new NameValuePair[3];
                nameValue[1] = new BasicNameValuePair("grant_type", "client_credential");
                nameValue[0] = new BasicNameValuePair("appid", WechatConfig.MCH_APPID);
                nameValue[2] = new BasicNameValuePair("secret", WechatConfig.wxAppSecret);
                String params = HttpRequestUtils.get("https://api.weixin.qq.com/cgi-bin/token", nameValue);
                Map params_map = JSON.parseObject(params);
                accessToken = (String) params_map.get("access_token");
                WechatConfig.access_token = accessToken;
                WechatConfig.token_updatetime = DateUtils.getUnixTimestamp();
                messageMapper.updateToken(DateUtils.getTime(DateUtils.getUnixTimestamp(),DateUtils.standard),accessToken);
            }
            NameValuePair[] nameValue1 = new NameValuePair[2];
            nameValue1[0] = new BasicNameValuePair("access_token", WechatConfig.access_token);
            nameValue1[1] = new BasicNameValuePair("type", "jsapi");
            String jsapi = HttpRequestUtils.get("https://api.weixin.qq.com/cgi-bin/ticket/getticket", nameValue1);
            Map jsapi_map = JSON.parseObject(jsapi);
            String nonceStr = "weitaomiApp";
            String url = request.getRequestURL().toString()+"?"+request.getQueryString();
            String signParams = "jsapi_ticket=" + jsapi_map.get("ticket") + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + url;
            System.out.println("==========>signParams=" + signParams);
            String sign = HmacSHA256Utils.SHA(signParams);
            Map param=new HashMap();
            param.put("memberId",memberId);
            param.put("payType",payType);
            param.put("amount",amount.toString());
            param.put("spbill_create_ip",IpUtils.getIpAddr(request));
            ModelAndView modelAndView = new ModelAndView("wap/pay.jsp");
            String paramString=paymentService.getPaymentParams(param);
            if ((Integer)param.get("payType")==(PayType.WECHAT_WEB.getValue())){
                Map map= JSON.parseObject(paramString);
                String nonce=(String)map.get("noncestr");
                String pre_sign="appId="+map.get("appid")+"&nonceStr="+nonce+"&package=prepay_id="+map.get("prepayid")+"&signType=MD5"+"&timeStamp="+map.get("timestamp")+"&key="+ WechatConfig.OFFICIAL_API_KEY;
                System.out.println("prePaySign=="+pre_sign);
                modelAndView.addObject("pay_timestamp",map.get("timestamp"));
                modelAndView.addObject("nonceStrPay",nonce);
                modelAndView.addObject("prepayid",map.get("prepayid"));
                String signature=DigestUtils.md5Hex(pre_sign).toUpperCase();
                System.out.println("签名为:"+signature);
                modelAndView.addObject("paySign",signature);

            }
            modelAndView.addObject("nonceStr",nonceStr);
            modelAndView.addObject("timestamp",timestamp);
            modelAndView.addObject("signature",sign);
            modelAndView.addObject("appid",WechatConfig.MCH_APPID);
            return modelAndView;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
