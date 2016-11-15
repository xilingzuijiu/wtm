package com.weitaomi.application.controller;

import com.weitaomi.application.controller.baseController.BaseController;
import com.weitaomi.application.model.dto.RequestFrom;
import com.weitaomi.application.service.interf.IMemberScoreService;
import com.weitaomi.systemconfig.dataFormat.AjaxResult;
import com.weitaomi.systemconfig.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by supumall on 2016/7/23.
 */
@Controller
@RequestMapping("/app/admin/memberScore")
public class MemberScoreController extends BaseController {
    @Autowired
    private IMemberScoreService memberScoreService;

    /**
     * 获取积分记录
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getMemberScoreFlowList",method = RequestMethod.POST)
    AjaxResult getMemberScoreFlowList(HttpServletRequest request){
        Long memberId=this.getUserId(request);
        if (memberId==null){
            throw new BusinessException("用户ID为空");
        }
        return AjaxResult.getOK(memberScoreService.getMemberScoreFlowList(memberId));
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
        return AjaxResult.getOK(memberScoreService.getMemberScoreById(memberId,from.getName()));
    }
}
