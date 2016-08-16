package com.weitaomi.application.controller;

import com.weitaomi.application.controller.baseController.BaseController;
import com.weitaomi.application.service.interf.IMemberTaskHistoryService;
import com.weitaomi.application.service.interf.IOfficeAccountService;
import com.weitaomi.systemconfig.dataFormat.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

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
}
