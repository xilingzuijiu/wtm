package com.weitaomi.application.model.mapper;

import com.weitaomi.application.model.bean.MemberInvitedRecord;
import com.weitaomi.application.model.dto.InvitedParamsDto;
import com.weitaomi.application.model.dto.InvitedRecord;
import com.weitaomi.application.model.dto.TotalSharedMsg;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberInvitedRecordMapper extends IBaseMapper<MemberInvitedRecord> {
    MemberInvitedRecord getMemberInvitedRecordByMemberId(@Param("memberId")Long memberId);
    List<InvitedRecord> getInvitedRecord(@Param("parentID") Long parentID);
    InvitedParamsDto getInvitedParamsDto(@Param("memberId")Long memberId);
    List<TotalSharedMsg> getTotalSharedMsg();
}