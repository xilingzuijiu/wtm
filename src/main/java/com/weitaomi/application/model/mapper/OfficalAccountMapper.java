package com.weitaomi.application.model.mapper;

import com.weitaomi.application.model.bean.OfficialAccount;
import com.weitaomi.application.model.dto.OfficialAccountMsg;
import com.weitaomi.application.model.dto.OfficialAccountWithScore;
import com.weitaomi.application.model.dto.UnFollowOfficicalAccountDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OfficalAccountMapper extends IBaseMapper<OfficialAccount> {
    List<UnFollowOfficicalAccountDto> getAccountsByMemberId(@Param("memberId") Long memberId);
    OfficialAccount getOfficalAccountByoriginId(@Param("originId") String originId);
    List<OfficialAccountMsg> getOfficialAccountMsg(@Param("memberId") Long memberId);
    OfficialAccountWithScore getOfficialAccountWithScoreById(@Param("id") Long id);
}