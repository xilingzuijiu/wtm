package com.weitaomi.application.service.interf;

import com.weitaomi.application.model.bean.Article;
import com.weitaomi.application.model.dto.ArticleSearch;
import com.weitaomi.application.model.dto.ArticleShowDto;

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
    public List<ArticleShowDto> getAllArticle(ArticleSearch articleSearch);

    /**
     * 增加文章
     * @param article
     * @return
     */
    public Boolean addArticle(Article article);

}
