package com.weitaomi.application.service.interf;

import com.weitaomi.application.model.bean.MemberScore;
import com.weitaomi.application.model.bean.MemberScoreFlow;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by supumall on 2016/7/8.
 */
public interface IMemberScoreService {
    /**
     * 增加的积分类型
     * @param memberId
     * @param typeId
     * @param score
     * @param sessionId
     * @return
     */
    MemberScore addMemberScore(Long memberId,Long typeId,Integer isFinished,Double score,String sessionId);

    /**
     * 定时持久化前日平台奖励
     * @return
     */
    Integer updateExtraRewardTimer();

    /**
     * 获取积分历史
     * @param memberId
     * @return
     */
    public List<MemberScoreFlow> getMemberScoreFlowList(Long memberId);

    /**
     * 更新最新积分信息
     * @param memberId
     * @return
     */
    public MemberScore getMemberScoreById(Long memberId,String phoneType);

    /**
     * 获取用户可用余额和公众号信息
     * @param memberId
     * @return
     */
    Map getAvaliableScoreAndWxInfo(long memberId);

    /**
     * 定时任务，更新用户可用米币数
     * @return
     */
    Integer updateAvaliableScore();
}
