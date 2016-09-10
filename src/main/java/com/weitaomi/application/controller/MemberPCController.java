package com.weitaomi.application.controller;

import com.weitaomi.application.controller.baseController.BaseController;
import com.weitaomi.application.model.bean.Member;
import com.weitaomi.application.model.bean.TaskPool;
import com.weitaomi.application.model.dto.PublishReadRequestDto;
import com.weitaomi.application.model.dto.RegisterMsg;
import com.weitaomi.application.service.interf.IArticleService;
import com.weitaomi.application.service.interf.IMemberService;
import com.weitaomi.application.service.interf.IMemberTaskPoolService;
import com.weitaomi.systemconfig.dataFormat.AjaxResult;
import com.weitaomi.systemconfig.exception.BusinessException;
import com.weitaomi.systemconfig.exception.DBException;
import com.weitaomi.systemconfig.exception.InfoException;
import com.weitaomi.systemconfig.util.DateUtils;
import com.weitaomi.systemconfig.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/23.
 */
@Controller
@RequestMapping("/pc/admin/member")
public class MemberPCController extends BaseController {
    @Autowired
    private IMemberService memberService;
    @Autowired
    private IMemberTaskPoolService memberTaskPoolService;
    @Autowired
    private IArticleService articleService;
    /**
     * 获取用户信息
     * @throws ParseException    the parse exception
     * @see
     */
    @ResponseBody
    @RequestMapping(value = "/getMemberAccountPublishMsg", method = RequestMethod.POST)
    public AjaxResult getMemberAccountPublishMsg(Long memberId,Long time){
        return AjaxResult.getOK(memberTaskPoolService.getRequireFollowerParamsDto(memberId, time));
    }
    /**
     * 公众号发布关注任务
     * @throws ParseException    the parse exception
     * @see
     */
    @ResponseBody
    @RequestMapping(value = "/publishAddRequest", method = RequestMethod.POST)
    public AjaxResult publishAddRequest(@RequestBody TaskPool taskPool){
        taskPool.setTaskType(0);
        taskPool.setCreateTime(DateUtils.getUnixTimestamp());
        taskPool.setRate(BigDecimal.valueOf(0.8));
        taskPool.setTotalScore(taskPool.getNeedNumber()*taskPool.getSingleScore());
        return AjaxResult.getOK(memberTaskPoolService.uploadAddTaskPool(taskPool));
    }
    /**
     * 公众号发布阅读任务
     * @throws ParseException    the parse exception
     * @see
     */
    @ResponseBody
    @RequestMapping(value = "/publishReadRequest", method = RequestMethod.POST)
    public AjaxResult publishReadRequest(@RequestBody PublishReadRequestDto publishReadRequestDto){
        if (!StringUtil.isEmpty(publishReadRequestDto.getImageFile())){
            if (publishReadRequestDto.getImageFile().length()>2*1024*1024){
                throw new InfoException("图片大小不得大于2M");
            }
        }
        return AjaxResult.getOK(memberTaskPoolService.uploadReadTaskPool(publishReadRequestDto));
    }
    /**
     * 邀请码邀请
     * @throws ParseException    the parse exception
     * @see
     */
    @ResponseBody
    @RequestMapping(value = "/getInvitedCode", method = RequestMethod.GET)
    public AjaxResult getInvitedCode(Long memberId){
        return AjaxResult.getOK(memberService.getMemberDetailById(memberId).getInvitedCode());
    }

    /**
     * 邀请码邀请
     * @throws ParseException    the parse exception
     * @see
     */
    @ResponseBody
    @RequestMapping(value = "/getArticleList", method = RequestMethod.GET)
    public AjaxResult getArticleList(Long memberId,Long time){
        return AjaxResult.getOK(articleService.getArticleReadRecordDto(memberId, time));
    }
}
