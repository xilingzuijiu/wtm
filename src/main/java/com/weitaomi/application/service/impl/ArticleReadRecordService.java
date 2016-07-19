package com.weitaomi.application.service.impl;

import com.weitaomi.application.model.dto.ArticleReadRecordDto;
import com.weitaomi.application.model.dto.ArticleSearch;
import com.weitaomi.application.model.mapper.ArticleMapper;
import com.weitaomi.application.model.mapper.ArticleReadRecordMapper;
import com.weitaomi.application.service.interf.IArticleReadRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by supumall on 2016/7/8.
 */
@Service
public class ArticleReadRecordService implements IArticleReadRecordService {
    private final Logger logger = LoggerFactory.getLogger(MemberScoreService.class);
    @Autowired
    private ArticleReadRecordMapper articleReadRecordMapper;
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public List<ArticleReadRecordDto> getMemberArticleList(ArticleSearch articleSearch) {
        return null;
    }
}
