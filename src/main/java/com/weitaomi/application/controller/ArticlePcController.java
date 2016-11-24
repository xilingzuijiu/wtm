package com.weitaomi.application.controller;

import com.alibaba.fastjson.JSON;
import com.weitaomi.application.controller.baseController.BaseController;
import com.weitaomi.application.model.bean.Article;
import com.weitaomi.application.model.bean.Member;
import com.weitaomi.application.model.dto.ArticleReadRecordDto;
import com.weitaomi.application.model.dto.ArticleSearch;
import com.weitaomi.application.model.dto.RequestFrom;
import com.weitaomi.application.model.mapper.ArticleMapper;
import com.weitaomi.application.service.interf.IArticleService;
import com.weitaomi.application.service.interf.ICacheService;
import com.weitaomi.systemconfig.dataFormat.AjaxResult;
import com.weitaomi.systemconfig.exception.InfoException;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/11.
 */
@Controller
@RequestMapping("/pc/admin/article")
public class ArticlePcController extends BaseController {
    @Autowired
    private IArticleService articleService;
    @Autowired
    private ICacheService cacheService;
    @Autowired
    private ArticleMapper articleMapper;
    /**
     * 根据条件获取文章列表
     * @param articleSearch
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAllArticle",method = RequestMethod.POST)
    public AjaxResult getAllArticle(HttpServletRequest request, @RequestBody(required = false) ArticleSearch articleSearch){
        if (true){
            throw new InfoException("阅读板块优化中，请到app执行任务");
        }
        Long memberId=this.getUserId(request);
        RequestFrom from=this.getRequestFrom(request);
        Integer flag=0;
        if (from.getId()==4||from.getId()==6){
            flag=0;
        }else if (from.getId()==5||from.getId()==7){
            flag=1;
        }
        return AjaxResult.getOK(articleService.getAllArticle(memberId,articleSearch,flag));
    }
    /**
     * 阅读文章
     * @throws ParseException    the parse exception
     * @see
     */
    @ResponseBody
    @RequestMapping(value = "/pcreadArticleRequest", method = RequestMethod.POST)
    public AjaxResult getArticleList(HttpServletRequest request,@RequestParam(required = false) Long time, Long articleId){
        long memberId=this.getUserId(request);
//TODO : 此处为wap站阅读文章入口，暂时屏蔽
//    articleService.pcreadArticleRequest(memberId,articleId);
        return AjaxResult.getOK();
    }
    /**
     * 阅读文章
     * @throws ParseException    the parse exception
     * @see
     */
    @ResponseBody
    @RequestMapping(value = "/readArticleRequest", method = RequestMethod.GET)
    public ModelAndView getArticleList(String uuid, Long articleId, HttpServletRequest request, HttpServletResponse response){
        Long time1=System.currentTimeMillis();
        Map<String,Object> params=cacheService.getCacheByKey("member:obtain:artile:readList:"+uuid,Map.class);
        if (params!=null){
            Long memberId=Long.parseLong(params.get("memberId").toString());
            Long requestTime=Long.parseLong(params.get("createTime").toString());
            String onlyId=(String)params.get("onlyId");
            if (memberId!=null&&requestTime!=null){
                Member member=memberMapper.selectByPrimaryKey(memberId);
                if (member!=null){
                    String onlyIdTemp=new Sha256Hash(uuid, member.getSalt()).toString();
                    if (onlyIdTemp.equals(onlyId)) {
                        System.out.println(System.currentTimeMillis() - time1);
                        if (articleService.readArticleRequest(memberId, requestTime, articleId)) {
                            Article article = articleMapper.selectByPrimaryKey(articleId);
                            try {
                                response.setHeader("Access-Control-Allow-Origin", "*");
                                response.sendRedirect(article.getUrl());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }else {
                return null;
            }
        }
        return null;
    }
    @RequestMapping(value = "/getReadList",method =RequestMethod.GET)
    public ModelAndView loadReadList(String uuid) {
        ModelAndView modelAndView=new ModelAndView("readList.jsp");
        List<ArticleReadRecordDto> articleReadRecordDtoList=articleService.getArticleReadRecordDto(uuid);
        modelAndView.addObject("articleReadRecordDtoList", JSON.toJSONString(articleReadRecordDtoList));
        return modelAndView;
    }
}
