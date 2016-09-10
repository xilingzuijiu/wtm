package com.weitaomi.application.model.mapper;

import com.weitaomi.application.model.bean.Article;
import com.weitaomi.application.model.dto.ArticleSearch;
import com.weitaomi.application.model.dto.ArticleShowDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface ArticleMapper extends IBaseMapper<Article> {
    List<ArticleShowDto> getAtricleList(@Param("memberId") Long memberId, @Param("articleSearch")ArticleSearch articleSearch, @Param("rowBounds")RowBounds rowBounds);
    Integer putArticleToTop(@Param("articleId") Long articleId,@Param("isTop") Integer isTop);
    Integer updateArticleByRead(@Param("articleId") Long articleId,@Param("typeId")Integer typeId);
    Article getArticleByUrl(@Param("url") String url);
}