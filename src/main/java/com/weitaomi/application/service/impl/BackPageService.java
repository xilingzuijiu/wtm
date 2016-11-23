package com.weitaomi.application.service.impl;

import com.github.pagehelper.PageInfo;
import com.weitaomi.application.model.bean.TaskPool;
import com.weitaomi.application.model.dto.ArticleSearch;
import com.weitaomi.application.model.dto.ArticleShowDto;
import com.weitaomi.application.model.mapper.TaskPoolMapper;
import com.weitaomi.application.service.interf.IBackPageService;
import com.weitaomi.application.service.interf.IMemberTaskPoolService;
import com.weitaomi.systemconfig.constant.SystemConfig;
import com.weitaomi.systemconfig.util.DateUtils;
import com.weitaomi.systemconfig.util.Page;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/3.
 */
@Service
public class BackPageService extends BaseService implements IBackPageService {
    @Autowired
    private TaskPoolMapper taskPoolMapper;

    @Override
    public Page<ArticleShowDto> getAllArticle(Integer pageIndex, Integer pageSize) {
        List<Map<String, String>> articleShowDtoList = taskPoolMapper.getAtricleList(new RowBounds(pageIndex, pageSize));
        PageInfo<Map<String, String>> showDtoPage = new PageInfo<Map<String, String>>(articleShowDtoList);
        return Page.trans(showDtoPage);
    }

    @Override
    public int patchCheckArticle(List<Long> poolIdList) {
        if (!poolIdList.isEmpty()){
            int number=taskPoolMapper.patchCheckArticle(poolIdList);
            return  number;
        }
        return 0;
    }
}
