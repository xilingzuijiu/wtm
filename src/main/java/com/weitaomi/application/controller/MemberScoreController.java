package com.weitaomi.application.controller;

import com.weitaomi.application.service.interf.IMemberScoreService;
import com.weitaomi.systemconfig.dataFormat.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by supumall on 2016/7/23.
 */
@Controller
@RequestMapping("/app/admin/memberScore")
public class MemberScoreController {
    @Autowired
    private IMemberScoreService memberScoreService;

    /**
     * 增加的积分类型
     * @param memberId
     * @param typeId
     * @param score
     * @param sessionId
     * @return
     */
    @ResponseBody
    @RequestMapping("/addMemberScore")
    AjaxResult addMemberScore(Long memberId, Long typeId, Double score, Long sessionId){
        return AjaxResult.getOK(memberScoreService.addMemberScore(memberId, typeId, score, sessionId));
    }
}
