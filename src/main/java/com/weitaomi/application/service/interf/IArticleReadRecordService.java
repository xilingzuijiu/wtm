package com.weitaomi.application.service.interf;

import com.weitaomi.application.model.dto.ArticleReadRecordDto;
import com.weitaomi.application.model.dto.ArticleSearch;

import java.util.List;

/**
 * Created by supumall on 2016/7/8.
 */
public interface IArticleReadRecordService {

    /**
     * 获取用户阅读记录
     * @param articleSearch
     * @return
     */
    List<ArticleReadRecordDto> getMemberArticleList(ArticleSearch articleSearch);
}
