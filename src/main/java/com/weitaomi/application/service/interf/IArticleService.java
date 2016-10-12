package com.weitaomi.application.service.interf;

import com.weitaomi.application.model.bean.Article;
import com.weitaomi.application.model.dto.ArticleDto;
import com.weitaomi.application.model.dto.ArticleReadRecordDto;
import com.weitaomi.application.model.dto.ArticleSearch;
import com.weitaomi.application.model.dto.ArticleShowDto;
import com.weitaomi.systemconfig.util.Page;

import java.util.List;

/**
 * Created by supumall on 2016/7/7.
 */
public interface IArticleService {
    /**
     * 根据条件获取文章列表
     * @param articleSearch
     * @return
     */
    public Page<ArticleShowDto> getAllArticle(Long memberId,ArticleSearch articleSearch);

    /**
     * 增加文章
     * @param articleDto
     * @return
     */
    public Boolean addArticle(ArticleDto articleDto);

    /**
     * 修改文章
     * @param article
     * @return
     */
    public Boolean modifyAticle(Article article);

    /**
     * 文章ID，置顶与否状态 0：不置顶，1：置顶
     * @param articleId
     * @param isTop
     * @return
     */
    public Boolean putArticleToTop(Long articleId,Integer isTop);

    /**
     * 用户点击阅读/点赞文章
     * @param memberId
     * @param articleId
     * @return
     */
    public Boolean readArticle(Long memberId,String unionId, List<Long> articleId);

    /**
     * 获取文章阅读列表
     * @param memberId
     * @param createTime
     * @return
     */
    public List<ArticleReadRecordDto> getArticleReadRecordDto(Long memberId,Long createTime);

    /**
     * 阅读文章
     * @param memberId
     * @param time
     * @param articleId
     * @return
     */
    public Boolean readArticleRequest(Long memberId,Long time,Long articleId);
    /**
     * wap站阅读文章
     * @param memberId
     * @param articleId
     * @return
     */
    public Boolean pcreadArticleRequest(Long memberId,Long articleId);
}
