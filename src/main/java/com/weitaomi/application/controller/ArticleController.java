package com.weitaomi.application.controller;

import com.weitaomi.application.model.bean.Article;
import com.weitaomi.application.model.dto.ArticleDto;
import com.weitaomi.application.model.dto.ArticleSearch;
import com.weitaomi.application.service.interf.IArticleService;
import com.weitaomi.systemconfig.dataFormat.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by supumall on 2016/7/23.
 */
@Controller
@RequestMapping(name = "/app/admin/article")
public class ArticleController {
    @Autowired
    private IArticleService articleService;
    /**
     * 根据条件获取文章列表
     * @param articleSearch
     * @return
     */
    @ResponseBody
    @RequestMapping("/getAllArticle")
    public AjaxResult getAllArticle(@RequestBody ArticleSearch articleSearch){
        return AjaxResult.getOK(articleService.getAllArticle(articleSearch));
    }

    /**
     * 增加文章
     * @param articleDto
     * @return
     */
    @ResponseBody
    @RequestMapping("/addArticle")
    public AjaxResult addArticle(@RequestBody ArticleDto articleDto){
        return AjaxResult.getOK(articleService.addArticle(articleDto));
    }

    /**
     * 修改文章
     * @param article
     * @return
     */
    @ResponseBody
    @RequestMapping("/modifyAticle")
    public AjaxResult modifyAticle(@RequestBody Article article){
        return AjaxResult.getOK(articleService.modifyAticle(article));
    }

    /**
     * 文章ID，置顶与否状态 0：不置顶，1：置顶
     * @param articleId
     * @param isTop
     * @return
     */
    @ResponseBody
    @RequestMapping("/putArticleToTop")
    public AjaxResult putArticleToTop(Long articleId,Integer isTop){
        return AjaxResult.getOK(articleService.putArticleToTop(articleId, isTop));
    }

    /**
     * 用户点击阅读/点赞文章
     * @param memberId
     * @param articleId
     * @return
     */
    @ResponseBody
    @RequestMapping("/readArticle")
    public AjaxResult readArticle(Long memberId,Long articleId,Integer typeId,Long sessionmdID){
        return AjaxResult.getOK(articleService.readArticle(memberId, articleId, typeId, sessionmdID));
    }

}
