package com.weitaomi.application.model.mapper;

import com.weitaomi.application.model.bean.Article;
import org.apache.ibatis.annotations.Param;

public interface ArticleMapper extends IBaseMapper<Article> {
    Article getArticleByMemberId(@Param("memberId") Long memberId);
}