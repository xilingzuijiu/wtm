package com.weitaomi.application.model.mapper;

import com.github.abel533.mapper.Mapper;
import com.weitaomi.application.model.bean.MemberTaskHistory;
import com.weitaomi.application.model.dto.MemberTaskHistoryDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface MemberTaskHistoryMapper extends IBaseMapper<MemberTaskHistory> {
    List<MemberTaskHistoryDto> getMemberTaskHistoryList(@Param("memberId") Long memberId, @Param("type") Integer type, @Param("rowBounds") RowBounds rowBounds);
}