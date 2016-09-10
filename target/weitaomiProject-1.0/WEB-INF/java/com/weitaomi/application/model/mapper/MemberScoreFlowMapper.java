package com.weitaomi.application.model.mapper;

import com.github.abel533.mapper.Mapper;
import com.weitaomi.application.model.bean.MemberScoreFlow;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberScoreFlowMapper extends IBaseMapper<MemberScoreFlow> {
    List<MemberScoreFlow> getMemberScoreFlowListByMemberId(@Param("memberId") Long memberId);
    MemberScoreFlow getMemberScoreFlow(@Param("memberId") Long memberId, @Param("score") Double score,
                                       @Param("createTime") Long createTime, @Param("typeId") Long typeId,
                                       @Param("isFinished") Integer isFinished);
}