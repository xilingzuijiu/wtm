package com.weitaomi.application.model.mapper;

import com.github.abel533.mapper.Mapper;
import com.weitaomi.application.model.bean.MemberTaskHistoryDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberTaskHistoryDetailMapper extends IBaseMapper<MemberTaskHistoryDetail> {
    List<MemberTaskHistoryDetail> getMemberTaskHistoryDetialList(@Param("taskHistoryId") Long taskHistoryId);
}