package com.weitaomi.application.controller;

import com.weitaomi.application.controller.baseController.BaseController;
import com.weitaomi.application.model.dto.AddOfficalAccountDto;
import com.weitaomi.application.service.interf.IMemberTaskHistoryService;
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
 * Created by Administrator on 2016/8/16.
 */
@Controller
@RequestMapping("/app/admin/official")
public class OfficialAccountController extends BaseController{
    @Autowired
    private IOfficeAccountService officeAccountService;

    /**
     * 获取用户每日任务
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getFollowOfficialAccountList",method = RequestMethod.POST)
    public AjaxResult getFollowOfficialAccountList(HttpServletRequest httpServletRequest){
        Long memberId=super.getUserId(httpServletRequest);
        return AjaxResult.getOK(officeAccountService.getOfficialAccountMsg(memberId));
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
     * 加入公众号任务关注列表
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/pushAddFinished",method = RequestMethod.POST)
    public AjaxResult pushAddFinished(@RequestBody Map<String,String> params){
        System.out.println(params.get("originId")+"======="+params.get("unionId"));
        return AjaxResult.getOK();
    }
}
