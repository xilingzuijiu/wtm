package com.weitaomi.application.controller;

import com.alibaba.fastjson.JSON;
import com.weitaomi.application.controller.baseController.BaseController;
import com.weitaomi.application.model.bean.OfficialAccount;
import com.weitaomi.application.model.bean.ThirdLogin;
import com.weitaomi.application.model.dto.AddOfficalAccountDto;
import com.weitaomi.application.model.mapper.MemberMapper;
import com.weitaomi.application.model.mapper.ThirdLoginMapper;
import com.weitaomi.application.service.interf.IMemberTaskHistoryService;
import com.weitaomi.application.service.interf.IOfficeAccountService;
import com.weitaomi.systemconfig.dataFormat.AjaxResult;
import com.weitaomi.systemconfig.util.JpushUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/16.
 */
@Controller
@RequestMapping("/app/admin/official")
public class OfficialAccountController extends BaseController{
    @Autowired
    private IOfficeAccountService officeAccountService;
    @Autowired
    private ThirdLoginMapper thirdLoginMapper;

    /**
     * 获取用户每日任务
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getFollowOfficialAccountList",method = RequestMethod.POST)
    public AjaxResult getFollowOfficialAccountList(HttpServletRequest httpServletRequest,String unionId){
        Long memberId=super.getUserId(httpServletRequest);
        return AjaxResult.getOK(officeAccountService.getOfficialAccountMsg(memberId,unionId));
    }

    /**
     * 加入公众号任务关注列表
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/pushAddRequest",method = RequestMethod.POST)
    public AjaxResult pushAddRequest(HttpServletRequest request,@RequestBody AddOfficalAccountDto addOfficalAccountDto){
        Long memberId=super.getUserId(request);
        officeAccountService.pushAddRequest(memberId,addOfficalAccountDto);
        return AjaxResult.getOK();
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
     * 提醒用户任务即将到期
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/notifyMemberTask",method = RequestMethod.POST)
    public AjaxResult notifyMemberTask(Map<String,String> params){
        String unionId=params.get("unionId");
        String status=params.get("status");
        if (status.equals("0")) {
            Long memberId = thirdLoginMapper.getMemberIdByUnionId(unionId);
                JpushUtils.buildRequest("任务五分钟之后即将失效，请尽快到服务号完成",memberId);
        }
        return AjaxResult.getOK();
    }

    /**
     * 获取公众号列表
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getOfficialAccountList",method = RequestMethod.POST)
    public AjaxResult getOfficialAccountList(HttpServletRequest request){
        Long memberId=super.getUserId(request);
        return AjaxResult.getOK(officeAccountService.getOfficialAccountList(memberId));
    }
    /**
     * 更新公众号
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateOfficialAccountList",method = RequestMethod.POST)
    public AjaxResult updateOfficialAccountList(Long accountId,Integer isOpen){
        return AjaxResult.getOK(officeAccountService.updateOfficialAccountList(accountId,isOpen));
    }

}
