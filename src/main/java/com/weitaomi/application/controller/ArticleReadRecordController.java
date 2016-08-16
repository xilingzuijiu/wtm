package com.weitaomi.application.controller;

import com.weitaomi.application.model.dto.ArticleSearch;
import com.weitaomi.application.service.interf.IArticleReadRecordService;
import com.weitaomi.systemconfig.dataFormat.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by supumall on 2016/7/23.
 */
@Controller
@RequestMapping(name = "/app/admin/articleRead")
public class ArticleReadRecordController {
    @Autowired
    private IArticleReadRecordService articleReadRecordService;

    /**
     * 获取用户阅读记录
     * @param articleSearch
     * @return
     */
    @ResponseBody
    @RequestMapping(value ="/getMemberArticleList",method = RequestMethod.POST)
    public AjaxResult getMemberArticleList(@RequestBody ArticleSearch articleSearch){
        return AjaxResult.getOK(articleReadRecordService.getMemberArticleList(articleSearch));
    }
}
