package com.weitaomi.application.service.interf;

import com.weitaomi.application.model.bean.MemberScore;
import com.weitaomi.application.model.bean.MemberScoreFlow;

import java.util.List;

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
    MemberScore addMemberScore(Long memberId,Long typeId,Long score,String sessionId);

    /**
     * 获取积分历史
     * @param memberId
     * @return
     */
    public List<MemberScoreFlow> getMemberScoreFlowList(Long memberId);
}
