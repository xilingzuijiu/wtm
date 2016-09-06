package com.weitaomi.application.controller;

import com.weitaomi.application.controller.baseController.BaseController;
import com.weitaomi.application.model.bean.TaskPool;
import com.weitaomi.application.service.interf.IMemberTaskPoolService;
import com.weitaomi.systemconfig.dataFormat.AjaxResult;
import com.weitaomi.systemconfig.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;

/**
 * Created by Administrator on 2016/9/6.
 */
@Controller
@RequestMapping("/app/admin/taskPool")
public class TaskPoolController extends BaseController {
    @Autowired
    private IMemberTaskPoolService memberTaskPoolService;

    /**
     * 获取公众号任务管理列表
     * @param officialAccountId
     * @param type
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getTaskPoolDto", method = RequestMethod.POST)
    public AjaxResult getTaskPoolDto(Long officialAccountId, Integer type, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(defaultValue = "0")int pageIndex){
        return AjaxResult.getOK(memberTaskPoolService.getTaskPoolDto(officialAccountId,type,pageSize,pageIndex));
    }
    /**
     * 修改任务
     * @param taskPoolId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateTaskPoolDto", method = RequestMethod.POST)
    public AjaxResult updateTaskPoolDto(Long taskPoolId,int isPublishNow){
        return AjaxResult.getOK(memberTaskPoolService.updateTaskPoolDto(taskPoolId,isPublishNow));
    }
    /**
     * 申请积分退回
     * @param taskPoolId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/applyReturnBackScore", method = RequestMethod.POST)
    public AjaxResult applyReturnBackScore(Long taskPoolId, HttpServletRequest request){
        Long memberId=this.getUserId(request);
        return AjaxResult.getOK();
    }
}
