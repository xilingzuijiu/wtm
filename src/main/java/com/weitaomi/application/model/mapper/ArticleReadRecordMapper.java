package com.weitaomi.application.model.mapper;

import com.github.abel533.mapper.Mapper;
import com.weitaomi.application.model.bean.ArticleReadRecord;
import com.weitaomi.application.model.dto.ArticleReadRecordDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleReadRecordMapper extends Mapper<ArticleReadRecord> {
    int insertReadArticleRecord(@Param("memberId") Long memberId, @Param("articleIdList") List<Long> articleIdList, @Param("type") Integer type, @Param("createTime") Long createTime );
    List<ArticleReadRecordDto> getArticleReadRecordDto(@Param("memberId") Long memberId, @Param("createTime") Long createTime);
}