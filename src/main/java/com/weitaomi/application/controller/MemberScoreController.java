package com.weitaomi.application.controller;

import com.weitaomi.application.controller.baseController.BaseController;
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
     * 增加的积分类型
     * @param typeId
     * @param score
     * @param sessionId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addMemberScore",method = RequestMethod.POST)
    AjaxResult addMemberScore(HttpServletRequest request,Long typeId, Double score, Long sessionId){
        Long memberId=this.getUserId(request);
        if (memberId==null){
            throw new BusinessException("用户ID为空");
        }
        return AjaxResult.getOK(memberScoreService.addMemberScore(memberId, typeId, score, sessionId));
    }
}
