package com.weitaomi.application.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weitaomi.application.controller.baseController.BaseController;
import com.weitaomi.application.model.bean.Member;
import com.weitaomi.application.model.bean.TaskPool;
import com.weitaomi.application.model.bean.ThirdLogin;
import com.weitaomi.application.model.dto.PublishReadRequestDto;
import com.weitaomi.application.model.dto.RegisterMsg;
import com.weitaomi.application.model.dto.RequestFrom;
import com.weitaomi.application.service.interf.*;
import com.weitaomi.systemconfig.alipay.StringUtils;
import com.weitaomi.systemconfig.dataFormat.AjaxResult;
import com.weitaomi.systemconfig.exception.BusinessException;
import com.weitaomi.systemconfig.exception.DBException;
import com.weitaomi.systemconfig.exception.InfoException;
import com.weitaomi.systemconfig.util.*;
import com.weitaomi.systemconfig.wechat.WechatConfig;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.DigestSignatureSpi;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/23.
 */
@Controller
@RequestMapping("/pc/admin/member")
public class MemberPCController extends BaseController {
    private Logger logger= org.slf4j.LoggerFactory.getLogger(MemberPCController.class);
    @Autowired
    private IMemberService memberService;
    @Autowired
    private IMemberTaskPoolService memberTaskPoolService;
    @Autowired
    private IArticleService articleService;
    @Autowired
    private IAppVersionService appVersionService;
    @Autowired
    private IOfficeAccountService officeAccountService;
    @Autowired
    private IMemberTaskHistoryService memberTaskHistoryService;
    @Autowired
    private IMemberScoreService memberScoreService;
    @Autowired
    private ICacheService cacheService;
    /**
     * 获取用户信息
     * @throws ParseException    the parse exception
     * @see
     */
    @ResponseBody
    @RequestMapping(value = "/getMemberAccountPublishMsg", method = RequestMethod.POST)
    public AjaxResult getMemberAccountPublishMsg(Long memberId){
        return AjaxResult.getOK(memberTaskPoolService.getRequireFollowerParamsDto(memberId));
    }
    /**
     * 获取用户信息
     * @throws ParseException    the parse exception
     * @see
     */
    @ResponseBody
    @RequestMapping(value = "/getMemberArticlePublishMsg", method = RequestMethod.POST)
    public AjaxResult getMemberArticlePublishMsg(Long memberId){
        return AjaxResult.getOK(memberTaskPoolService.getMemberArticlePublishMsg(memberId));
    }
    /**
     * 公众号发布关注任务
     * @throws ParseException    the parse exception
     * @see
     */
    @ResponseBody
    @RequestMapping(value = "/publishAddRequest", method = RequestMethod.POST)
    public AjaxResult publishAddRequest(@RequestBody TaskPool taskPool,HttpServletRequest request){
//        Integer flag=Integer.valueOf(request.getHeader("flag"));
        taskPool.setTaskType(0);
        taskPool.setCreateTime(DateUtils.getUnixTimestamp());
        String rateTemp= cacheService.getCacheByKey("task:rate:percent",String.class);
        Double rate=0.5;
        if (!StringUtil.isEmpty(rateTemp)){
            rate = Double.valueOf(rateTemp);
        }else {
            cacheService.setCacheByKey("task:rate:percent",0.5,null);
        }
        taskPool.setRate(BigDecimal.valueOf(rate));
        taskPool.setTotalScore(taskPool.getNeedNumber()*taskPool.getSingleScore());
        String randomkey=taskPool.getRandomKey();
        Integer rands=cacheService.getCacheByKey(taskPool.getMemberId()+":"+randomkey,Integer.class);
        if (rands!=null&&rands==1){
            throw new InfoException("任务已发布成功，请勿重复操作");
        }
        if (!StringUtil.isEmpty(randomkey)){
            cacheService.setCacheByKey(taskPool.getMemberId()+":"+randomkey,1,10);
        }
        return AjaxResult.getOK(memberTaskPoolService.uploadAddTaskPool(taskPool));
    }
    /**
     * 公众号发布阅读任务
     * @throws ParseException    the parse exception
     * @see
     */
    @ResponseBody
    @RequestMapping(value = "/publishReadRequest", method = RequestMethod.POST)
    public AjaxResult publishReadRequest(@RequestBody PublishReadRequestDto publishReadRequestDto){
        if (!StringUtil.isEmpty(publishReadRequestDto.getImageFile())){
            if (publishReadRequestDto.getImageFile().length()>2*1024*1024){
                throw new InfoException("图片大小不得大于2M");
            }
        }
        String randomkey=publishReadRequestDto.getRandomKey();
        Integer rands=cacheService.getCacheByKey(publishReadRequestDto.getMemberId()+":"+randomkey,Integer.class);
        if (rands!=null&&rands==1){
            throw new InfoException("任务已发布成功，请勿重复操作");
        }
        if (!StringUtil.isEmpty(randomkey)){
            cacheService.setCacheByKey(publishReadRequestDto.getMemberId()+":"+randomkey,1,10);
        }
        return AjaxResult.getOK(memberTaskPoolService.uploadReadTaskPool(publishReadRequestDto));
    }
    /**
     * 邀请码邀请
     * @throws ParseException    the parse exception
     * @see
     */
    @ResponseBody
    @RequestMapping(value = "/getInvitedCode", method = RequestMethod.GET)
    public AjaxResult getInvitedCode(Long memberId){
        return AjaxResult.getOK(memberService.getMemberDetailById(memberId).getInvitedCode());
    }

