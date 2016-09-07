package com.weitaomi.application.service.interf;

import java.util.List;
import com.weitaomi.application.model.bean.TaskPool;
import com.weitaomi.application.model.dto.RequireFollowerParamsDto;
import com.weitaomi.application.model.dto.TaskPoolDto;
import com.weitaomi.systemconfig.util.Page;

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

    /**
     * 获取公众号任务
     * @param officialAccountId
     * @return
     */
    public Page<TaskPoolDto> getTaskPoolDto(Long officialAccountId, Integer type, int pageSize, int pageIndex);

    /**
     * 控制任务发布与否
     * @param taskPoolId
     * @param isPublishNow
     * @return
     */
    public Boolean updateTaskPoolDto(Long taskPoolId,int isPublishNow);
}
