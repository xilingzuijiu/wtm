package com.weitaomi.application.model.mapper;

import com.weitaomi.application.model.bean.OfficeMember;
import com.weitaomi.application.model.bean.ThirdLogin;
import com.weitaomi.application.model.dto.MemberAccountLabel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OfficeMemberMapper extends IBaseMapper<OfficeMember> {
    int batchAddOfficeMember(@Param("memberList") List<OfficeMember> memberList, @Param("dateTime") Long dateTime);
    OfficeMember getOfficeMember(@Param("memberId") Long memberId, @Param("officialAccountId") Long officialAccountId);
    int deleteOverTimeUnfollowedAccounts(@Param("time") Integer time);
    List<OfficeMember> getOfficeMemberList(@Param("memberId") Long memberId);
    List<MemberAccountLabel> getOfficialAccountNameList(@Param("memberId") Long memberId, @Param("sourceType") Integer sourceType);
    OfficeMember getOfficeMemberByOpenId(@Param("openid") String openid);
    int deleteFollowAccountsMember(@Param("id") Long id);
    Integer updateOfficialMemberList(@Param("memberId") Long memberId);
}