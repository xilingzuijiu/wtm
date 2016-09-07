package com.weitaomi.application.model.mapper;

import com.github.abel533.mapper.Mapper;
import com.weitaomi.application.model.bean.TaskPool;
import com.weitaomi.application.model.dto.TaskPoolDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface TaskPoolMapper extends IBaseMapper<TaskPool> {
    TaskPool getTaskPoolByOfficialId(@Param("officialAccountId") Long officialAccountId, @Param("isPublishNow") Integer isPublishNow);
    int updateTaskPoolWithScore(@Param("score") Integer score, @Param("taskId") Long taskId);
    List<TaskPoolDto> getTaskPoolArticleDto(@Param("officialAccount") Long officialAccount, @Param("rowBounds") RowBounds rowBounds);
    List<TaskPoolDto> getTaskPoolAccountDto(@Param("officialAccount") Long officialAccount, @Param("rowBounds") RowBounds rowBounds);
}