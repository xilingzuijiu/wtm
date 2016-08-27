package com.weitaomi.application.controller;

import com.weitaomi.application.controller.baseController.BaseController;
import com.weitaomi.application.model.bean.Article;
import com.weitaomi.application.model.dto.ArticleDto;
import com.weitaomi.application.model.dto.ArticleSearch;
import com.weitaomi.application.service.interf.IArticleService;
import com.weitaomi.systemconfig.dataFormat.AjaxResult;
import com.weitaomi.systemconfig.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by supumall on 2016/7/23.
 */
@Controller
@RequestMapping("/app/admin/article")
public class ArticleController extends BaseController {
    @Autowired
    private IArticleService articleService;
    /**
     * 根据条件获取文章列表
     * @param articleSearch
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAllArticle",method = RequestMethod.POST)
    public AjaxResult getAllArticle(HttpServletRequest request,@RequestBody(required = false) ArticleSearch articleSearch){
        Long memberId=this.getUserId(request);
        return AjaxResult.getOK(articleService.getAllArticle(memberId,articleSearch));
    }

    /**
     * 增加文章
     * @param articleDto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addArticle",method = RequestMethod.POST)
    public AjaxResult addArticle(@RequestBody ArticleDto articleDto){
        return AjaxResult.getOK(articleService.addArticle(articleDto));
    }

    /**
     * 修改文章
     * @param article
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/modifyAticle",method = RequestMethod.POST)
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
    @RequestMapping(value = "/putArticleToTop",method = RequestMethod.POST)
    public AjaxResult putArticleToTop(Long articleId,Integer isTop){
        return AjaxResult.getOK(articleService.putArticleToTop(articleId, isTop));
    }

    /**
     * 用户点击阅读/点赞文章
     * @param articleId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/readArticle",method = RequestMethod.POST)
    public AjaxResult readArticle(HttpServletRequest request,Long articleId, Integer typeId, String sessionmdID){
        Long memberId=this.getUserId(request);
        if (memberId==null){
            throw new BusinessException("用户ID为空");
        }
        return AjaxResult.getOK(articleService.readArticle(memberId, articleId, typeId, sessionmdID));
    }

}
