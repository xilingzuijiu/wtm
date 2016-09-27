package com.weitaomi.application.controller;

import com.alibaba.fastjson.JSON;
import com.weitaomi.application.controller.baseController.BaseController;
import com.weitaomi.application.model.bean.Member;
import com.weitaomi.application.model.bean.TaskPool;
import com.weitaomi.application.model.dto.PublishReadRequestDto;
import com.weitaomi.application.model.dto.RegisterMsg;
import com.weitaomi.application.service.interf.*;
import com.weitaomi.systemconfig.dataFormat.AjaxResult;
import com.weitaomi.systemconfig.exception.BusinessException;
import com.weitaomi.systemconfig.exception.DBException;
import com.weitaomi.systemconfig.exception.InfoException;
import com.weitaomi.systemconfig.util.DateUtils;
import com.weitaomi.systemconfig.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
    public AjaxResult publishAddRequest(@RequestBody TaskPool taskPool){
        taskPool.setTaskType(0);
        taskPool.setCreateTime(DateUtils.getUnixTimestamp());
        taskPool.setRate(BigDecimal.valueOf(0.8));
        taskPool.setTotalScore(taskPool.getNeedNumber()*taskPool.getSingleScore());
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
    @RequestMapping(value = "/getArticleList", method = RequestMethod.GET)
    public AjaxResult getArticleList(Long memberId,Long time){
        return AjaxResult.getOK(articleService.getArticleReadRecordDto(memberId, time));
    }
    /**
     * 阅读文章
     * @throws ParseException    the parse exception
     * @see
     */
    @ResponseBody
    @RequestMapping(value = "/readArticleRequest", method = RequestMethod.GET)
    public AjaxResult getArticleList(Long memberId,Long time,Long articleId){
        return AjaxResult.getOK(articleService.readArticleRequest(memberId, time,articleId));
    }

    /**
     * 更新版本号
     * @throws ParseException    the parse exception
     * @see
     */
    @ResponseBody
    @RequestMapping(value = "/updateAppVersion", method = RequestMethod.POST)
    public AjaxResult updateAppVersion(Integer platFlag,String version){
        return AjaxResult.getOK(appVersionService.updateAppVersion(platFlag, version));
    }
    /**
     * 获取版本号
     * @throws ParseException    the parse exception
     * @see
     */
    @ResponseBody
    @RequestMapping(value = "/getAppVersion", method = RequestMethod.GET)
    public AjaxResult getAppVersion(Integer platFlag){
        return AjaxResult.getOK(appVersionService.getCurrentVersion(platFlag));
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
    public AjaxResult pushAddFinished(@RequestBody Map<String,String> params){
        System.out.println(params.get("nickname")+"======="+params.get("sex")+ JSON.toJSONString(params));
        return AjaxResult.getOK(officeAccountService.pushAddFinished(params));
    }
    /**
     * 获取邀请码
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/sendIndentifyCode",method = RequestMethod.GET)
    public AjaxResult sendIndentifyCode(String telephone,@RequestParam(required = false,defaultValue ="0") Integer type){
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
                return AjaxResult.getOK(memberService.register(registerMsg));
            }
        }
        return AjaxResult.getError();
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
     * 微信服务号签到
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/signAccount", method = RequestMethod.POST)
    public AjaxResult signAccount(@RequestBody Map map) {
        System.out.println(JSON.toJSONString(map));
        return AjaxResult.getOK();
    }
}
