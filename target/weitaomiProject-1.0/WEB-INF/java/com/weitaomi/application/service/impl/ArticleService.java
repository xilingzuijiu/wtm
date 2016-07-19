package com.weitaomi.application.service.impl;

import com.weitaomi.application.model.bean.Article;
import com.weitaomi.application.model.dto.ArticleSearch;
import com.weitaomi.application.model.dto.ArticleShowDto;
import com.weitaomi.application.service.interf.IArticleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by supumall on 2016/7/7.
 */
@Service
public class ArticleService implements IArticleService {

    @Override
    public List<ArticleShowDto> getAllArticle(ArticleSearch articleSearch) {
        if (articleSearch.getSearchWay()==0){

        }
        return null;
    }

    @Override
    public Boolean addArticle(Article article) {
        return null;
    }
}
