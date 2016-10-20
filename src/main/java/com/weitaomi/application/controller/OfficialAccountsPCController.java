package com.weitaomi.application.controller;

import com.alibaba.fastjson.JSON;
import com.weitaomi.application.controller.baseController.BaseController;
import com.weitaomi.application.model.dto.AddOfficalAccountDto;
import com.weitaomi.application.model.dto.OfficialAccountMsg;
import com.weitaomi.application.model.mapper.ThirdLoginMapper;
import com.weitaomi.application.service.interf.IOfficeAccountService;
import com.weitaomi.systemconfig.dataFormat.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/11.
 */
@Controller
@RequestMapping("/pc/admin/official")
public class OfficialAccountsPCController extends BaseController{
    @Autowired
    private IOfficeAccountService officeAccountService;
    @Autowired
    private ThirdLoginMapper thirdLoginMapper;

    /**
     * 获取用户可关注公众号列表
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getFollowOfficialAccountList",method = RequestMethod.POST)
    public AjaxResult getFollowOfficialAccountList(HttpServletRequest httpServletRequest, String unionId){
        Long memberId=super.getUserId(httpServletRequest);
        return AjaxResult.getOK(officeAccountService.getOfficialAccountMsg(memberId,unionId));
    }

    /**
     * 完成关注
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/pushAddFinished",method = RequestMethod.POST)
    public AjaxResult pushAddFinished(@RequestBody Map<String,String> params){
        return AjaxResult.getOK(officeAccountService.pushAddFinished(params));
    }
    /**
     * 标记即将关注该公众号
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/markAddRecord",method = RequestMethod.POST)
    public AjaxResult markAddRecord(@RequestBody OfficialAccountMsg officialAccountMsg,HttpServletRequest request){
        Long memberId=this.getUserId(request);
        return AjaxResult.getOK(officeAccountService.markAddRecord(memberId,officialAccountMsg));
    }

    /**
     * 查看已关注公众号
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getOfficialAccountMsgList")
    public AjaxResult getOfficialAccountMsgList(HttpServletRequest request){
        Long memberId=super.getUserId(request);
        return AjaxResult.getOK(officeAccountService.getOfficialAccountMsgList(memberId));
    }

    /**
     * 更新已关注公众号
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/signOfficialAccountMsgList",method = RequestMethod.POST)
    public AjaxResult signOfficialAccountMsgList(HttpServletRequest request){
        Long memberId=super.getUserId(request);
        return AjaxResult.getOK(officeAccountService.signOfficialAccountMsgList(memberId));
    }
    /**
     * 添加公众号
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addOfficialAccount",method = RequestMethod.POST)
    public AjaxResult addOfficialAccount(HttpServletRequest request,String addUrl,String remark){
        Long memberId=super.getUserId(request);
        officeAccountService.addOfficialAccount(memberId,addUrl,remark);
        return AjaxResult.getOK();
    }
}
