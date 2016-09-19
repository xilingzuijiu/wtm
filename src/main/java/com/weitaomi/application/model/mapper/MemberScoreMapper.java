package com.weitaomi.application.model.mapper;

import com.weitaomi.application.model.bean.MemberScore;
import com.weitaomi.application.model.dto.MemberScoreFlowDto;
import com.weitaomi.application.model.dto.MyWalletDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface MemberScoreMapper extends IBaseMapper<MemberScore> {
    MemberScore getMemberScoreByMemberId(@Param("memberId")Long memberId);
    Double getAvaliableMemberScore(@Param("memberId")Long memberId, @Param("time") Long time);
    List<MemberScoreFlowDto> getMyWalletDtoByMemberId(@Param("memberId") Long memberId, @Param("rowBounds") RowBounds rowBounds);
}