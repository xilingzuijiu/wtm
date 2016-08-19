package com.weitaomi.application.model.mapper;

import com.github.abel533.mapper.Mapper;
import com.weitaomi.application.model.bean.TaskPool;
import org.apache.ibatis.annotations.Param;

public interface TaskPoolMapper extends IBaseMapper<TaskPool> {
    TaskPool getTaskPoolByOfficialId(@Param("officialAccountId") Long officialAccountId);
    int updateTaskPoolWithScore(@Param("score") Long score, @Param("taskId") Long taskId);
}