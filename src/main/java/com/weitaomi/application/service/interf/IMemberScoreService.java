package com.weitaomi.application.service.interf;

import com.weitaomi.application.model.bean.MemberScore;

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
    MemberScore addMemberScore(Long memberId,Long typeId,Double score,Long sessionId);
}
