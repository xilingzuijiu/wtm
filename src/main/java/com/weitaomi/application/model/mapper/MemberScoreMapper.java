package com.weitaomi.application.model.mapper;

import com.weitaomi.application.model.bean.MemberScore;
import org.apache.ibatis.annotations.Param;

public interface MemberScoreMapper extends IBaseMapper<MemberScore> {
    MemberScore getMemberScoreByMemberId(@Param("memberId")Long memberId);
}