    /**
     * 获取文章列表
     * @throws ParseException    the parse exception
     * @see
     */
    @ResponseBody
    @RequestMapping(value = "/getArticleList", method = RequestMethod.POST)
    public AjaxResult getArticleList(@RequestParam(value = "memberId") Long memberId,@RequestParam(value = "requestTime") Long requestTime){
        return AjaxResult.getOK(articleService.getArticleReadRecordDto(memberId, requestTime));
    }
    /**
     * 阅读文章
     * @throws ParseException    the parse exception
     * @see
     */
    @ResponseBody
    @RequestMapping(value = "/readArticleRequest", method = RequestMethod.POST)
    public AjaxResult getArticleList(Long memberId,Long requestTime,Long articleId){
        return AjaxResult.getOK(articleService.readArticleRequest(memberId, requestTime,articleId));
    }

    /**
     * 更新版本号
     * @throws ParseException    the parse exception
     * @see
     */
    @ResponseBody
    @RequestMapping(value = "/updateAppVersion", method = RequestMethod.POST)
    public AjaxResult updateAppVersion(Integer platFlag,String version,@RequestParam(required = false) String link){
        return AjaxResult.getOK(appVersionService.updateAppVersion(platFlag, version,link));
    }
    /**
     * 获取版本号
     * @throws ParseException    the parse exception
     * @see
     */
    @ResponseBody
    @RequestMapping(value = "/getAppVersion", method = RequestMethod.GET)
    public AjaxResult getAppVersion(Integer platFlag,@RequestParam(required = false,defaultValue = "0") Integer flag){
        return AjaxResult.getOK(appVersionService.getCurrentVersion(platFlag,flag));
    }
    /**
     * 取消关注通知
     * @throws ParseException    the parse exception
     * @see
     */
    @ResponseBody
    @RequestMapping(value = "/cancelFollowOfficialAccount", method = RequestMethod.POST)
    public AjaxResult cancelFollowOfficialAccount(@RequestBody Map<String,String> map){
        String unionId=map.get("unionId");
        String appId=map.get("appId");
        return AjaxResult.getOK();
    }
    /**
     * 完成关注
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/pushAddFinished",method = RequestMethod.POST)
    public AjaxResult pushAddFinished(@RequestBody Map<String,Object> params){
        return AjaxResult.getOK(officeAccountService.pushAddFinished(params));
    }
    /**
     * 获取邀请码
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/sendIndentifyCode",method = RequestMethod.GET)
    public AjaxResult sendIndentifyCode(String telephone,@RequestParam(required = false,defaultValue ="0") Integer type,HttpServletRequest request){
        Long timestart=System.currentTimeMillis();
        String ip=IpUtils.getIpAddr(request);
        logger.info("用户获取验证码请求IP：{},获取mac时间：{}", IpUtils.getIpAddr(request),System.currentTimeMillis()-timestart);
        Integer value=cacheService.getCacheByKey(ip,Integer.class);
        if (value!=null&&value>0){
            if (value>10){
                throw new InfoException("同一设备操作过于频繁");
            }else {
                cacheService.increCacheBykey(ip,1L);
            }
        }else {
            Long time=DateUtils.getTodayEndSeconds()-DateUtils.getUnixTimestamp();
            cacheService.setCacheByKey(ip,1,time.intValue());
        }
        return AjaxResult.getOK(memberService.sendIndentifyCode(telephone,type));
    }


    /**
     * 用户注册（手机注册）
     *
     * @param registerMsg 注册信息
     * @return AjaxResult 注册成功与否

     * @throws ParseException    the parse exception
     * @see
     */
    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public AjaxResult registerAction(@RequestBody RegisterMsg registerMsg,HttpServletRequest request)
            throws BusinessException, DBException, ParseException {
        String source=this.getRequestFrom(request).getName();
        if (registerMsg!=null){
            Member member=registerMsg.getMember();
            if (member!=null){
                member.setSource(source);
                ThirdLogin thirdLogin = registerMsg.getThirdLogin();
                if (thirdLogin!=null){
                    thirdLogin.setSourceType(1);
                }
                return AjaxResult.getOK(memberService.register(registerMsg,1));
            }
        }
        return AjaxResult.getError();
    }

