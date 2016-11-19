package com.weitaomi.application.service.interf;

import com.weitaomi.application.model.dto.ArticleShowDto;
import com.weitaomi.systemconfig.util.Page;

import java.util.List;

/**
 * Created by Administrator on 2016/11/3.
 */
public interface IBackPageService {
    Page<ArticleShowDto> getAllArticle(Integer pageIndex, Integer pageSize);
    int patchCheckArticle(List<Long> poolIdList);

    /**
     * 上传文件
     * @param path
     * @param imageFiles
     * @return
     */
    String uploadUnyunFiles(String path, String files,String suffix);
}
