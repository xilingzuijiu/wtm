package com.weitaomi.application.service.interf;

import com.weitaomi.application.model.bean.MemberScore;
import com.weitaomi.application.model.bean.MemberTaskHistoryDetail;
import com.weitaomi.application.model.dto.MemberTaskDto;
import com.weitaomi.application.model.dto.MemberTaskWithDetail;
import com.weitaomi.systemconfig.util.Page;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/16.
 */
public interface IMemberTaskHistoryService {
    /**
     * 获取用户的任务列表
     *
     * type :0 未完成，1已完成
     *
     * @param memberId
     * @return
     */
    public Page<MemberTaskWithDetail> getMemberTaskInfo(Long memberId, Integer type, Integer pageSize, Integer pageIndex);

    /**
     * 任务ID
     * @param taskHistoryId
     * @return
     */
    public List<MemberTaskHistoryDetail> getMemberTaskInfoDetail(Long taskHistoryId);

    /**
     * 获取每日奖励任务
     * @param memberId
     * @return
     */
    public List<MemberTaskDto> getMemberDailyTask(Long memberId);

    /**
     * 是否公众号签到
     * @param memberId
     * @return
     */
    Boolean isSignAccount(Long memberId);

    /**
     * 增加任务记录
     * @param
     * @return
     */
    public boolean addMemberTaskToHistory(Long memberId, Long taskId, Double score, Integer flag,String detail,List<MemberTaskHistoryDetail> detailList,String taskFlag);
    /**
     * 增加任务记录
     * @param
     * @return
     */
    public boolean updateMemberTaskToHistory(Long memberTaskId);

    /**
     * 每日任务记录
     * @param memberId
     * @param typeId
     * @return
     */
    MemberScore addDailyTask(Long memberId, Long typeId);

    String signAccounts(Map map);

    void recordMemberAddressAndEtc(Long memberId, String phone, String ip);
}
