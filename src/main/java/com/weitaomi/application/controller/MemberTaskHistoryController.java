package com.weitaomi.application.controller;

import com.weitaomi.application.controller.baseController.BaseController;
import com.weitaomi.application.service.interf.IMemberTaskHistoryService;
import com.weitaomi.systemconfig.dataFormat.AjaxResult;
import com.weitaomi.systemconfig.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2016/8/16.
 */
@Controller
@RequestMapping("/app/admin/memberTask")
public class MemberTaskHistoryController extends BaseController {
    @Autowired
    private IMemberTaskHistoryService memberTaskHistoryService;

    /**
     * 获取用户每日任务
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getMemberDailyTask",method = RequestMethod.POST)
    public AjaxResult getMemberDailyTask(HttpServletRequest httpServletRequest){
        Long memberId=super.getUserId(httpServletRequest);
        return AjaxResult.getOK(memberTaskHistoryService.getMemberDailyTask(memberId));
    }
    /**
     * 是否服务号签到
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/isSignAccount",method = RequestMethod.POST)
    public AjaxResult isSignAccount(HttpServletRequest httpServletRequest){
        Long memberId=super.getUserId(httpServletRequest);
        return AjaxResult.getOK(memberTaskHistoryService.isSignAccount(memberId));
    }
    /**
     * 获取用户任务记录
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getMemberTaskInfo",method = RequestMethod.POST)
    public AjaxResult getMemberTaskInfo(HttpServletRequest httpServletRequest, Integer type,@RequestParam(defaultValue ="10") Integer pageSize,Integer pageIndex){
        Long memberId=super.getUserId(httpServletRequest);
        return AjaxResult.getOK(memberTaskHistoryService.getMemberTaskInfo(memberId,type,pageSize,pageIndex));
    }
    /**
     * 获取用户任务记录详情
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getMemberTaskInfoDetail")
    public AjaxResult getMemberTaskInfoDetail(Long taskHistoryId){
        return AjaxResult.getOK(memberTaskHistoryService.getMemberTaskInfoDetail(taskHistoryId));
    }
    /**
     * 用户每日任务提交
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addDailyTask")
    public AjaxResult addDailyTask(HttpServletRequest request, @RequestParam Long taskId){
        Long memberId=this.getUserId(request);
        if (memberId==null){
            throw new BusinessException("用户ID为空");
        }
        return AjaxResult.getOK(memberTaskHistoryService.addDailyTask(memberId,taskId));
    }
}
