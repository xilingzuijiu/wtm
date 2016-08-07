package com.weitaomi.application.controller;

import com.weitaomi.application.controller.baseController.BaseController;
import com.weitaomi.application.model.bean.Member;
import com.weitaomi.application.model.bean.ThirdLogin;
import com.weitaomi.application.model.dto.RegisterMsg;
import com.weitaomi.application.service.interf.IMemberService;
import com.weitaomi.systemconfig.dataFormat.AjaxResult;
import com.weitaomi.systemconfig.exception.BusinessException;
import com.weitaomi.systemconfig.exception.DBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Administrator on 2016/6/27.
 */
@Controller
@RequestMapping("/app/admin/member")
public class MemberController  extends BaseController {
    @Autowired
    private IMemberService memberService;
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
     * 向手机发送验证码
     *
     * @param mobile 手机号
     * @param type   类型（注册、找回密码）
     * @return 获取成功与否 identify code action
     * @throws BusinessException the business exception
     */
    @ResponseBody
    @RequestMapping(value = "/getIdentifyCode", method = RequestMethod.POST)
    public AjaxResult getIdentifyCodeAction(@RequestParam("mobile") String mobile, @RequestParam(value = "type", defaultValue ="0") Integer type, HttpServletRequest request) throws BusinessException, IOException {
        String identifyCode=memberService.sendIndentifyCode(mobile,type);
        if (identifyCode!=null&&!identifyCode.isEmpty()){
            return AjaxResult.getOK(identifyCode);
        }
        return null;
    }

    /**
     * 绑定第三方登陆平台
     * @param thirdLogin
     * @return
     * @throws BusinessException
     * @throws DBException
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "/bindThirdPlat", method = RequestMethod.POST)
    public AjaxResult bindThirdPlat(@RequestBody ThirdLogin thirdLogin)
            throws BusinessException, DBException, ParseException {
        return AjaxResult.getOK(memberService.bindThirdPlat(thirdLogin));
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
        return AjaxResult.getOK(memberService.login(mobileOrName, password));
    }
    /**
     * 第三方平台登陆
     * @param openId
     * @param type
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/thirdPlatLogin", method = RequestMethod.POST)
    public AjaxResult thirdPlatLogin(@RequestParam("openId")String openId, @RequestParam("type")Integer type){
        return AjaxResult.getOK(memberService.thirdPlatLogin(openId, type));
    }
    /**
     * 获取用户信息
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getMemberDetailById", method = RequestMethod.POST)
    public AjaxResult getMemberDetailById(HttpServletRequest request){
        Long memberId=this.getUserId(request);
        if (memberId==null){
            throw new BusinessException("用户ID为空");
        }
        return AjaxResult.getOK(memberService.getMemberDetailById(memberId));
    }

    /**
     * 获取邀请记录
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getInvitedRecord", method = RequestMethod.POST)
    public AjaxResult getInvitedRecord(HttpServletRequest request){
        Long memberId=this.getUserId(request);
        if (memberId==null){
            throw new BusinessException("用户ID为空");
        }
        return AjaxResult.getOK(memberService.getMemberDetailById(memberId));
    }

}
