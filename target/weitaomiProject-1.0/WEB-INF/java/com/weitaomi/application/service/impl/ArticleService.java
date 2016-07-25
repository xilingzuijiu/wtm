package com.weitaomi.application.service.impl;

import com.github.pagehelper.PageInfo;
import com.weitaomi.application.model.bean.Article;
import com.weitaomi.application.model.bean.ArticleReadRecord;
import com.weitaomi.application.model.dto.ArticleDto;
import com.weitaomi.application.model.dto.ArticleSearch;
import com.weitaomi.application.model.dto.ArticleShowDto;
import com.weitaomi.application.model.mapper.ArticleMapper;
import com.weitaomi.application.model.mapper.ArticleReadRecordMapper;
import com.weitaomi.application.service.interf.IArticleService;
import com.weitaomi.application.service.interf.ICacheService;
import com.weitaomi.application.service.interf.IMemberScoreService;
import com.weitaomi.systemconfig.exception.BusinessException;
import com.weitaomi.systemconfig.util.DateUtils;
import com.weitaomi.systemconfig.util.Page;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by supumall on 2016/7/7.
 */
@Service
public class ArticleService implements IArticleService {
    private Logger logger= LoggerFactory.getLogger(ArticleService.class);
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleReadRecordMapper articleReadRecordMapper;
    @Autowired
    private IMemberScoreService memberScoreService;
    @Autowired
    private ICacheService cacheService;
    @Override
    public Page<ArticleShowDto> getAllArticle(ArticleSearch articleSearch) {
        if (articleSearch.getSearchWay()==0){
            List<ArticleShowDto> articleShowDtoList=articleMapper.getAtricleList(articleSearch,new RowBounds(articleSearch.getPageIndex(),articleSearch.getPageSize()));

            PageInfo<ArticleShowDto> showDtoPage=new PageInfo<ArticleShowDto>(articleShowDtoList);
            return Page.trans(showDtoPage);
        }else{
            //TODO
        }
        return null;
    }

    @Override
    @Transactional
    public Boolean addArticle(ArticleDto article) {
        if (article==null){
            throw new BusinessException("文章信息为空");
        }
        if (article.getTitle()==null||!article.getTitle().isEmpty()){
            throw new BusinessException("文章标题为空");
        }
        if (article.getArticleAbstract()==null||article.getArticleAbstract().isEmpty()){
            throw new BusinessException("文章摘要为空");
        }
        if (article.getUrl()==null||!article.getUrl().isEmpty()){
            throw new BusinessException("文章地址为空");
        }
        if (article.getUserId()==null){
            throw new BusinessException("文章所属机构为空");
        }
        article.setCreateTime(DateUtils.getUnixTimestamp());
        return articleMapper.insertSelective(article)>=0?true:false;
    }

    @Override
    @Transactional
    public Boolean modifyAticle(Article article) {
        if (article==null){
            throw new BusinessException("文章信息为空");
        }
        if (article.getTitle()==null||!article.getTitle().isEmpty()){
            throw new BusinessException("文章标题为空");
        }
        if (article.getArticleAbstract()==null||article.getArticleAbstract().isEmpty()){
            throw new BusinessException("文章摘要为空");
        }
        if (article.getUrl()==null||!article.getUrl().isEmpty()){
            throw new BusinessException("文章地址为空");
        }
        if (article.getUserId()==null){
            throw new BusinessException("文章所属机构为空");
        }
        return articleMapper.updateByPrimaryKeySelective(article)>=0?true:false;
    }

    @Override
    public Boolean putArticleToTop(Long articleId, Integer isTop) {
        if (articleId==null){
            throw new BusinessException("所选文章ID为空");
        }
        return articleMapper.putArticleToTop(articleId, isTop)>=0?true:false;
    }

    @Override
    @Transactional
    public Boolean readArticle(Long memberId, Long articleId,Integer typeId,Long sessionID) {
        if(memberId==null){
            throw new BusinessException("用户ID为空");
        }
        if(articleId==null){
            throw new BusinessException("文章ID为空");
        }
        String key="member:score:"+sessionID;
        String value= cacheService.getCacheByKey(key,String.class);
        if (value!=null){
            throw new BusinessException("请勿重复操作");
        }
        ArticleReadRecord articleReadRecord=new ArticleReadRecord();
        articleReadRecord.setArticleId(articleId);
        articleReadRecord.setMemberId(memberId);
        articleReadRecord.setType(typeId);
        articleReadRecord.setCreateTime(DateUtils.getUnixTimestamp());
        if (typeId==0) {
            articleReadRecordMapper.insertSelective(articleReadRecord);
            //TODO 修改typeID
            memberScoreService.addMemberScore(memberId,3L, 3.00, sessionID);
        }
        articleMapper.updateArticleByRead(articleId,typeId);
        Integer times=45;
        cacheService.setCacheByKey(key,memberId.toString(),times);
        return true;
    }
}
