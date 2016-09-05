package com.weitaomi.application.service.interf;

import com.weitaomi.application.model.bean.TaskPool;
import com.weitaomi.application.model.dto.RequireFollowerParamsDto;

/**
 * Created by Administrator on 2016/8/24.
 */
public interface IMemberTaskPoolService {
    /**
     * 发布求粉任务
     * @param taskPool
     * @return
     */
    public String uploadTaskPool(TaskPool taskPool);
    /**
     * 获取求粉页相关信息
     * @param memberId
     * @param time
     * @return
     */
    public RequireFollowerParamsDto getRequireFollowerParamsDto(Long memberId, Long time);
}
