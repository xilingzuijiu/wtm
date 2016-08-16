package com.weitaomi.application.model.mapper;

import com.weitaomi.application.model.bean.OfficalAccount;
import com.weitaomi.application.model.dto.OfficialAccountMsg;
import com.weitaomi.application.model.dto.UnFollowOfficicalAccountDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OfficalAccountMapper extends IBaseMapper<OfficalAccount> {
    List<UnFollowOfficicalAccountDto> getAccountsByMemberId(@Param("memberId") Long memberId);
    OfficalAccount getOfficalAccountByoriginId(@Param("originId") String originId);
    List<OfficialAccountMsg> getOfficialAccountMsg(@Param("memberId") Long memberId);
}