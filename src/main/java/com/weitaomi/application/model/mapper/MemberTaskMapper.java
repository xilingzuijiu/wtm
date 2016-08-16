package com.weitaomi.application.model.mapper;

import com.weitaomi.application.model.bean.MemberTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberTaskMapper extends IBaseMapper<MemberTask> {
    List<MemberTask> getMemberTaskFinished(@Param("memberId") Long memberId, @Param("start") Long start, @Param("end") Long end);
    List<MemberTask> getAllMemberTask();
}