    /**
     * 修改地址
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/modifyMemberAddress", method = RequestMethod.POST)
    public AjaxResult modifyMemberAddress(HttpServletRequest request,String memberAddress){
        Long memberId=this.getUserId(request);
        if (memberId==null){
            throw new BusinessException("用户ID为空");
        }
        if (StringUtil.isEmpty(memberAddress)){
            throw new InfoException("定位地址失败");
        }
        return AjaxResult.getOK(memberService.modifyMemberAddress(memberId,memberAddress));
    }
    /**
     * 存储用户信息
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveAccountsUser", method = RequestMethod.POST)
    public AjaxResult getOfficialAccountMember(@RequestBody Map map) {
        System.out.println(JSON.toJSONString(map));
        return AjaxResult.getOK();
    }
    /**
     * 更新积分记录
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateMemberScore",method = RequestMethod.POST)
    AjaxResult updateMemberScore(HttpServletRequest request){
        Long memberId=this.getUserId(request);
        if (memberId==null){
            throw new BusinessException("用户ID为空");
        }
        RequestFrom from=this.getRequestFrom(request);
        String phoneType=RequestFrom.getById(2).getName();
        if (from.getId()==4||from.getId()==6){
            phoneType=RequestFrom.getById(4).getName();
        }
        return AjaxResult.getOK(memberScoreService.getMemberScoreById(memberId,phoneType,IpUtils.getIpAddr(request)));
    }

    /**
     * 获取用户可用余额和公众号信息
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAvaliableScoreAndWxInfo",method = RequestMethod.POST)
    AjaxResult getAvaliableScoreAndWxInfo(HttpServletRequest request){
        Long memberId=this.getUserId(request);
        if (memberId==null){
            throw new BusinessException("用户ID为空");
        }
        return AjaxResult.getOK(memberScoreService.getAvaliableScoreAndWxInfo(memberId));
    }

    /**
     * 微信公众号签到
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/signAccount", method = RequestMethod.POST)
    public AjaxResult signAccount(@RequestBody Map map) {
        return AjaxResult.getOK(memberTaskHistoryService.signAccounts(map));
    }
    /**
     * 会员登陆
     * @param mobileOrName
     * @param password
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public AjaxResult login(@RequestParam("mobileOrName")String mobileOrName,@RequestParam("password") String password){
        return AjaxResult.getOK(memberService.login(mobileOrName, password,1));
    }
    /**
     * 修改密码
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/modifyBirth", method = RequestMethod.POST)
    public AjaxResult modifyBirth(HttpServletRequest request,Long birth){
        Long memberId=this.getUserId(request);
        if (memberId==null){
            throw new BusinessException("用户ID为空");
        }
        return AjaxResult.getOK(memberService.modifyBirth(memberId,birth));
    }
    /**
     * 修改地址
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/modifyMemberName", method = RequestMethod.POST)
    public AjaxResult modifyMemberName(HttpServletRequest request,String memberName){
        Long memberId=this.getUserId(request);
        if (StringUtil.isEmpty(memberName)){
            throw new InfoException("账户名为空");
        }
        return AjaxResult.getOK(memberService.modifyMemberName(memberId,memberName));
    }
    /**
     * 签名
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/signature", method = RequestMethod.POST)
    public AjaxResult signature(Long timestamp){
        NameValuePair[] nameValue = new NameValuePair[3];
        nameValue[1]=new BasicNameValuePair("grant_type","client_credential");
        nameValue[0]=new BasicNameValuePair("appid", WechatConfig.MCH_APPID);
        nameValue[2]=new BasicNameValuePair("secret",WechatConfig.wxAppSecret);
        try {
            String params=HttpRequestUtils.get("https://api.weixin.qq.com/cgi-bin/token",nameValue);
            Map params_map=JSON.parseObject(params);
            NameValuePair[] nameValue1 = new NameValuePair[2];
            nameValue1[0]=new BasicNameValuePair("access_token",(String)params_map.get("access_token"));
            nameValue1[1]=new BasicNameValuePair("type","jsapi");
            String jsapi=HttpRequestUtils.get("https://api.weixin.qq.com/cgi-bin/ticket/getticket",nameValue1);
            Map jsapi_map=JSON.parseObject(jsapi);
            String nonceStr="weitaomiApp";
            String url="http://www.weitaomi.cn/frontPage/wap/myinvite.html";
            String signParams="jsapi_ticket="+jsapi_map.get("ticket")+"&noncestr="+nonceStr+"&timestamp="+timestamp+"&url="+url;
            System.out.println("==========>signParams="+signParams);
            String sign= HmacSHA256Utils.SHA(signParams);
            return AjaxResult.getOK(sign);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
